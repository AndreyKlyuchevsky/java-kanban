package manager.server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler implements HttpHandler {
    private final TaskManager manager;
    private final Gson gson;

    public TaskHandler(TaskManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            if (method.equals("GET")) {
                String path = exchange.getRequestURI().getQuery();
                Integer id = extractIdFromPath(path);
                if (id == null) {
                    // Запрос на получение всех Task
                    List<Task> tasks = manager.getTaskAll();
                    sendResponse(exchange, gson.toJson(tasks), HttpURLConnection.HTTP_OK);
                } else {
                    // Запрос на получение конкретного Task по ID
                    Task task = manager.getTaskById(id);
                    if (task != null) {
                        sendResponse(exchange, gson.toJson(task), HttpURLConnection.HTTP_OK);
                    } else {
                        sendResponse(exchange, "Task not found", HttpURLConnection.HTTP_NOT_FOUND);
                    }
                }
            }

            if ("POST".equals(exchange.getRequestMethod())) {
                // Обработка POST-запроса для создания или обновления Task
                Task newTask = parseTaskFromBody(exchange);

                Integer taskId = extractTaskIdFromRequest(exchange);

                if (taskId != null) {
                    // Если есть корректный id, это запрос на обновление
                    manager.updateTask(newTask);
                    sendEmptyResponse(exchange, HttpURLConnection.HTTP_OK); // Отправляем код 200 OK
                } else {
                    // Иначе, это запрос на создание нового Task
                    manager.addTask(newTask);
                    sendJsonResponse(exchange, gson.toJson(newTask), HttpURLConnection.HTTP_CREATED); // Отправляем код 201 Created
                }
            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                // Обработка DELETE-запроса для удаления Task
                Integer taskId = extractTaskIdFromRequest(exchange);
                if (taskId != null) {
                    manager.removeTaskById(taskId);
                    sendEmptyResponse(exchange, HttpURLConnection.HTTP_NO_CONTENT); // Отправляем код 204 No Content
                } else {
                    // ID не найден или неверного формата, отправляем ошибку
                    sendErrorResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Invalid Task ID");
                }
            }
        } catch (NumberFormatException e) {
            // Неправильный формат ID, отправляем ошибку
            sendErrorResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Invalid Task ID");
        } finally {
            exchange.close();
        }
    }

    private Integer extractIdFromPath(String path) {
        try {
            String[] parts = path.split("/");
            if (parts.length >= 3) {
                return Integer.parseInt(parts[2]);
            }
        } catch (NumberFormatException e) {
            // Произошла ошибка при парсинге ID
        }
        return null;
    }

    private Integer extractTaskIdFromRequest(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        String[] queryParams = query.split("=");
        if (queryParams.length == 2 && "id".equals(queryParams[0])) {
            return Integer.parseInt(queryParams[1]);
        }
        // ID не найден или неверного формата
        return null;
    }

    private Task parseTaskFromBody(HttpExchange exchange) throws IOException {
        // Распарсивание Task из JSON-тела запроса
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            return gson.fromJson(requestBody, Task.class);
        } catch (JsonSyntaxException e) {
            // Если формат JSON неверный, отправляем ошибку и возвращаем null
            sendErrorResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Invalid JSON format");
            return null;
        }
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void sendJsonResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void sendEmptyResponse(HttpExchange exchange, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, 0);
    }

    private void sendErrorResponse(HttpExchange exchange, int statusCode, String errorMessage) throws IOException {
        String response = "{\"error\": \"" + errorMessage + "\"}";
        sendJsonResponse(exchange, response, statusCode);
    }
}

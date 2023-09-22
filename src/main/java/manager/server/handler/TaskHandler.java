package manager.server.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import manager.file.FileBackedTasksManager;
import model.Epic;
import model.SubTask;
import model.Task;
import com.google.gson.Gson;

import java.io.*;
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
                    // Запрос на получение всех Epic
                    List<SubTask> subTasks = manager.getSubTaskAll();
                    sendResponse(exchange, gson.toJson(subTasks), HttpURLConnection.HTTP_OK);
                } else {
                    // Запрос на получение конкретного SubTask по ID
                    Epic epic = manager.getEpicById(id);
                    if (epic != null) {
                        sendResponse(exchange, gson.toJson(epic), HttpURLConnection.HTTP_OK);
                    } else {
                        sendResponse(exchange, "Epic not found", HttpURLConnection.HTTP_NOT_FOUND);
                    }
                }
            }

            if ("POST".equals(exchange.getRequestMethod())) {
                // Обработка POST-запроса для создания или обновления Epic
                Task newTask = parseTaskFromBody(exchange);

                if (newTask != null) {
                    Integer epicId = extractTaskIdFromRequest(exchange);

                    if (epicId != null) {
                        // Если есть корректный id, это запрос на обновление
                        manager.updateTask(newTask);
                        sendEmptyResponse(exchange, HttpURLConnection.HTTP_OK ); // Отправляем код 200 OK
                    } else {
                        // Иначе, это запрос на создание нового Epic
                        manager.addTask(newTask);
                        sendJsonResponse(exchange, gson.toJson(newTask), HttpURLConnection.HTTP_CREATED); // Отправляем код 201 Created
                    }
                }
            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                // Обработка DELETE-запроса для удаления Epic
                Integer epicId = extractTaskIdFromRequest(exchange);
                if (epicId != null) {
                    manager.removeEpicById(epicId);
                    sendEmptyResponse(exchange, HttpURLConnection.HTTP_NO_CONTENT); // Отправляем код 204 No Content
                } else {
                    // ID не найден или неверного формата, отправляем ошибку

                    sendErrorResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Invalid Epic ID");
                }
            }
        } catch (NumberFormatException e) {
            // Неправильный формат ID, отправляем ошибку
            sendErrorResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Invalid Epic ID");
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

    private SubTask parseTaskFromBody(HttpExchange exchange) throws IOException {
        // Распарсивание Task из JSON-тела запроса
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            return gson.fromJson(requestBody, SubTask.class);
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

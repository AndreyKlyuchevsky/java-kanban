package manager.server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.file.FileBackedTasksManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler implements HttpHandler {
    private final FileBackedTasksManager taskManager;
    private final Gson gson;
    private static final int INVALID_ID = -1;

    public EpicHandler(FileBackedTasksManager manager, Gson gson) {
        this.taskManager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        int id = extractIdFromPath(path);

        try {
            if (method.equals("GET")) {
                if (id == INVALID_ID) {
                    // Запрос на получение всех SubTask
                    List<SubTask> subTasks = taskManager.getSubTaskAll();
                    sendResponse(exchange, gson.toJson(subTasks), HttpURLConnection.HTTP_OK);
                } else {
                    // Запрос на получение конкретного SubTask по ID
                    SubTask subTask = taskManager.getSubtaskById(id);
                    if (subTask != null) {
                        sendResponse(exchange, gson.toJson(subTask), HttpURLConnection.HTTP_OK);
                    } else {
                        sendResponse(exchange, "SubTask not found", HttpURLConnection.HTTP_NOT_FOUND);
                    }
                }
            }

            if ("POST".equals(exchange.getRequestMethod())) {
                // Обработка POST-запроса для создания или обновления Epic
                Epic newEpic = parseEpicFromBody(exchange);

                if (newEpic != null) {
                    int epicId = extractEpicIdFromRequest(exchange);

                    if (epicId != INVALID_ID) {
                        // Если есть корректный id, это запрос на обновление
                        taskManager.updateEpic(newEpic);
                        sendEmptyResponse(exchange, HttpURLConnection.HTTP_OK ); // Отправляем код 200 OK
                    } else {
                        // Иначе, это запрос на создание нового Epic
                        taskManager.addEpic(newEpic);
                        sendJsonResponse(exchange, gson.toJson(newEpic), HttpURLConnection.HTTP_CREATED); // Отправляем код 201 Created
                    }
                }
            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                // Обработка DELETE-запроса для удаления Epic
                int epicId = extractEpicIdFromRequest(exchange);
                if (epicId != INVALID_ID) {
                    taskManager.removeEpicById(epicId);
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

    private int extractIdFromPath(String path) {
        try {
            String[] parts = path.split("/");
            if (parts.length >= 3) {
                return Integer.parseInt(parts[2]);
            }
        } catch (NumberFormatException e) {
            // Произошла ошибка при парсинге ID
        }
        return -1;
    }

    private int extractEpicIdFromRequest(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        String[] queryParams = query.split("=");
        if (queryParams.length == 2 && "id".equals(queryParams[0])) {
            return Integer.parseInt(queryParams[1]);
        }
        // ID не найден или неверного формата
        return -1;
    }

    private Epic parseEpicFromBody(HttpExchange exchange) throws IOException {
        // Распарсивание Epic из JSON-тела запроса
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            return gson.fromJson(requestBody, Epic.class);
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
        exchange.sendResponseHeaders(statusCode, -1);
    }

    private void sendErrorResponse(HttpExchange exchange, int statusCode, String errorMessage) throws IOException {
        String response = "{\"error\": \"" + errorMessage + "\"}";
        sendJsonResponse(exchange, response, statusCode);
    }
}

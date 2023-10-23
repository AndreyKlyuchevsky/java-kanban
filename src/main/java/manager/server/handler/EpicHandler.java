package manager.server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Epic;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager manager, Gson gson) {
        this.taskManager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            if (method.equals("GET")) {

                Integer id = extractEpicIdFromRequest(exchange);
                if (id == null) {
                    // Запрос на получение всех Epic
                    List<Epic> epics = taskManager.getEpicAll();
                    sendResponse(exchange, gson.toJson(epics), HttpURLConnection.HTTP_OK);
                } else {
                    // Запрос на получение конкретного Epic по ID
                    Epic epic = taskManager.getEpicById(id);
                    if (epic != null) {
                        sendResponse(exchange, gson.toJson(epic), HttpURLConnection.HTTP_OK);
                    } else {
                        sendErrorResponse(exchange, HttpURLConnection.HTTP_NOT_FOUND, "Epic not found");
                    }
                }
            }

            if ("POST".equals(exchange.getRequestMethod())) {
                // Обработка POST-запроса для создания или обновления Epic
                Epic newEpic = parseEpicFromBody(exchange);

                if (newEpic.getId() != null) {
                    // Если есть корректный id, это запрос на обновление
                    taskManager.updateEpic(newEpic);
                    sendEmptyResponse(exchange, HttpURLConnection.HTTP_OK); // Отправляем код 200 OK
                } else {
                    // Иначе, это запрос на создание нового Epic
                    taskManager.addEpic(newEpic);
                    sendJsonResponse(exchange, gson.toJson(newEpic), HttpURLConnection.HTTP_CREATED); // Отправляем код 201 Created
                }
            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                // Обработка DELETE-запроса для удаления Epic
                Integer epicId = extractEpicIdFromRequest(exchange);
                if (epicId != null) {
                    taskManager.removeEpicById(epicId);
                    sendEmptyResponse(exchange, HttpURLConnection.HTTP_NO_CONTENT); // Отправляем код 204 No Content
                } else {
                    taskManager.removeEpicAll();
                    sendEmptyResponse(exchange, HttpURLConnection.HTTP_NO_CONTENT); // Отправляем код 204 No Content
                }
            }
        } catch (JsonSyntaxException e) {
            // Ошибка синтаксиса JSON, отправляем ошибку HTTP_BAD_REQUEST
            sendErrorResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST, "Invalid JSON format");
        } catch (Exception e) {
            // Ошибка синтаксиса JSON, отправляем ошибку HTTP_BAD_REQUEST
            sendErrorResponse(exchange, HttpURLConnection.HTTP_BAD_REQUEST, "Invalid request");
        } finally {
            exchange.close();
        }
    }

    private Integer extractEpicIdFromRequest(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        if (query == null) {
            return null;
        }
        String[] queryParams = query.split("=");
        if (queryParams.length == 2 && "id".equals(queryParams[0])) {
            return Integer.parseInt(queryParams[1]);
        }
        return null;
    }

    private Epic parseEpicFromBody(HttpExchange exchange) throws IOException {
        // Распарсивание Epic из JSON-тела запроса
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        return gson.fromJson(requestBody, Epic.class);
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

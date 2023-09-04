package manager.server.handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.file.FileBackedTasksManager;
import model.Epic;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class EpicHandler implements HttpHandler {
    private final FileBackedTasksManager taskManager;
    private final Gson gson;

    public EpicHandler(FileBackedTasksManager manager, Gson gson) {
        this.taskManager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Обработка POST-запроса для создания или обновления Epic
                Epic newEpic = parseEpicFromBody(exchange);

                if (newEpic.getId() != 0) {
                    // Если есть id, это запрос на обновление
                    taskManager.updateEpic(newEpic);
                    sendEmptyResponse(exchange, 200); // Отправляем код 200 OK
                } else {
                    // Иначе, это запрос на создание нового Epic
                    taskManager.addEpic(newEpic);
                    sendJsonResponse(exchange, gson.toJson(newEpic), 201); // Отправляем код 201 Created
                }
            }else if ("POST".equals(exchange.getRequestMethod())) {
                // Обработка POST-запроса для создания Epic
                // Распарсите JSON-тело и передайте его в метод создания Epic
                // Отправьте JSON-ответ с созданным Epic и статусом 201 Created
                Epic newEpic = parseEpicFromBody(exchange);
                taskManager.addEpic(newEpic);
                sendJsonResponse(exchange, gson.toJson(newEpic), 201);

            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                // Обработка DELETE-запроса для удаления Epic
                // И отправка статуса 204 No Content
                int epicId = extractEpicIdFromRequest(exchange);
                taskManager.removeEpicById(epicId);
                sendEmptyResponse(exchange, 204);
            }
        } finally {
            exchange.close();
        }
    }

    private int extractEpicIdFromRequest(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        String[] queryParams = query.split("=");
        if (queryParams.length == 2 && "id".equals(queryParams[0])) {
            try {
                return Integer.parseInt(queryParams[1]);
            } catch (NumberFormatException e) {
                throw new IOException("Invalid Epic ID", e);
            }
        }
        throw new IOException("Epic ID not found");
    }

    private Epic parseEpicFromBody(HttpExchange exchange) throws IOException {
        // Распарсивание Epic из JSON-тела запроса
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            return gson.fromJson(requestBody, Epic.class);
        } catch (JsonSyntaxException e) {
            sendErrorResponse(exchange, 400, "Invalid JSON format");
            throw new IOException("Invalid JSON format", e); // Выбрасываем исключение
        }
    }

    private void sendJsonResponse(HttpExchange exchange, String response) throws IOException {
        sendJsonResponse(exchange, response, 200);
    }

    private void sendJsonResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
            os.close();;
        }
    }

    private void sendEmptyResponse(HttpExchange exchange, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, -1);
        exchange.close();
    }

    private void sendErrorResponse(HttpExchange exchange, int statusCode, String errorMessage) throws IOException {
        String response = "{\"error\": \"" + errorMessage + "\"}";
        sendJsonResponse(exchange, response, statusCode);
    }
}

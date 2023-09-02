package manager.server.handler;

import com.google.gson.Gson;
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
        if ("GET".equals(exchange.getRequestMethod())) {
            // Обработка GET-запроса для Epic
            // Получение Epic по id и отправка JSON-ответа
            int epicId = extractEpicIdFromRequest(exchange);
            Epic epic = taskManager.getEpicById(epicId);

            if (epic != null) {
                String response = gson.toJson(epic);
                sendJsonResponse(exchange, response);
            } else {
                sendErrorResponse(exchange, 404, "Epic not found");
            }

        } else if ("POST".equals(exchange.getRequestMethod())) {
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
    }

    private int extractEpicIdFromRequest(HttpExchange exchange) {
        // Извлечение идентификатора Epic из запроса
        String query = exchange.getRequestURI().getQuery();
        String[] queryParams = query.split("=");
        if (queryParams.length == 2 && "id".equals(queryParams[0])) {
            return Integer.parseInt(queryParams[1]);
        }
        return -1;
    }

    private Epic parseEpicFromBody(HttpExchange exchange) throws IOException {
        // Распарсивание Epic из JSON-тела запроса
        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        return gson.fromJson(requestBody, Epic.class);
    }

    private void sendJsonResponse(HttpExchange exchange, String response) throws IOException {
        sendJsonResponse(exchange, response, 200);
    }

    private void sendJsonResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
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

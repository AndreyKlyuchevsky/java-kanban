package manager.server.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import model.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class HistoryHandler implements HttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            // Получить историю задач из менеджера
            List<Task> history = taskManager.getHistory();

            // Преобразовать историю в формат JSON
            String jsonResponse = gson.toJson(history);

            // Отправить JSON-ответ клиенту
            exchange.sendResponseHeaders(200, jsonResponse.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(jsonResponse.getBytes());
            }
        } else {
            // Если метод запроса не GET, вернуть ошибку метода не разрешен
            exchange.sendResponseHeaders(405, -1); // 405 Method Not Allowed
        }
    }
}

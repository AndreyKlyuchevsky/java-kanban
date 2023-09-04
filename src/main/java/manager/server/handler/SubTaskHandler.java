package manager.server.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.file.FileBackedTasksManager;
import model.SubTask;
import com.google.gson.Gson;

import java.io.*;
import java.util.List;

public class SubTaskHandler implements HttpHandler {
    private final FileBackedTasksManager manager;
    private final Gson gson;

    public SubTaskHandler(FileBackedTasksManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        int id = extractIdFromPath(path);
        try {
            if (method.equals("GET")) {
                if (id == -1) {
                    // Запрос на получение всех SubTask
                    List<SubTask> subTasks = manager.getSubTaskAll();
                    sendResponse(exchange, gson.toJson(subTasks), 200);
                } else {
                    // Запрос на получение конкретного SubTask по ID
                    SubTask subTask = manager.getSubtaskById(id);
                    if (subTask != null) {
                        sendResponse(exchange, gson.toJson(subTask), 200);
                    } else {
                        sendResponse(exchange, "SubTask not found", 404);
                    }
                }
            } else if (method.equals("POST")) {
                // Запрос на создание нового SubTask
                if (id == -1) {
                    InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    String json = br.readLine();

                    SubTask newSubTask = gson.fromJson(json, SubTask.class);
                    manager.addSubTask(newSubTask);
                    sendResponse(exchange, "SubTask created", 201);
                } else {
                    sendResponse(exchange, "Invalid request", 400);
                }
            } else if (method.equals("DELETE")) {
                // Запрос на удаление SubTask по ID
                if (id != -1) {
                    manager.removeSubTaskById(id);
                    sendResponse(exchange, "SubTask deleted", 200);
                } else {
                    sendResponse(exchange, "Invalid request", 400);
                }
            } else {
                sendResponse(exchange, "Method not allowed", 405);
            }
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

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}

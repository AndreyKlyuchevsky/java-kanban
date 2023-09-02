package manager.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.file.FileBackedTasksManager;
import model.Task;
import com.google.gson.Gson;
import java.io.*;
import java.util.List;

public class TaskHandler implements HttpHandler {
    private final FileBackedTasksManager manager;
    private final Gson gson;

    public TaskHandler(FileBackedTasksManager manager, Gson gson) {
        this.manager = manager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        int id = extractIdFromPath(path);

        if (method.equals("GET")) {
            if (id == -1) {
                // Запрос на получение всех Task
                List<Task> tasks = manager.getTaskAll();
                sendResponse(exchange, gson.toJson(tasks), 200);
            } else {
                // Запрос на получение конкретного Task по ID
                Task task = manager.getTaskById(id);
                if (task != null) {
                    sendResponse(exchange, gson.toJson(task), 200);
                } else {
                    sendResponse(exchange, "Task not found", 404);
                }
            }
        } else if (method.equals("POST")) {
            // Запрос на создание нового Task
            if (id == -1) {
                InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String json = br.readLine();

                Task newTask = gson.fromJson(json, Task.class);
                manager.addTask(newTask);
                sendResponse(exchange, "Task created", 201);
            } else {
                sendResponse(exchange, "Invalid request", 400);
            }
        } else if (method.equals("DELETE")) {
            // Запрос на удаление Task по ID
            if (id != -1) {
                manager.removeTaskById(id);
                sendResponse(exchange, "Task deleted", 200);
            } else {
                sendResponse(exchange, "Invalid request", 400);
            }
        } else {
            sendResponse(exchange, "Method not allowed", 405);
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
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}

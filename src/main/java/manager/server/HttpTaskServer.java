package manager.server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import manager.file.FileBackedTasksManager;
import com.google.gson.Gson;
import manager.http.HttpTaskManager;
import manager.server.handler.EpicHandler;
import manager.server.handler.HistoryHandler;
import manager.server.handler.SubTaskHandler;
import manager.server.handler.TaskHandler;


import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final String DEFAULT_FILE_PATH= "filewriter.csv";
    private final HttpServer server;
    private final Gson gson;

    public HttpTaskServer() throws IOException {
        this(new FileBackedTasksManager(DEFAULT_FILE_PATH));
    }

    public HttpTaskServer(TaskManager manager) throws IOException {
        // Создаем HTTP-сервер и привязываем его к порту 8080
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        gson= new Gson();
        // Добавляем обработчики для различных типов задач
        HttpHandler taskHandler = new TaskHandler(manager, gson);
        HttpHandler subTaskHandler = new SubTaskHandler(manager, gson);
        HttpHandler epicHandler = new EpicHandler(manager, gson);
        HttpHandler historyHandler = new HistoryHandler(manager, gson);


        // Маппим пути к обработчикам
        server.createContext("/tasks/task", taskHandler);
        server.createContext("/tasks/subtask", subTaskHandler);
        server.createContext("/tasks/epic", epicHandler);
        server.createContext("/tasks/history", historyHandler);
    }

    public void start() {
        // Запускаем сервер
        server.start();
        System.out.println("Server started on port " + PORT);
    }

    public void stop() {
        // Останавливаем сервер
        server.stop(0);
        System.out.println("Server stopped");
    }
}
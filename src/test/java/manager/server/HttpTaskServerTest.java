package manager.server;

import manager.mem.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import manager.file.FileBackedTasksManager;
import com.google.gson.Gson;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    private HttpTaskServer httpTaskServer;

    @BeforeEach
    void setUp() throws IOException {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        Gson gson = new Gson();
        httpTaskServer = new HttpTaskServer((FileBackedTasksManager) manager, gson);
        httpTaskServer.start();
    }

    @AfterEach
    void tearDown() {
        // Остановка HttpTaskServer после каждого теста
        httpTaskServer.stop();
    }

    @Test
    void testTaskEndpoint() throws IOException, InterruptedException {
        // Тест эндпоинта "/tasks/task"

        // Подготовка данных для запроса
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task?id=1"))
                .GET()
                .build();

        // Выполнение запроса
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверка статуса ответа
        assertEquals(200, response.statusCode());
    }

    @Test
    void testSubTaskEndpoint() throws IOException, InterruptedException {
        // Тест эндпоинта "/tasks/subtask"

        // Подготовка данных для запроса
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask?id=1"))
                .GET()
                .build();

        // Выполнение запроса
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверка статуса ответа
        assertEquals(200, response.statusCode());
    }

    @Test
    void testEpicEndpoint() throws IOException, InterruptedException {
        // Тест эндпоинта "/tasks/epic"

        // Подготовка данных для запроса
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic?id=1"))
                .GET()
                .build();

        // Выполнение запроса
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверка статуса ответа
        assertEquals(200, response.statusCode());
    }

    @Test
    void testHistoryEndpoint() throws IOException, InterruptedException {
        // Тест эндпоинта "/tasks/history"

        // Подготовка данных для запроса
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/history?id=1"))
                .GET()
                .build();

        // Выполнение запроса
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Проверка статуса ответа
        assertEquals(200, response.statusCode());
    }

}

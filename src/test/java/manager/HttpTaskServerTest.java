package manager;

import com.google.gson.Gson;
import manager.file.FileBackedTasksManager;
import manager.server.HttpTaskServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {

    private HttpTaskServer httpTaskServer;

    @BeforeEach
    void setUp() throws IOException {
        // Инициализация HttpTaskServer перед каждым тестом
        FileBackedTasksManager manager = new FileBackedTasksManager("test_data.json");
        Gson gson = new Gson();
        httpTaskServer = new HttpTaskServer(manager, gson);
        httpTaskServer.start();
    }


    @AfterEach
    void tearDown() {
        // Остановка HttpTaskServer после каждого теста
        httpTaskServer.stop();
    }

    @Test
    void testTaskEndpoint() throws IOException {
        // Тест эндпоинта "/tasks/task"
        // Подготовка данных для запроса
        URL url = new URL("http://localhost:8080/tasks/task?id=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Выполнение запроса и проверка статуса ответа
        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        // Закрытие соединения
        connection.disconnect();
    }

    @Test
    void testSubTaskEndpoint() throws IOException {
        // Тест эндпоинта "/tasks/subtask"

        // Подготовка данных для запроса
        URL url = new URL("http://localhost:8080/tasks/subtask?id=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Выполнение запроса и проверка статуса ответа
        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        // Закрытие соединения
        connection.disconnect();
    }

    @Test
    void testEpicEndpoint() throws IOException {
        // Тест эндпоинта "/tasks/epic"
        // Подготовка данных для запроса
        URL url = new URL("http://localhost:8080/tasks/epic?id=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Выполнение запроса и проверка статуса ответа
        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        // Закрытие соединения
        connection.disconnect();
    }

    @Test
    void testHistoryEndpoint() throws IOException {
        // Тест эндпоинта "/tasks/history"
        // Подготовка данных для запроса
        URL url = new URL("http://localhost:8080/tasks/history?id=1");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Выполнение запроса и проверка статуса ответа
        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        // Закрытие соединения
        connection.disconnect();
    }

}

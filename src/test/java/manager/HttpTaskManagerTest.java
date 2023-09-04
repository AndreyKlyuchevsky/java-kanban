package manager;

import manager.http.HttpTaskManager;
import manager.server.KVServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HttpTaskManagerTest {
    private static HttpTaskManager httpTaskManager;

    @BeforeAll
    public static void setUp() throws IOException {
        // Настройка HttpTaskManager перед началом тестов
        KVServer server = new KVServer();
        server.start();
        httpTaskManager = new HttpTaskManager("http://localhost:8078");
    }

    @Test
    public void testPutAndLoad() {
        // Тест для добавления и получения задачи через HTTP-сервер
        String key = "test_task_key";
        String json = "{\"id\":\"test_task_key\",\"title\":\"Test Task\",\"description\":\"Test Description\",\"status\":\"NEW\",\"estimate\":5}";

        // Добавление задачи
        httpTaskManager.put(key, json);

        // Загрузка задачи
        String loadedJson = httpTaskManager.load(key);
        assertEquals(json, loadedJson);
    }

    @Test
    public void testLoadNonExistent() {
        // Тест для попытки загрузки несуществующей задачи
        String key = "non_existent_key";

        // Попытка загрузки несуществующей задачи
        String loadedJson = httpTaskManager.load(key);
        assertNull(loadedJson);
    }
}

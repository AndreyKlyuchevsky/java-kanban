package manager;


import manager.client.TaskClient;
import manager.http.HttpTaskManager;
import model.StatusTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class HttpTaskManagerTest {

    private HttpTaskManager httpTaskManager;
    private TaskClient fakeTaskClient;

    @BeforeEach
    void setUp() {
        fakeTaskClient = new FakeTaskClient();
        httpTaskManager = new HttpTaskManager(fakeTaskClient);
    }

    @Test
    void testSave() {
        // Тест для метода save

        // Делаем какие-то изменения в менеджере
        httpTaskManager.addTask(new Task("Test addNewTask", "Test addNewTask description", StatusTask.NEW, 40, LocalDateTime.of(2023, 9, 8, 00, 00, 00)));
        // Сохраняем состояние менеджера
        httpTaskManager.save();
        // Проверяем, что сохранение вызвало метод put нашего фейкового клиента
        assertEquals("{\"id\":\"fake_task_id\",\"title\":\"Fake Task\",\"description\":\"Fake Description\",\"status\":\"NEW\",\"estimate\":5}", fakeTaskClient.load("task_manager_state"));
    }
}

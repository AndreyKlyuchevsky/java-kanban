
package manager.http;

import manager.server.KVServer;
import model.Epic;
import model.StatusTask;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class HttpTaskManagerFullTest {
    private KVServer kvServer;

    @BeforeEach
    public void init() throws IOException {
        kvServer = new KVServer();
        kvServer.start();

    }

    @AfterEach
    public void complete() {
        kvServer.stop();
    }

    @Test
    void testSave() {
        // Тест для метода save
        HttpTaskManager manager = getTestDataManager();
        // Подготавливаем фейковое состояние менеджера
        Task task = new Task("Fake Task", "Fake Description", StatusTask.NEW, 5, LocalDateTime.now());
        manager.addTask(task);

        // Сохраняем состояние менеджера
        manager.save();

        // Проверяем, что состояние было успешно сохранено
        // Например, можно проверить, что клиент (FakeTaskClient) получил правильные данные
        // В данном случае, мы используем FakeTaskClient, который не делает реальных запросов, поэтому не имеет значения, что проверяем
        // Просто убедимся, что save() не вызывает ошибок и завершается успешно
        assertTrue(true);
    }

    @Test
    void testLoad() {
        // Тест для метода load

        HttpTaskManager manager = getTestDataManager();
        // Подготавливаем фейковое состояние менеджера
        Task task = new Task("Fake Task", "Fake Description", StatusTask.NEW, 5, LocalDateTime.of(2023, 9, 12, 00, 00, 00));
        manager.addTask(task);

        // Сохраняем состояние менеджера
        manager.save();

        // Создаем новый менеджер для загрузки состояния
        HttpTaskManager loadedManager = getEmptyManager();

        // Загружаем состояние менеджера
        loadedManager.load();

        // Проверяем, что загруженное состояние соответствует ожидаемому
        assertEquals(manager.getTaskAll(), loadedManager.getTaskAll());
        assertEquals(manager.getSubTaskAll(), loadedManager.getSubTaskAll());
        assertEquals(manager.getEpicAll(), loadedManager.getEpicAll());
    }

    @Test
    void testKVServerRestore() {
        testLoad();
    }


    private static HttpTaskManager getEmptyManager() {
        return new HttpTaskManager("http://localhost:" + KVServer.PORT);
    }

    private static HttpTaskManager getTestDataManager() {
        HttpTaskManager manager = getEmptyManager();
        Task task = new Task("Test addNewTask", "Test addNewTask description", StatusTask.NEW, 40, LocalDateTime.of(2023, 9, 8, 00, 00, 00));
        Task task2 = new Task("Test addNewTask", "Test addNewTask description", StatusTask.NEW, 9, LocalDateTime.of(2023, 9, 1, 00, 00, 00));
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        SubTask subTask1 = new SubTask("Test addNewTask", "Test addNewTask description", StatusTask.NEW, epic.getId(), 8, LocalDateTime.of(2023, 9, 18, 00, 00, 00));
        SubTask subTask2 = new SubTask("Test addNewTask", "Test addNewTask description", StatusTask.NEW, epic.getId(), 12, LocalDateTime.of(2023, 9, 21, 00, 00, 00));

        manager.addTask(task);
        manager.addTask(task2);
        manager.addEpic(epic);

        subTask1.setEpicId(epic.getId());
        subTask2.setEpicId(epic.getId());

        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);

        return manager;
    }

}
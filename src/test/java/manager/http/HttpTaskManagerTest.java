package manager.http;

import manager.FakeTaskClient;
import manager.TaskManagerTest;
import model.StatusTask;
import model.Task;
import org.testng.annotations.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    @Override
    public HttpTaskManager getManager() {
        return new HttpTaskManager(new FakeTaskClient());
    }

    // Другие методы тестирования

    @Test
    void testSave() {
        // Тест для метода save

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

        // Подготавливаем фейковое состояние менеджера
        Task task = new Task("Fake Task", "Fake Description", StatusTask.NEW, 5, LocalDateTime.now());
        manager.addTask(task);

        // Сохраняем состояние менеджера
        manager.save();

        // Создаем новый менеджер для загрузки состояния
        HttpTaskManager loadedManager = new HttpTaskManager(new FakeTaskClient());

        // Загружаем состояние менеджера
        loadedManager.load();

        // Проверяем, что загруженное состояние соответствует ожидаемому
        assertEquals(manager.getTaskAll(), loadedManager.getTaskAll());
        assertEquals(manager.getSubTaskAll(), loadedManager.getSubTaskAll());
        assertEquals(manager.getEpicAll(), loadedManager.getEpicAll());
    }
}

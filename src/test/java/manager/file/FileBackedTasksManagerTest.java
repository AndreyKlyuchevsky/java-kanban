package manager.file;

import manager.TaskManagerTest;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    @BeforeEach // ревьюрер предлагал создать метод BeforeEach
    protected void init() {
        manager = new FileBackedTasksManager(new File("filewriter.csv"));
        super.init();
    }

    @Test
    @DisplayName("Сохрагяем и загружаем")
    void testSaveAndLoad() throws IOException {

        // Создаем менеджера и добавляем задачи
        manager.addEpic(epic);
        manager.addTask(task);
        subTask1.setEpicId(epic.getId());
        subTask2.setEpicId(epic.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        manager.getTaskById(task.getId());
        manager.getSubtaskById(subTask1.getId());
        // Сохраняем менеджера в файл
        manager.save();
        // Сохраняем историю просмотра
        List<Task> historyTask = manager.getHistory();
        // Загружаем менеджера из файла
        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile(new File("filewriter.csv"));
        // Проверяем, что загруженные задачи совпадают с добавленными
        assertEquals(epic, loadedManager.getEpicById(epic.getId()));
        assertEquals(task, loadedManager.getTaskById(task.getId()));
        assertEquals(subTask1, loadedManager.getSubtaskById(subTask1.getId()));
        assertEquals(subTask2, loadedManager.getSubtaskById(subTask2.getId()));

        List<Task> historyTaskLoaded = manager.getHistory();
        // Проверяем, что история загружена верно
        assertEquals(historyTask, historyTaskLoaded);
    }

}
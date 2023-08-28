package manager.file;

import manager.TaskManagerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;


class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    @BeforeEach // ревьюрер предлагал создать метод BeforeEach
    protected void init(){
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
        // Сохраняем менеджера в файл
        manager.save();

        // Загружаем менеджера из файла
        FileBackedTasksManager loadedManager = FileBackedTasksManager.loadFromFile(new File("filewriter.csv"));
        // Проверяем, что загруженные задачи совпадают с добавленными
        assertEquals(epic, loadedManager.getEpicById(epic.getId()));
        assertEquals(task, loadedManager.getTaskById(task.getId()));
        assertEquals(subTask1, loadedManager.getSubtaskById(subTask1.getId()));
        assertEquals(subTask2, loadedManager.getSubtaskById(subTask2.getId()));

    }

}
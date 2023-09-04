package manager;


import manager.http.HttpTaskManager;
import model.Epic;
import model.StatusTask;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerTest {

    private static HttpTaskManager httpTaskManager;

    @BeforeAll
    public static void setUp() throws IOException {
        // Создайте HttpTaskManager с URL сервера KVServer
        httpTaskManager = new HttpTaskManager("http://localhost:8078");
    }

    @Test
    public void testAddEpic() {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        httpTaskManager.addEpic(epic);

        Epic retrievedEpic = httpTaskManager.getEpicById(epic.getId());
        assertNotNull(retrievedEpic);
        assertEquals("Test Epic", retrievedEpic.getName());
        assertEquals("Description for test epic", retrievedEpic.getDescription());
    }

    @Test
    public void testAddSubTask() {
        Task parentTask = new Task("Test addNewTask", "Test addNewTask description", StatusTask.NEW, 40, LocalDateTime.of(2023, 9, 8, 00, 00, 00));
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");

        httpTaskManager.addTask(parentTask);
        httpTaskManager.addEpic(epic);

        SubTask subTask = new SubTask("Test addNewTask", "Test addNewTask description", StatusTask.NEW, epic.getId(), 8, LocalDateTime.of(2023, 9, 18, 00, 00, 00));
        subTask.setName("SubTask");
        subTask.setDescription("Description for subtask");
        subTask.setEpicId(parentTask.getId());

        httpTaskManager.addSubTask(subTask);

        SubTask retrievedSubTask = httpTaskManager.getSubtaskById(subTask.getId());
        assertNotNull(retrievedSubTask);
        assertEquals("SubTask", retrievedSubTask.getName());
        assertEquals("Description for subtask", retrievedSubTask.getDescription());
    }

    @Test
    public void testGetEpicById() {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        httpTaskManager.addEpic(epic);

        Epic retrievedEpic = httpTaskManager.getEpicById(epic.getId());
        assertNotNull(retrievedEpic);
        assertEquals("Test Epic", retrievedEpic.getName());
        assertEquals("Description for test epic", retrievedEpic.getDescription());
    }

    @Test
    public void testUpdateEpic() {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        httpTaskManager.addEpic(epic);

        epic.setName("Updated Epic");
        httpTaskManager.updateEpic(epic);

        Epic updatedEpic = httpTaskManager.getEpicById(epic.getId());
        assertNotNull(updatedEpic);
        assertEquals("Updated Epic", updatedEpic.getName());
        assertEquals("Description for test epic", updatedEpic.getDescription());
    }

    @Test
    public void testRemoveEpicById() {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        httpTaskManager.addEpic(epic);

        httpTaskManager.removeEpicById(epic.getId());

        Epic removedEpic = httpTaskManager.getEpicById(epic.getId());
        assertNull(removedEpic);
    }


}

package manager;

import model.Epic;
import model.StatusTask;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class TaskManagerTest<T extends TaskManager> {
    private Task task;
    private Epic epic;
    private SubTask subTask1;
    private SubTask subTask2;
    protected T manager;

    @BeforeEach // ревьюрер предлагал создать метод BeforeEach
    private void init() {
        task = new Task("Test addNewTask", "Test addNewTask description", StatusTask.NEW, 40,LocalDateTime.of(2023, 9, 8, 00, 00, 00));
        epic = new Epic("Test addNewTask", "Test addNewTask description");
        subTask1 = new SubTask("Test addNewTask", "Test addNewTask description", StatusTask.NEW, epic.getId(),8,LocalDateTime.of(2023, 16, 8, 00, 00, 00));
        subTask2 = new SubTask("Test addNewTask", "Test addNewTask description", StatusTask.NEW, epic.getId(),12,LocalDateTime.of(2023, 25, 8, 00, 00, 00));

    }

    @Test
    @DisplayName("добавляем Task задачу")
    public void addTaskTest() throws IOException {
        init();
        manager.addTask(task);
        final int taskId = task.getId();
        final Task savedTask = manager.getTaskById(taskId);
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        final List<Task> taskList = manager.getTaskAll();
        assertNotNull(taskList, "Задачи не возвращаются.");
        assertEquals(1, taskList.size(), "Неверное количество задач.");
        for (Task tasks : taskList) {
            if (tasks.getId() == 1) {
                assertEquals(task, tasks, "Задачи не совпадают.");
                break;
            }
        }
    }

    @Test
    @DisplayName("удаляем Task задачу")
    public void removeTaskTest() throws IOException {
        init();
        manager.addTask(task);
        final int taskId = task.getId();
        List<Task> tasks = manager.getTaskAll();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        manager.removeTaskById(taskId);
        tasks = manager.getTaskAll();
        assertEquals(0, tasks.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("удаляем Task задачу не существующую")
    public void removeNullTaskTest() throws IOException {
        init();
        try {
            manager.removeTaskById(8);
        } catch (NullPointerException exception) {
            System.out.println(exception);
        }
    }

    @Test
    @DisplayName("удаляем Task задачу не существующую")
    public void removeNullEpicTest() throws IOException {
        init();
        try {
            manager.removeEpicById(100);
        } catch (NullPointerException exception) {
            System.out.println(exception);
        }

    }

    @Test
    @DisplayName("добавляем Epic задачу")
    public void addEpicTest() throws IOException {
        init();
        manager.addEpic(epic);
        final int epicId = epic.getId();
        final Epic savedEpic = manager.getEpicById(epicId);
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        final List<Epic> epicList = manager.getEpicAll();
        assertNotNull(epicList, "Задачи на возвращаются.");
        assertEquals(1, epicList.size(), "Неверное количество задач.");
        for (Epic epics : epicList) {
            if (epics.getId() == 1) {
                assertEquals(epic, epics, "Задачи не совпадают.");
            }
        }
    }

    @Test
    @DisplayName("удаляем Epic задачу")
    public void removeEpicTest() throws IOException {
        init();
        manager.addEpic(epic);
        final int epicId = epic.getId();
        List<Epic> epics = manager.getEpicAll();
        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        manager.removeEpicById(epicId);
        epics = manager.getEpicAll();
        assertEquals(0, epics.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("добавляем SubTask задачу")
    public void addSubTaskTest() throws IOException {
        init();
        manager.addEpic(epic);
        //проверяем список подзадач у epic. Должен быть 0
        final List<SubTask> subTaskNull = manager.getSubTaskAll();
        int countSibTask = 0;
        for (SubTask item : subTaskNull) {
            if (item.getEpicId() == epic.getId()) {
                countSibTask++;
            }
        }
        assertEquals(0, countSibTask, "У epic есть подзадачи!");
        //проверяем статус epic - должен быть NEW
        assertEquals(StatusTask.NEW, manager.getEpicById(epic.getId()).getStatus(), "Не верный статус");
        //добавляем подзадачу
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        final int subTaskId = subTask1.getId();
        final SubTask saveSubTask = manager.getSubtaskById(subTaskId);
        //проверяем добавление задачи
        assertNotNull(saveSubTask, "Задача не найдена.");
        assertEquals(subTask1, saveSubTask, "Задачи не совпадают.");
        final List<SubTask> subsTaskList = manager.getSubTaskAll();
        assertNotNull(subsTaskList, "Задачи не возвращаются.");
        assertEquals(2, subsTaskList.size(), "Неверное количество задач.");
        for (SubTask subTask : subsTaskList) {
            if (subTask.getId() == subTaskId) {
                assertEquals(subTask1, subTask, "Задачи не совпадают.");
            }
        }
        //проверяем статус epic - должен быть NEW
        assertEquals(StatusTask.NEW, manager.getEpicById(epic.getId()).getStatus(), "Не верный статус");
        //меняем статус у subTask1 -IN_PROGRESS
        subTask1.setStatus(StatusTask.IN_PROGRESS);
        manager.updateSubTask(subTask1);
        assertEquals(StatusTask.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus(), "Не верный статус");
        //меняем статус у subTask1 DONE
        subTask1.setStatus(StatusTask.DONE);
        manager.updateSubTask(subTask1);
        assertEquals(StatusTask.DONE, subTask1.getStatus(), "Не верный статус");
        //проверяем статус epic - должен быть DONE
        assertEquals(StatusTask.IN_PROGRESS, manager.getEpicById(epic.getId()).getStatus(), "Не верный статус");
        //меняем статус у subTask2 DONE
        subTask2.setStatus(StatusTask.DONE);
        manager.updateSubTask(subTask2);
        assertEquals(StatusTask.DONE, subTask2.getStatus(), "Не верный статус");
        //проверяем статус epic - должен быть DONE
        assertEquals(StatusTask.DONE, manager.getEpicById(epic.getId()).getStatus(), "Не верный статус");
    }
}
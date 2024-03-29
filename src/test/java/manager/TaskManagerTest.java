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

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected Task task;
    protected Task task4;
    protected Epic epic;
    protected SubTask subTask1;
    protected SubTask subTask2;
    protected T manager;


    abstract public T getManager();

    @BeforeEach
    protected void init() throws IOException {
        manager = getManager();
        task = new Task("Test addNewTask", "Test addNewTask description", StatusTask.NEW, 40, LocalDateTime.of(2023, 9, 8, 00, 00, 00));
        task4 = new Task("Test addNewTask", "Test addNewTask description", StatusTask.NEW, 9, LocalDateTime.of(2023, 9, 1, 00, 00, 00));
        epic = new Epic("Test addNewTask", "Test addNewTask description");
        subTask1 = new SubTask("Test addNewTask", "Test addNewTask description", StatusTask.NEW, epic.getId(), 8, LocalDateTime.of(2023, 9, 18, 00, 00, 00));
        subTask2 = new SubTask("Test addNewTask", "Test addNewTask description", StatusTask.NEW, epic.getId(), 12, LocalDateTime.of(2023, 9, 21, 00, 00, 00));

        manager.addTask(task);
        manager.addTask(task4);
        manager.addEpic(epic);

        subTask1.setEpicId(epic.getId());
        subTask2.setEpicId(epic.getId());

        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);

    }

    @Test
    @DisplayName("добавляем Task задачу")
    public void addTaskTest() {
        final int taskId = task.getId();
        final Task savedTask = manager.getTaskById(taskId);
        final List<Task> taskList = manager.getTaskAll();

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertEquals(2, taskList.size(), "Неверное количество задач.");

        for (Task tasks : taskList) {
            if (tasks.getId() == 1) {
                assertEquals(task, tasks, "Задачи не совпадают.");
                break;
            }
        }
    }

    @Test
    @DisplayName("вернуть Task задачу")
    public void getTaskByIdTest() {
        manager.addTask(task);
        final List<Task> taskList = manager.getTaskAll();

        assertEquals(3, taskList.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("вернуть Task задачу - Null")
    public void getTaskByIdTestNull() {
        manager.addTask(task);
        final Task savedTask = manager.getTaskById(0);
        assertNull(savedTask, "Задача не найдена.");
    }

    @Test
    @DisplayName("вернуть Task задачу")
    public void getTaskAllTest() {
        manager.addTask(task);
        final int taskId = task.getId();
        final Task savedTask = manager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }


    @Test
    @DisplayName("обновляем Task задачу")
    public void getUpdateTaskTest() {
        manager.addTask(task);
        final int taskId = task.getId();
        Task taskNew = new Task("Test addNewTask", "Test addNewTask description", taskId, StatusTask.DONE, 40, LocalDateTime.of(2023, 9, 8, 00, 00, 00));

        manager.updateTask(taskNew);

        Task savedTaskNew = manager.getTaskById(taskId);
        assertEquals(taskNew, savedTaskNew, "Задачи не совпадают.");
    }


    @Test
    @DisplayName("удаляем Task задачу")
    public void removeTaskTest() {
        final int taskId = task.getId();

        manager.removeTaskById(taskId);
        List<Task> tasks = manager.getTaskAll();

        assertEquals(1, tasks.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("удаляем Task задачу не существующую")
    public void removeNullTaskTest() {
        try {
            manager.removeTaskById(8);
        } catch (NullPointerException exception) {
            System.out.println(exception);
        }
    }

    @Test
    @DisplayName("удаляем Task задачу не существующую")
    public void removeNullEpicTest() {
        try {
            manager.removeEpicById(100);
        } catch (NullPointerException exception) {
            System.out.println(exception);
        }

    }

    @Test
    @DisplayName("добавляем Epic задачу")
    public void addEpicTest() {
        manager.addEpic(epic);
        final int epicId = epic.getId();
        final Epic savedEpic = manager.getEpicById(epicId);
        final List<Epic> epicList = manager.getEpicAll();

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertEquals(2, epicList.size(), "Неверное количество задач.");

        for (Epic epics : epicList) {
            if (epics.getId() == 1) {
                assertEquals(epic, epics, "Задачи не совпадают.");
            }
        }
    }

    @Test
    @DisplayName("вернуть Epic задачу")
    public void getEpicByIdTest() {
        manager.addEpic(epic);
        final int epicId = epic.getId();
        final Epic savedEpic = manager.getEpicById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
    }

    @Test
    @DisplayName("вернуть Epic задачу - Null")
    public void getEpicByIdTestNull() {
        manager.addEpic(epic);
        final Epic savedEpic = manager.getEpicById(0);
        assertNull(savedEpic, "Задача не найдена.");
    }

    @Test
    @DisplayName("вернуть Epic")
    public void getEpicAllTest() {
        final List<Epic> epicList = manager.getEpicAll();

        assertEquals(1, epicList.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("обновляем Epic задачу")
    public void getUpdateEpicTest() {
        final int epicId = epic.getId();
        final Epic savedEpic = manager.getEpicById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        Epic epicNew = new Epic("Test addNewTask new", "Test addNewTask description", epicId);
        manager.updateEpic(epicNew);

        assertEquals(epicNew.getName(), savedEpic.getName(), "Задачи не совпадают.");
        assertEquals(epicNew.getDescription(), savedEpic.getDescription(), "Задачи не совпадают.");
    }

    @Test
    @DisplayName("удаляем Epic задачу")
    public void removeEpicByIdTest() {
        final int epicId = epic.getId();
        List<Epic> epics = manager.getEpicAll();

        assertEquals(1, epics.size(), "Неверное количество задач.");

        manager.removeEpicById(epicId);
        epics = manager.getEpicAll();

        assertEquals(0, epics.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("добавляем SubTask задачу")
    public void addSubTaskTest() {
        final int SubTaskId = subTask1.getId();
        final SubTask savedSubTask = manager.getSubtaskById(SubTaskId);
        final List<SubTask> SubTaskList = manager.getSubTaskAll();

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask1, savedSubTask, "Задачи не совпадают.");
        assertEquals(2, SubTaskList.size(), "Неверное количество задач.");

        for (Task tasks : SubTaskList) {
            if (tasks.getId() == 1) {
                assertEquals(task, tasks, "Задачи не совпадают.");
                break;
            }
        }
    }

    @Test
    @DisplayName("вернуть SubTask задачу")
    public void getSubtaskByIdTest() {
        manager.addEpic(epic);
        subTask1.setEpicId(epic.getId());
        manager.addSubTask(subTask1);
        final int subtaskId = subTask1.getId();
        final SubTask savedSubTask = manager.getSubtaskById(subtaskId);

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask1, savedSubTask, "Задачи не совпадают.");
    }

    @Test
    @DisplayName("вернуть SubtaskTask задачу - Null")
    public void getSubTaskByIdTestNull() {
        manager.addSubTask(subTask1);
        final SubTask savedSubTask = manager.getSubtaskById(0);
        assertNull(savedSubTask, "Задача не найдена.");
    }

    @Test
    @DisplayName("вернуть SubTask конкретного Epic")
    public void getSubtaskEpicTest() {
        final List<SubTask> subsTaskList = manager.getSubTaskEpic(epic);

        assertEquals(2, subsTaskList.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("вернуть SubTask")
    public void getSubtaskAllTest() {
        List<SubTask> subTaskList = manager.getSubTaskAll();

        assertEquals(2, subTaskList.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("обновляем SubTask")
    public void     updateSubTaskTest() {
        final int subtaskId = subTask1.getId();
        SubTask subTask3 = new SubTask("Test addNewTask", "Test addNewTask description", StatusTask.DONE, subtaskId, epic.getId(), 12, LocalDateTime.of(2023, 12, 9, 00, 00, 00));
        manager.updateSubTask(subTask3);
        final SubTask savedSubTask = manager.getSubtaskById(subtaskId);

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask3, savedSubTask, "Задачи не совпадают.");
    }

    @Test
    @DisplayName("удаляем SubTask")
    public void removeSubTaskByIdTest() {
        List<SubTask> subTaskList = manager.getSubTaskAll();

        manager.removeSubTaskById(subTask1.getId());
        subTaskList = manager.getSubTaskAll();

        assertEquals(1, subTaskList.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("обновляем статус")
    public void updateStatusTest() {
        manager.addEpic(epic);

        //проверяем статус epic - должен быть NEW
        assertEquals(StatusTask.NEW, manager.getEpicById(epic.getId()).getStatus(), "Не верный статус");

        //добавляем подзадачу
        subTask1.setEpicId(epic.getId());
        subTask2.setEpicId(epic.getId());
        manager.addSubTask(subTask1);
        manager.addSubTask(subTask2);
        final int subTaskId = subTask1.getId();
        final SubTask saveSubTask = manager.getSubtaskById(subTaskId);

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

    @Test
    @DisplayName("тест на пересечение времени")
    void ValidateTaskDurationIntervalTest() {

        LocalDateTime startTime1 = LocalDateTime.of(2023, 8, 1, 10, 0);
        LocalDateTime startTime2 = LocalDateTime.of(2023, 8, 1, 11, 0);
        int duration = 24;
        Task task1 = new Task("Task 1", "Description 1", StatusTask.NEW, duration, startTime1);
        manager.addTask(task1);
        //тест на вложенность времени. Когда временной интервал 3 задачи полностью поподает в первый
        assertThrows(IllegalArgumentException.class, () -> {
            Task task2 = new Task("Task 2", "Description 2", StatusTask.NEW, duration, startTime1);
            manager.addTask(task2);
        });

        //тест на вложенность времени. Когда временной интервал 3 задачи полностью поподает в первый
        Task task3 = new Task("Task 3", "Description 3", StatusTask.NEW, 5, startTime2);

        assertThrows(IllegalArgumentException.class, () -> {
            manager.addTask(task3);
        });
    }

    @Test
    @DisplayName("удаляем все SubTask")
    public void removeSubTaskAll() {

        manager.removeSubTaskAll();
        List<SubTask> subTaskList = manager.getSubTaskAll();
        assertEquals(0, subTaskList.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("удаляем все Epic")
    public void removeEpicAllTest() {

        List<Epic> epics = manager.getEpicAll();


        manager.removeEpicAll();
        epics = manager.getEpicAll();
        assertEquals(0, epics.size(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("удаляем все Task ")
    public void removeTaskAllTest() {
        List<Task> tasks = manager.getTaskAll();

        manager.removeTaskAll();
        List<Task> taskList = manager.getTaskAll();

        assertEquals(0, taskList.size(), "Неверное количество задач.");
    }

}
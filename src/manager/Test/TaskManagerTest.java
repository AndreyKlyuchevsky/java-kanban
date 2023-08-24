package manager.Test;

import manager.TaskManager;
import model.Epic;
import model.StatusTask;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {


    protected abstract T getManager();


    @Test
    //добавляем Task задачу
    public void addTaskTest() throws IOException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", StatusTask.NEW);
        getManager().addTask(task);
        final int taskId = task.getId();
        final Task savedTask = getManager().getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> taskList = getManager().getTaskAll();
        assertNotNull(taskList, "Задачи не возвращаются.");
        assertEquals(1, taskList.size(), "Неверное количество задач.");
        for (Task tasks : taskList) {
            if(tasks.getId()==1){
                assertEquals(task, tasks, "Задачи не совпадают.");
            }
        }
    }

    @Test
    //удаляем Task задачу
    public void removeTaskTest() throws IOException {
        Task task = new Task("Test addNewTask", "Test addNewTask description", StatusTask.NEW);
        getManager().addTask(task);
        final int taskId = task.getId();
        List<Task> tasks = getManager().getTaskAll();
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        getManager().removeTaskById(taskId);
        tasks = getManager().getTaskAll();
        assertEquals(0, tasks.size(), "Неверное количество задач.");
    }

    @Test
    //удаляем Task задачу не существующую
    public void removeNullTaskTest() throws IOException {
        try {
            getManager().removeTaskById(8);
        } catch (NullPointerException exception) {
            System.out.println(exception);
        }
    }

    @Test
    //удаляем Task задачу не существующую
    public void removeNullEpicTest() throws IOException {
        try {
            getManager().removeEpicById(100);
        } catch (NullPointerException exception) {
            System.out.println(exception);
        }

    }

    @Test
    //добавляем Epic задачу
    public void addEpicTest() throws IOException {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        getManager().addEpic(epic);
        final int epicId = epic.getId();
        final Epic savedEpic = getManager().getEpicById(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final List<Epic> epicList = getManager().getEpicAll();
        assertNotNull(epicList, "Задачи на возвращаются.");
        assertEquals(1, epicList.size(), "Неверное количество задач.");
        for (Epic epics : epicList) {
            if(epics.getId()==1){
                assertEquals(epic, epics, "Задачи не совпадают.");
            }
        }
    }

    @Test
    //удаляем Epic задачу
    public void removeEpicTest() throws IOException {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        getManager().addEpic(epic);
        final int epicId = epic.getId();
        List<Epic> epics = getManager().getEpicAll();
        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        getManager().removeEpicById(epicId);
        epics = getManager().getEpicAll();
        assertEquals(0, epics.size(), "Неверное количество задач.");
    }

    @Test
    //добавляем SubTask задачу
    public void addSubTaskTest() throws IOException {
        Epic epic = new Epic("Test addNewTask", "Test addNewTask description");
        getManager().addEpic(epic);
        SubTask subTask1 = new SubTask("Test addNewTask", "Test addNewTask description", StatusTask.NEW, epic.getId());
        SubTask subTask2 = new SubTask("Test addNewTask", "Test addNewTask description", StatusTask.NEW, epic.getId());

        //проверяем список подзадач у epic. Должен быть 0
        final List<SubTask> subTaskNull = getManager().getSubTaskAll();
        int countSibTask = 0;
        for (SubTask item : subTaskNull) {
            if (item.getEpicId() == epic.getId()) {
                countSibTask++;
            }
        }
        assertEquals(0, countSibTask, "У epic есть подзадачи!");

        //проверяем статус epic - должен быть NEW
        assertEquals(StatusTask.NEW, getManager().getEpicById(epic.getId()).getStatus(), "Не верный статус");

        //добавляем подзадачу
        getManager().addSubTask(subTask1);
        getManager().addSubTask(subTask2);
        final int subTaskId = subTask1.getId();
        final SubTask saveSubTask = getManager().getSubtaskById(subTaskId);

        //проверяем добавление задачи
        assertNotNull(saveSubTask, "Задача не найдена.");
        assertEquals(subTask1, saveSubTask, "Задачи не совпадают.");
        final List<SubTask> subsTaskList = getManager().getSubTaskAll();
        assertNotNull(subsTaskList, "Задачи не возвращаются.");
        assertEquals(2, subsTaskList.size(), "Неверное количество задач.");
        for (SubTask subTask : subsTaskList) {
            if(subTask.getId()==subTaskId){
                assertEquals(subTask1, subTask, "Задачи не совпадают.");
            }
        }



        //проверяем статус epic - должен быть NEW
        assertEquals(StatusTask.NEW, getManager().getEpicById(epic.getId()).getStatus(), "Не верный статус");


        //меняем статус у subTask1 -IN_PROGRESS
        subTask1.setStatus(StatusTask.IN_PROGRESS);
        getManager().updateSubTask(subTask1);
        assertEquals(StatusTask.IN_PROGRESS, getManager().getEpicById(epic.getId()).getStatus(), "Не верный статус");


        //меняем статус у subTask1 DONE
        subTask1.setStatus(StatusTask.DONE);
        getManager().updateSubTask(subTask1);
        assertEquals(StatusTask.DONE, subTask1.getStatus(), "Не верный статус");

        //проверяем статус epic - должен быть DONE
        assertEquals(StatusTask.IN_PROGRESS, getManager().getEpicById(epic.getId()).getStatus(), "Не верный статус");

        //меняем статус у subTask2 DONE
        subTask2.setStatus(StatusTask.DONE);
        getManager().updateSubTask(subTask2);
        assertEquals(StatusTask.DONE, subTask2.getStatus(), "Не верный статус");

        //проверяем статус epic - должен быть DONE
        assertEquals(StatusTask.DONE, getManager().getEpicById(epic.getId()).getStatus(), "Не верный статус");

    }
}
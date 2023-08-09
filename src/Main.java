import manager.Managers;
import manager.TaskManager;
import model.Epic;
import model.StatusTask;
import model.Subtask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new Managers().getDefault();


        Task task1 = new Task("Первая задача", "очень важная первая задача", StatusTask.NEW);
        Task task2 = new Task("Вторая задача", "оычень важная вторая задача", StatusTask.NEW);
        Epic epic1 = new Epic("Первая большая задача", "очень важная первая большая задача");
        Epic epic2 = new Epic("Вторая большая задача", "очень важная вторая большая задача");


        //добавляем 2 простых задачи Task
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        //добавляем Epic задачи
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        //создаем подзадачи
        Subtask subtask1 = new Subtask("Первая подзадача", "очень важная первая подзадача 1 Epic", StatusTask.DONE, epic1.getId());
        Subtask subtask2 = new Subtask("Вторая подзадача", "очень важная вторая подзадача 1 Epic", StatusTask.DONE, epic1.getId());
        Subtask subtask5 = new Subtask("Вторая подзадача", "очень важная вторая подзадача 1 Epic", StatusTask.DONE, epic1.getId());


        //добавляем в  Epic  подзадачи
        taskManager.addSubTask(subtask1);
        taskManager.addSubTask(subtask2);
        taskManager.addSubTask(subtask5);


        //печатаем Epic1
        System.out.println("Смотрим 1 Ерic");
        taskManager.getTaskById(epic1.getId());
        System.out.println(taskManager.getHistory());

        System.out.println("Смотрим 1,2 Task");
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        System.out.println(taskManager.getHistory());

        System.out.println("Смотрим 1 Ерic и подзадачи");
        taskManager.getTaskById(epic1.getId());
        taskManager.getTaskById(subtask1.getId());
        taskManager.getTaskById(subtask2.getId());
        taskManager.getTaskById(subtask5.getId());
        System.out.println(taskManager.getHistory());
        //удаляем Task
        System.out.println("удаляем Task");
        taskManager.removeTaskById(task1.getId());
        System.out.println(taskManager.getHistory());
        //удаляем Epic
        System.out.println("удаляем Epic");
        taskManager.removeTaskById(epic1.getId());
        System.out.println(taskManager.getHistory());

    }
}

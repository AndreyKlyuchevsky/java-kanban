import Managers.InMemoryTaskManager;
import Managers.Managers;
import Managers.TaskManager;
import Tasks.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager inMemoryTaskManager = new Managers().getDefault();


        Task task1 = new Task("Первая задача", "очень важная первая задача", StatusTask.NEW);
        Task task2 = new Task("Вторая задача", "оычень важная вторая задача", StatusTask.NEW);
        Epic epic1 = new Epic("Первая большая задача", "очень важная первая большая задача");
        Epic epic2 = new Epic("Вторая большая задача", "очень важная вторая большая задача");


        //добавляем 2 простых задачи Task
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);

        //добавляем Epic задачи
        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.addEpic(epic2);

        //создаем подзадачи
        Subtask subtask1 = new Subtask("Первая подзадача", "очень важная первая подзадача 1 Epic", StatusTask.DONE, epic1.getId());
        Subtask subtask2 = new Subtask("Вторая подзадача", "очень важная вторая подзадача 1 Epic", StatusTask.DONE, epic1.getId());
        Subtask subtask5 = new Subtask("Вторая подзадача", "очень важная вторая подзадача 1 Epic", StatusTask.DONE, epic1.getId());


        //добавляем в  Epic  подзадачи
        inMemoryTaskManager.addSubTask(subtask1);
        inMemoryTaskManager.addSubTask(subtask2);
        inMemoryTaskManager.addSubTask(subtask5);


        //печатаем Epic1
        System.out.println("Смотрим 1 Ерic");
        inMemoryTaskManager.getTaskById(epic1.getId());
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Смотрим 1,2 Task");
        inMemoryTaskManager.getTaskById(task1.getId());
        inMemoryTaskManager.getTaskById(task2.getId());
        System.out.println(inMemoryTaskManager.getHistory());

        System.out.println("Смотрим 1 Ерic и подзадачи");
        inMemoryTaskManager.getTaskById(epic1.getId());
        inMemoryTaskManager.getTaskById(subtask1.getId());
        inMemoryTaskManager.getTaskById(subtask2.getId());
        inMemoryTaskManager.getTaskById(subtask5.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        //удаляем Task
        System.out.println("удаляем Task");
        inMemoryTaskManager.removeTaskById(task1.getId());
        System.out.println(inMemoryTaskManager.getHistory());
        //удаляем Epic
        System.out.println("удаляем Epic");
        inMemoryTaskManager.removeTaskById(epic1.getId());
        System.out.println(inMemoryTaskManager.getHistory());

    }
}

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
        Subtask subtask3 = new Subtask("Первая подзадача", "оычень важная вторая подзадача 2 Epic", StatusTask.NEW, epic2.getId());

        //добавляем в  Epic  подзадачи
        inMemoryTaskManager.addSubTask(subtask1);
        inMemoryTaskManager.addSubTask(subtask2);
        inMemoryTaskManager.addSubTask(subtask5);

        //добавляем в 4 Epic 1 подзадачу
        inMemoryTaskManager.addSubTask(subtask3);

        //печатаем Epic1
        System.out.println(inMemoryTaskManager.getTaskById(epic1.getId()));

        //меняем статус у Epic и description
        System.out.println("меняем статус у Epic и description");
        Subtask subtask4 = new Subtask("Первая подзадача", "очень важная первая подзадача 1 Epic", StatusTask.NEW, epic1.getId(), subtask1.getId());
        inMemoryTaskManager.updateSubTask(subtask4);
        inMemoryTaskManager.updateEpic(new Epic("Первая большая задача", "самая важная первая большая задача", epic1.getId()));
        System.out.println(inMemoryTaskManager.getTaskById(epic1.getId()));

        //печатаем все задачи
        System.out.println("печатаем все задачи");
        System.out.println(inMemoryTaskManager.getAll());

        //печатаем все задачи по очереди. В начале все Task, потом Epic, последним Subtask
        System.out.println("Выводим все Task");
        System.out.println(inMemoryTaskManager.getTaskAll());
        System.out.println("Выводим все Epic");
        System.out.println(inMemoryTaskManager.getEpicAll());
        System.out.println("Выводим все Subtask");
        System.out.println(inMemoryTaskManager.getSubTaskAll());

        //удаляем Task
        System.out.println("удаляем Task");
        inMemoryTaskManager.removeTaskById(task1.getId());
        System.out.println(inMemoryTaskManager.getAll());
        System.out.println("удаляем все Task");
        inMemoryTaskManager.removeTaskAll();
        System.out.println(inMemoryTaskManager.getTaskAll());

        //удаляем Subtask
        System.out.println("удаляем Subtask");
        inMemoryTaskManager.removeTaskById(subtask4.getId());
        System.out.println(inMemoryTaskManager.getAll());
        inMemoryTaskManager.removeSubTaskAll();
        System.out.println(inMemoryTaskManager.getSubTaskAll());

        //удаляем Epic
        System.out.println("удаляем Epic");
        inMemoryTaskManager.removeTaskById(epic1.getId());
        System.out.println(inMemoryTaskManager.getAll());
        inMemoryTaskManager.removeEpicAll();
        System.out.println(inMemoryTaskManager.getEpicAll());

        //удаляем все задачи
        inMemoryTaskManager.removeAll();
        System.out.println("удаляем все задачи");
        System.out.println(inMemoryTaskManager.getAll());
        System.out.println(inMemoryTaskManager.getHistory());
    }
}

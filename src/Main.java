import Managers.Manager;
import Tasks.*;
public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Manager manager=new Manager();
        Task task1 = new Task("Первая задача","очень важная первая задача",StatusTask.NEW);
        Task task2 = new Task("Вторая задача","оычень важная вторая задача",StatusTask.NEW);
        Epic epic1 = new Epic("Первая большая задача","очень важная первая большая задача");
        Epic epic2 = new Epic("Вторая большая задача","очень важная вторая большая задача");


        //добавляем 2 простых задачи Task
        manager.addTask(task1);
        manager.addTask(task2);

        //добавляем Epic задачи
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        //создаем подзадачи
        Subtask subtask1 = new Subtask("Первая подзадача","очень важная первая подзадача 1 Epic", StatusTask.DONE,epic1.getId());
        Subtask subtask2 = new Subtask("Вторая подзадача","очень важная вторая подзадача 1 Epic",StatusTask.DONE,epic1.getId());
        Subtask subtask5 = new Subtask("Вторая подзадача","очень важная вторая подзадача 1 Epic",StatusTask.DONE,epic1.getId());
        Subtask subtask3 = new Subtask("Первая подзадача","оычень важная вторая подзадача 2 Epic",StatusTask.NEW,epic2.getId());

        //добавляем в  Epic  подзадачи
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask5);

        //добавляем в 4 Epic 1 подзадачу
        manager.addSubtask(subtask3);

        //печатаем Epic1
        System.out.println(manager.getTaskToId(epic1.getId()));

        //меняем статус у Epic и description
        System.out.println("меняем статус у Epic и description");
        Subtask subtask4 = new Subtask("Первая подзадача","очень важная первая подзадача 1 Epic", StatusTask.NEW,epic1.getId(),subtask1.getId());
        manager.updateSubtask(subtask4);
        manager.updateEpic(new Epic("Первая большая задача","самая важная первая большая задача",epic1.getId()));
        System.out.println(manager.getTaskToId(epic1.getId()));

        //печатаем все задачи
        System.out.println("печатаем все задачи");
        System.out.println(manager.getAll());

        //печатаем все задачи по очереди. В начале все Task, потом Epic, последним Subtask
        System.out.println("Выводим все Task");
        System.out.println(manager.getTaskAll());
        System.out.println("Выводим все Epic");
        System.out.println(manager.getEpicAll());
        System.out.println("Выводим все Subtask");
        System.out.println(manager.getSubtaskAll());

        //удаляем Task
        System.out.println("удаляем Task");
        manager.removeTaskToId(task1.getId());
        System.out.println(manager.getAll());
        System.out.println("удаляем все Task");
        manager.removeTaskAll();
        System.out.println(manager.getTaskAll());

        //удаляем Subtask
        System.out.println("удаляем Subtask");
        manager.removeTaskToId(subtask4.getId());
        System.out.println(manager.getAll());
        manager.removeSubtaskAll();
        System.out.println(manager.getSubtaskAll());

        //удаляем Epic
        System.out.println("удаляем Epic");
        manager.removeTaskToId(epic1.getId());
        System.out.println(manager.getAll());
        manager.removeEpicAll();
        System.out.println(manager.getEpicAll());

        //удаляем все задачи
        manager.removeAll();
        System.out.println("удаляем все задачи");
        System.out.println(manager.getAll());

    }
}

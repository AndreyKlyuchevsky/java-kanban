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
        //добавляем 2 Epic задачи
        manager.addEpic(epic1);
        manager.addEpic(epic2);

        Subtask subtask1 = new Subtask("Первая подзадача","очень важная первая подзадача 1 Epic", StatusTask.NEW,epic1.getId());
        Subtask subtask2 = new Subtask("Вторая подзадача","очень важная вторая подзадача 1 Epic",StatusTask.NEW,epic1.getId());
        Subtask subtask3 = new Subtask("Первая подзадача","оычень важная вторая подзадача 2 Epic",StatusTask.NEW,epic2.getId());

        //добавляем в 3 Epic 2 подзадачи
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        //добавляем в 4 Epic 1 подзадачу
        manager.addSubtask(subtask3);
        //печатаем Epic1
        System.out.println("Печатаем Epic");
        System.out.println(manager.getId(epic1.getId()));
        //меняем статус у Epic
        System.out.println("меняем статус у Epic");
        Subtask subtask4 = new Subtask("Первая подзадача","очень важная первая подзадача 1 Epic", StatusTask.DONE,epic1.getId(),subtask1.getId());
        manager.updateSubtask(subtask4);
        System.out.println(manager.getId(epic1.getId()));
        //печатаем все задачи
        System.out.println("печатаем все задачи");
        System.out.println(manager.printAll());
        //удаляем Task
        System.out.println("удаляем Task");
        manager.removeId(task1.getId());
        System.out.println(manager.printAll());
        //удаляем Subtask
        System.out.println("удаляем Subtask");
        manager.removeId(subtask4.getId());
        System.out.println(manager.printAll());
        //удаляем Epic
        System.out.println("удаляем Epic");
        manager.removeId(epic1.getId());
        System.out.println(manager.printAll());
        //удаляем все задачи
        manager.removeAll();
        System.out.println("удаляем все задачи");
        System.out.println(manager.printAll());
        
    }
}

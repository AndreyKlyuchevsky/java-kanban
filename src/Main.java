public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        Manager manager=new Manager();
        Task task1 = new Task("Первая задача","очень важная первая задача");
        Task task2 = new Task("Вторая задача","оычень важная вторая задача");
        Epic epic1 = new Epic("Первая большая задача","очень важная первая большая задача");
        Epic epic2 = new Epic("Вторая большая задача","очень важная вторая большая задача");
        Subtask subtask1 = new Subtask("Первая подзадача","очень важная первая подзадача 1 Epic");
        Subtask subtask2 = new Subtask("Вторая подзадача","очень важная вторая подзадача 1 Epic");
        Subtask subtask3 = new Subtask("Первая подзадача","оычень важная вторая подзадача 2 Epic");

        //добавляем 2 простых задачи Task
        manager.addTask(task1);
        manager.addTask(task2);
        //добавляем 2 Epic задачи
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        //добавляем в 3 Epic 2 подзадачи
        manager.addSubtask(subtask1,epic1);
        manager.addSubtask(subtask2,epic1);
        //добавляем в 4 Epic 1 подзадачу
        manager.addSubtask(subtask3,epic2);
        System.out.println(manager.printAll());
        //меняем статус у Epic
        System.out.println("меняем статус у Epic");
        manager.updateSubtask("Первая подзадача","очень важная первая подзадача 1 Epic","DONE",epic1,subtask1);
        System.out.println(manager.printAll());
        //удаляем статус у Epic
        manager.removeID(epic1.id);
        System.out.println(manager.printAll());
    }
}

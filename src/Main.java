import manager.file.FileBackedTasksManager;
import model.Epic;
import model.StatusTask;
import model.Subtask;
import model.Task;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        FileBackedTasksManager taskManagerOld = new FileBackedTasksManager(new File("filewriter.csv"));

        Task task1 = new Task("Первая задача", "очень важная первая задача", StatusTask.NEW);
        Task task2 = new Task("Вторая задача", "оычень важная вторая задача", StatusTask.NEW);
        Epic epic1 = new Epic("Первая большая задача", "очень важная первая большая задача");
        Epic epic2 = new Epic("Вторая большая задача", "очень важная вторая большая задача");


        //добавляем 2 простых задачи Task
        taskManagerOld.addTask(task1);
        taskManagerOld.addTask(task2);

        //добавляем Epic задачи
        taskManagerOld.addEpic(epic1);
        taskManagerOld.addEpic(epic2);

        //создаем подзадачи
        Subtask subtask1 = new Subtask("Первая подзадача", "очень важная первая подзадача 1 Epic", StatusTask.DONE, epic1.getId());
        Subtask subtask2 = new Subtask("Вторая подзадача", "очень важная вторая подзадача 1 Epic", StatusTask.DONE, epic1.getId());
        Subtask subtask5 = new Subtask("Вторая подзадача", "очень важная вторая подзадача 1 Epic", StatusTask.DONE, epic1.getId());


        //добавляем в  Epic  подзадачи
        taskManagerOld.addSubTask(subtask1);
        taskManagerOld.addSubTask(subtask2);
        taskManagerOld.addSubTask(subtask5);


        //печатаем Epic1
        System.out.println("Смотрим 1 Ерic");
        taskManagerOld.getTaskById(epic1.getId());
        System.out.println(taskManagerOld.getHistory());

        System.out.println("Смотрим 1,2 Task");
        taskManagerOld.getTaskById(task1.getId());
        taskManagerOld.getTaskById(task2.getId());
        System.out.println(taskManagerOld.getHistory());

        System.out.println("Смотрим 1 Ерic и подзадачи");
        taskManagerOld.getTaskById(epic1.getId());
        taskManagerOld.getTaskById(subtask1.getId());
        taskManagerOld.getTaskById(subtask2.getId());
        taskManagerOld.getTaskById(subtask5.getId());
        System.out.println(taskManagerOld.getHistory());
        //удаляем Task
        System.out.println("удаляем Task");
        taskManagerOld.removeTaskById(task1.getId());
        System.out.println(taskManagerOld.getHistory());
        //удаляем Epic
        System.out.println("удаляем Epic");
        taskManagerOld.removeTaskById(epic1.getId());
        System.out.println(taskManagerOld.getHistory());

        FileBackedTasksManager taskManagerNew = FileBackedTasksManager.loadFromFile(new File("filewriter.csv"));
        boolean taskBoolean = testTaskCompare(taskManagerOld.getTaskAll(), taskManagerNew.getTaskAll());
        boolean EpicBoolean = testTaskCompare(taskManagerOld.getEpicAll(), taskManagerNew.getEpicAll());
        boolean SubTaskBoolean = testTaskCompare(taskManagerOld.getSubTaskAll(), taskManagerNew.getSubTaskAll());

        boolean HistoryBoolean = testTaskCompare(taskManagerOld.getTaskAll(), taskManagerNew.getTaskAll());

        System.out.println("Результат проверки Task: " + taskBoolean);
        System.out.println("Результат проверки Epic: " + EpicBoolean);
        System.out.println("Результат проверки Sub: " + SubTaskBoolean);
        System.out.println("Результат проверки HistoryTask: " + HistoryBoolean);
    }


    private static <T extends Task> boolean testTaskCompare(List<T> taskListOld, List<T> taskListNew) {
        for (Task taskNew : taskListNew) {
            for (Task taskOld : taskListOld) {
                if (taskOld.getId() == taskNew.getId()) {
                    if (!taskOld.equals(taskNew)) {
                        return false;
                    }
                    break;
                }
            }
        }
        return true;
    }


}


import com.google.gson.Gson;
import manager.file.FileBackedTasksManager;
import manager.server.HttpTaskServer;
import manager.server.KVServer;
import model.Epic;
import model.StatusTask;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;



public class Main {
    private static final String URL = "http://localhost:8080/tasks/task?id=1";

    public static void main(String[] args) throws IOException {
        KVServer server = new KVServer();

        server.start();
        FileBackedTasksManager taskManagerOld = new FileBackedTasksManager("filewriter.csv");
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManagerOld, new Gson());
        httpTaskServer.start();



        Task task1 = new Task("Первая задача", "очень важная первая задача", StatusTask.NEW, 8, LocalDateTime.of(2023, 9, 12, 00, 00, 00));
        Task task2 = new Task("Вторая задача", "оычень важная вторая задача", StatusTask.NEW, 8, LocalDateTime.of(2023, 9, 8, 00, 00, 00));
        Epic epic1 = new Epic("Первая большая задача", "очень важная первая большая задача");
        Epic epic2 = new Epic("Вторая большая задача", "очень важная вторая большая задача");


        //добавляем 2 простых задачи Task
        taskManagerOld.addTask(task1);
        taskManagerOld.addTask(task2);

        //добавляем Epic задачи
        taskManagerOld.addEpic(epic1);
        taskManagerOld.addEpic(epic2);

        //создаем подзадачи

        SubTask subtask1 = new SubTask("Первая подзадача", "очень важная первая подзадача 1 Epic", StatusTask.DONE, epic1.getId(), 8, LocalDateTime.of(2023, 9, 12, 00, 00, 00));
        SubTask subtask2 = new SubTask("Вторая подзадача", "очень важная вторая подзадача 1 Epic", StatusTask.DONE, epic1.getId(), 2, LocalDateTime.of(2023, 9, 15, 00, 00, 00));

        SubTask subtask5 = new SubTask("Вторая подзадача", "очень важная вторая подзадача 1 Epic", StatusTask.DONE, epic1.getId(), 15, LocalDateTime.of(2023, 9, 21, 00, 00, 00));


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

        FileBackedTasksManager taskManagerNew = FileBackedTasksManager.loadFromFile("filewriter.csv");

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
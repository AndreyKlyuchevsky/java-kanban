package manager.file;

import manager.mem.InMemoryTaskManager;
import model.Epic;
import model.StatusTask;
import model.Subtask;
import model.Task;
import model.TaskType;

import java.io.*;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File path;
    private static final String HEADER = "id,type,name,status,description,epic";

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

        FileBackedTasksManager taskManagerNew = loadFromFile(new File("filewriter.csv"));
        boolean taskBoolean = testTaskCompare(taskManagerOld.getTaskAll(),taskManagerNew.getTaskAll());
        boolean EpicBoolean = testTaskCompare(taskManagerOld.getEpicAll(),taskManagerNew.getEpicAll());
        boolean SubTaskBoolean =testTaskCompare(taskManagerOld.getSubTaskAll(),taskManagerNew.getSubTaskAll());

        boolean HistoryBoolean =testTaskCompare(taskManagerOld.getTaskAll(),taskManagerNew.getTaskAll());

        System.out.println("Результат проверки Task: " + taskBoolean );
        System.out.println("Результат проверки Epic: " + EpicBoolean );
        System.out.println("Результат проверки Sub: " + SubTaskBoolean );
        System.out.println("Результат проверки HistoryTask: " + HistoryBoolean );
    }


    private static <T extends Task>  boolean  testTaskCompare(List<T> taskListOld, List<T> taskListNew) {
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

    public FileBackedTasksManager(File path) {
            this.path = path;
        }

        public static FileBackedTasksManager loadFromFile (File file){
            FileBackedTasksManager manager = new FileBackedTasksManager(file);
            loadTasksFromFile(manager);
            return manager;
        }

        //не проверял
        public static void loadTasksFromFile (FileBackedTasksManager manager){
            boolean first = true;
            try (BufferedReader reader = new BufferedReader(new FileReader(manager.path))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (first) {
                        first = false;
                    } else if (line.isEmpty()) {
                        historyFromString(reader.readLine(), manager);
                        break;
                    } else {
                        taskFromString(line, manager);
                    }
                }
            } catch (IOException exception) {
                throw new ManagerSaveException("file upload error " + manager.path, exception);
            }

        }

        private static void taskFromString (String line, FileBackedTasksManager manager){
            String[] values = line.split(",");
            if (values.length < 5) {
                throw new IllegalArgumentException("Invalid format: " + line);
            }

            int id = Integer.parseInt(values[0]);
            TaskType type = TaskType.valueOf(values[1]);
            String name = values[2];
            StatusTask status = StatusTask.valueOf(values[3]);
            String description = values[4];

            Task task;

            switch (type) {
                case TASK:
                    task = new Task(name, description, id, status);
                    manager.taskList.put(task.getId(), task);
                    break;
                case EPIC:
                    task = new Epic(name, description, id);
                    manager.epicList.put(task.getId(), (Epic) task);
                    break;
                case SUBTASK:
                    int epicId = Integer.parseInt(values[5]);
                    task = new Subtask(name, description, status, epicId, id);
                    manager.subtaskList.put(task.getId(), (Subtask) task);
                    manager.epicList.get(epicId).setSubtaskId(id);
                    manager.updateStatus(epicId);
                    break;
            }
        }


        public static void historyFromString (String value, FileBackedTasksManager manager){

            String[] ids = value.split(",");
            for (String id : ids) {
                manager.getTaskById(Integer.parseInt(id));
            }
        }


        //методы для сохранения
        public void save () {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
                bufferedWriter.write(HEADER);
                bufferedWriter.newLine();

                for (Task task : super.getAll()) {
                    bufferedWriter.write(taskToString(task));
                    bufferedWriter.newLine();
                }

                bufferedWriter.newLine(); // Пустая строка
                bufferedWriter.write(historyToString());
            } catch (IOException exception) {
                throw new ManagerSaveException("Failed to save tasks to file: " + path, exception);
            }
        }

        private String taskToString (Task task){
            String line = task.getId() + ","
                    + task.getType() + ","
                    + task.getName() + ","
                    + task.getStatus() + ","
                    + task.getDescription() + ",";
            if(task.getType()==TaskType.SUBTASK){
                Subtask subTask = (Subtask) task;
                line=line+subTask.getEpicId()+",";
            }else{
                line=line+",";
            }
            return line;
        }

        @Override
        public Task getTaskById ( int id){
            Task task = super.getTaskById(id);
            save();
            return task;
        }

        private String historyToString () {

            StringBuilder stringBuilder = new StringBuilder();
            for (Task tasks : super.getHistory()) {
                stringBuilder.append(tasks.getId()).append(",");
            }
            return stringBuilder.toString();
        }

        @Override
        public void updateTask (Task task){
            super.updateTask(task);
            save();
        }

        @Override
        public void updateEpic (Epic epic){
            super.updateEpic(epic);
            save();
        }

        @Override
        public void updateSubTask (Subtask subtask){
            super.updateSubTask(subtask);
            save();
        }


        @Override
        public void removeTaskAll () {
            super.removeTaskAll();
            save();
        }

        @Override
        public void removeEpicAll () {
            super.removeEpicAll();
            save();
        }

        @Override
        public void removeSubTaskAll () {
            super.removeSubTaskAll();
            save();
        }

        @Override
        public void removeTaskById ( int id){
            super.removeTaskById(id);
            save();
        }

        @Override
        public void addTask (Task task){
            super.addTask(task);
            save();
        }

        @Override
        public void addEpic (Epic epic){
            super.addEpic(epic);
            save();
        }

        @Override
        public void addSubTask (Subtask subtask){
            super.addSubTask(subtask);
            save();
        }


    }

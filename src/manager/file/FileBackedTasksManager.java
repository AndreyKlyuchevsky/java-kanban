package manager.file;

import manager.mem.InMemoryTaskManager;
import model.Epic;
import model.StatusTask;
import model.Subtask;
import model.Task;
import model.TaskType;

import java.io.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private File path;
    private String header = "id, type, name, status, description, epic";

    public FileBackedTasksManager(File path) {
        this.path = path;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        loadTasksFromFile(manager);
        return manager;
    }

    //не проверял
    public static void loadTasksFromFile(FileBackedTasksManager manager) {

        try (BufferedReader reader = new BufferedReader(new FileReader(manager.path))) {
            String line;
            while ((line = reader.readLine()) != null) {

                if (line.equals("")) {
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

    private static void taskFromString(String line, FileBackedTasksManager manager) {
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
                break;
        }
    }


    public static void historyFromString(String value, FileBackedTasksManager manager) {

        String[] ids = value.split(",");
        for (String id : ids) {
            manager.getTaskById(Integer.parseInt(id));
        }
    }


    //методы для сохранения
    public void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
            bufferedWriter.write(header);
            bufferedWriter.newLine();

            for (Task task : super.getAll()) {
                bufferedWriter.write(taskToString(task));
                bufferedWriter.newLine();
            }

            bufferedWriter.newLine(); // Пустая строка
            bufferedWriter.write(historyToString());
        } catch (IOException exception) {
            throw new ManagerSaveException("Failed to save tasks to file.", exception);
        }
    }

    private String taskToString(Task task) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(task.getId()).append(",");
        stringBuilder.append(task.getType()).append(",");
        stringBuilder.append(task.getName()).append(",");
        stringBuilder.append(task.getStatus()).append(",");
        stringBuilder.append(task.getDescription()).append(",");

        if (task instanceof Subtask) {
            Subtask subTask = (Subtask) task;
            stringBuilder.append(subTask.getEpicId());
        } else {
            stringBuilder.append(",");
        }

        return stringBuilder.toString();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    private String historyToString() {

        StringBuilder stringBuilder = new StringBuilder();
        for (Task tasks : super.getHistory()) {
            stringBuilder.append(tasks.getId()).append(",");
        }
        return stringBuilder.toString();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        super.updateSubTask(subtask);
        save();
    }


    @Override
    public void removeAll() {
        super.removeAll();
        save();
    }

    @Override
    public void removeTaskAll() {
        super.removeTaskAll();
        save();
    }

    @Override
    public void removeEpicAll() {
        super.removeEpicAll();
        save();
    }

    @Override
    public void removeSubTaskAll() {
        super.removeSubTaskAll();
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubTask(Subtask subtask) {
        super.addSubTask(subtask);
        save();
    }

    public static class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}

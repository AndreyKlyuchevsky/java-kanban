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
        for (Subtask subtask : manager.subtaskList.values()) {
            manager.epicList.get(subtask.getEpicId()).addSubtaskId(subtask.getId());
            manager.updateStatus(subtask.getEpicId());
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

    private String taskToString(Task task) {
        String line = task.getId() + ","
                + task.getType() + ","
                + task.getName() + ","
                + task.getStatus() + ","
                + task.getDescription() + ",";
        if (task.getType() == TaskType.SUBTASK) {
            Subtask subTask = (Subtask) task;
            line = line + subTask.getEpicId() + ",";
        } else {
            line = line + ",";
        }
        return line;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
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
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
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


}
package manager.file;

import manager.Managers;
import manager.mem.InMemoryTaskManager;
import model.*;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String path;
    private static final String HEADER = "id,type,name,status,description,epic,duration,startTime";


    public FileBackedTasksManager(String path) {
        this.path = path;
    }

    public static FileBackedTasksManager loadFromFile(String file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        loadFromFile(manager);
        return manager;
    }

    public static FileBackedTasksManager loadFromFile(FileBackedTasksManager manager) {
        loadTasksFromFile(manager);
        return manager;
    }

    private static void loadTasksFromFile(FileBackedTasksManager manager) {
        boolean first = true;
        try (BufferedReader reader = new BufferedReader(new FileReader(manager.path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                } else if (line.isEmpty()) {
                    manager.historyFromString(reader.readLine());
                    break;
                } else {
                    taskFromString(line, manager);
                }
            }
            for (SubTask subtask : manager.subTaskMap.values()) {
                Epic epic =  manager.epicMap.get(subtask.getEpicId());
                epic.addSubtaskId(subtask);
                manager.updateStatus(epic);
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
        int duration = 0;
        LocalDateTime startTime = null;
        if (type != TaskType.EPIC) {
            duration = Integer.parseInt(values[6]);
            startTime = LocalDateTime.parse(values[7]);
        }
        Task task;
        switch (type) {
            case TASK:
                task = new Task(name, description, id, status, duration, startTime);
                manager.taskMap.put(task.getId(), task);
                manager.taskTreeSet.remove(task);
                manager.taskTreeSet.add(task);
                break;
            case EPIC:
                task = new Epic(name, description, id);
                manager.epicMap.put(task.getId(), (Epic) task);
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(values[5]);
                task = new SubTask(name, description, status, id, epicId, duration, startTime);
                manager.subTaskMap.put(task.getId(), (SubTask) task);
                manager.taskTreeSet.remove(task);
                manager.taskTreeSet.add(task);
                break;
        }
    }


    private List<Integer> historyFromString(String value) {
        if (value == null) {
            return new ArrayList<>();
        }

        String[] ids = value.split(",");
        List<Integer> idList = Arrays.stream(ids)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        for (Integer id : idList) {
            this.getTaskById(id);
        }
        return idList;
    }


    //методы для сохранения
    protected void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(HEADER);
            writer.newLine();

            // Save tasks to the file
            for (Task task : super.getAll()) {
                writer.write(taskToString(task));
                writer.newLine();
            }

            writer.newLine();
            writer.write(historyToString());
        } catch (IOException exception) {
            throw new ManagerSaveException("Failed to save tasks to file: " + path, exception);
        }
    }


    private String taskToString(Task task) {
        return task.getId() + ","
                + task.getType() + ","
                + task.getName() + ","
                + task.getStatus() + ","
                + task.getDescription() + ","
                + task.getEpicId() + ","
                + task.getDuration() + ","
                + task.getStartTime() + ","
                + task.getDuration() + ","
                + task.getStartTime() + ",";
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
    public SubTask getSubtaskById(int id) {
        SubTask subtask = super.getSubtaskById(id);
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
    public void updateSubTask(SubTask subtask) {
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
    public void addSubTask(SubTask subtask) {
        super.addSubTask(subtask);
        save();
    }


}

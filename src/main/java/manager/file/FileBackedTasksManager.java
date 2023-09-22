package manager.file;

import manager.mem.InMemoryTaskManager;
import model.*;

import java.io.*;
import java.time.LocalDateTime;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final String path;
    private static final String HEADER = "id,type,name,status,description,epic,duration,startTime";


    public FileBackedTasksManager(String path) {
        this.path = path;
    }

    public static FileBackedTasksManager loadFromFile(String file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        loadTasksFromFile(manager);
        return manager;
    }

    public void load(){
        loadTasksFromFile(this);
    }

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
            for (SubTask subtask : manager.subTaskMap.values()) {
                manager.epicMap.get(subtask.getEpicId()).addSubtaskId(subtask);
                manager.updateStatus(subtask.getEpicId());
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
        if(type!=TaskType.EPIC){
            duration = Integer.parseInt(values[6]);
            startTime = LocalDateTime.parse(values[7]);
        }
        Task task;
        switch (type) {
            case TASK:
                task = new Task(name, description, id, status, duration, startTime);
                manager.taskMap.put(task.getId(), task);
                manager.resetTaskTreeSet(task);
                break;
            case EPIC:
                task = new Epic(name, description, id);
                manager.epicMap.put(task.getId(), (Epic) task);
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(values[5]);
                task = new SubTask(name, description, status, id, epicId, duration, startTime);
                manager.subTaskMap.put(task.getId(), (SubTask) task);
                manager.resetTaskTreeSet(task);
                break;
        }

    }


    public static void historyFromString(String value, FileBackedTasksManager manager) {
        if (value == null) {
            return;
        }
        String[] ids = value.split(",");
        for (String id : ids) {
            manager.getTaskById(Integer.parseInt(id));
        }
    }


    //методы для сохранения
    public void save() {
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

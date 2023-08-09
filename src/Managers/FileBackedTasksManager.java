package Managers;

import Tasks.Epic;
import Tasks.StatusTask;
import Tasks.Subtask;
import Tasks.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{
    private File path;
    private List<Task> tasks;

    public FileBackedTasksManager(File path) {
        this.path = path;
        this.tasks = new ArrayList<>();
        loadTasksFromFile();
    }
    //не проверял
    private void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            List<String> lines = new ArrayList<>();
            while ((line=reader.readLine()) != null) {
                lines.add(line);
            }
            for (int i = 1; i < lines.size(); i++) {
                line=lines.get(i);
                if(line.equals("")){
                    historyFromString(lines.get(lines.size()-1));
                    break;
                }else{
                    taskFromString(lines.get(i));

                }
            }

        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

    }

    private void taskFromString(String line) {
        String[] values = line.split(",");
        if (values.length < 5) {
            return;
        }

        int id = Integer.parseInt(values[0]);
        TaskType type = TaskType.valueOf(values[1]);
        String name = values[2];
        StatusTask status = StatusTask.valueOf(values[3]);
        String description = values[4];

        Task task;

        switch (type) {
            case TASK:
                task = new Task( name,  description,id,status);
                super.taskList.put(task.getId(),task);
                break;
            case EPIC:
                task = new Epic(name,description ,id);
                super.epicList.put(task.getId(), (Epic) task);
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(values[5]);
                task = new Subtask(name, description, status, epicId, id);
                super.subtaskList.put(task.getId(), (Subtask) task);
                break;
        }
    }


    public void historyFromString(String value) {

        String[] ids = value.split(",");
        for (String id : ids) {
            super.getTaskById(Integer.parseInt( id));
        }
    }


    //методы для сохранения
      public void save(){
        try {
            Writer fileWriter = new FileWriter(path);
            fileWriter.write("id" + "," + "type" + "," + "name" + "," + "status" + "," + "description" + "," + "epic" + "\n");
            for (Task task : super.getAll()) {
                fileWriter.write(taskToString(task)+ "\n");
            }
            fileWriter.write("\n");
            fileWriter.write(historyToString());
            fileWriter.close();
        }catch (IOException exception){
            System.out.println(exception.getMessage());
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

    public String historyToString() {

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

}

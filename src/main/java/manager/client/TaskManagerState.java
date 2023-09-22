package manager.client;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManagerState {
    private  Map<Integer, Task> taskMap = new HashMap<>();
    private  Map<Integer, Epic> epicMap = new HashMap<>();
    private  Map<Integer, SubTask> subTaskMap = new HashMap<>();

    public List<Integer> getHistory() {
        return history;
    }

    public void setHistory(List<Integer> history) {
        this.history = history;
    }

    private List<Integer> history = new ArrayList<>();

    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public Map<Integer, Epic> getEpicMap() {
        return epicMap;
    }

    public Map<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    public void setTaskMap(Map<Integer, Task> taskMap) {
        this.taskMap = taskMap;
    }

    public void setEpicMap(Map<Integer, Epic> epicMap) {
        this.epicMap = epicMap;
    }

    public void setSubTaskMap(Map<Integer, SubTask> subTaskMap) {
        this.subTaskMap = subTaskMap;
    }
}

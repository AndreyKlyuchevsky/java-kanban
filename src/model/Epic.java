package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskId = new ArrayList<>();

    public Epic(String name, String description, int id) {
        super(name, description, id, StatusTask.NEW);
        type= TaskType.EPIC;
    }

    public Epic(String name, String description) {
        super(name, description, StatusTask.NEW);
        type= TaskType.EPIC;
    }

    public TaskType getType() {
        return type;
    }

    public void removeSubtaskId(int id) {
        for (int i = 0; i < subtaskId.size(); i++) {
            if (subtaskId.get(i) == id) {
                subtaskId.remove(i);
            }
        }
    }

    public void removeSubtaskAll() {
        subtaskId.clear();
    }

    public ArrayList<Integer> getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(int subtaskId) {
        this.subtaskId.add(subtaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", subtaskId=" + subtaskId +
                '}' + "\n";
    }
}

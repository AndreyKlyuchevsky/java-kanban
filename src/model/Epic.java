package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtaskId = new ArrayList<>();

    public Epic(String name, String description, int id) {
        super(name, description, id, StatusTask.NEW);
        type = TaskType.EPIC;
    }

    public Epic(String name, String description) {
        super(name, description, StatusTask.NEW);
        type = TaskType.EPIC;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskId, epic.subtaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskId);
    }

    public TaskType getType() {
        return type;
    }

    public void removeSubtaskId(int id) {
        subtaskId.remove(id);
    }

    public void removeSubtaskAll() {
        subtaskId.clear();
    }

    public List<Integer> getSubtaskIds() {
        return subtaskId;
    }

    public void addSubtaskId(int subtaskId) {
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

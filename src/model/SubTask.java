package model;


import java.util.Objects;

public class SubTask extends Task {
    private int epicId;


    public SubTask(String name, String description, StatusTask status, int epicId, int subTaskId) {
        super(name, description, subTaskId, status);
        this.epicId = epicId;
        type= TaskType.SUBTASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subtask = (SubTask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

    public TaskType getType() {
        return type;
    }

    public SubTask(String name, String description, StatusTask status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
        type= TaskType.SUBTASK;
    }

    public SubTask(String name, String description, StatusTask status) {
        super(name, description, status);
        type= TaskType.SUBTASK;

    }


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ",idEpic= " + epicId +
                '}' + "\n";
    }
}

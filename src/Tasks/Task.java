package Tasks;

import Managers.TaskType;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected StatusTask status;
    private final TaskType type;

    public Task(String name, String description, StatusTask status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.type=TaskType.TASK;
    }

    public TaskType getType() {
        return type;
    }

    public Task(String name, String description, int id, StatusTask status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.type=TaskType.TASK;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatusTask getStatus() {
        return status;
    }

    public void setStatus(StatusTask statusTask) {
        this.status = statusTask;
    }


    @Override
    public String toString() {
        return "Task{" +
                "name ='" + name  + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}' + "\n";
    }
}

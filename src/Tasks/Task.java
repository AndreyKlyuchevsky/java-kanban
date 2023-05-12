package Tasks ;

public class Task {
    protected String title;//Название, кратко описывающее суть задачи
    protected String description; //Описание, в котором раскрываются детали.
    protected int id;//Уникальный идентификационный номер задачи
    protected StatusTask statusTask;

    public Task(String title, String description,StatusTask statusTask) {
        this.title = title;
        this.description = description;
        this.statusTask = statusTask;
    }
    public Task(String title, String description, int id, StatusTask statusTask) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.statusTask = statusTask;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return statusTask;
    }

    public void setStatus(StatusTask statusTask) {
        this.statusTask = statusTask;
    }


    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + statusTask + '\'' +
                '}' + "\n";
    }
}

package Tasks;


public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description,  StatusTask statusTask, int epicId,int subTaskId) {
        super(title, description,subTaskId,statusTask);
        this.epicId = epicId;
    }
    public Subtask(String title, String description,  StatusTask statusTask, int epicId) {
        super(title, description,statusTask);
        this.epicId = epicId;
    }

    public Subtask(String title, String description,StatusTask statusTask) {
        super(title, description,statusTask);

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
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + statusTask + '\'' +
                ",idEpic= " + epicId+
                '}'+ "\n";
    }
}

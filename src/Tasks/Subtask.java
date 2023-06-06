package Tasks;


public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, StatusTask status, int epicId, int subTaskId) {
        super(name, description, subTaskId, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, StatusTask status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, StatusTask status) {
        super(name, description, status);

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

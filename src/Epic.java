import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Subtask> subtask;

    public Epic(String title, String description, int id, String status) {
        super(title, description, id, status);
        subtask = new ArrayList<>();
    }

    public Epic(String title, String description) {
        super(title, description);
        subtask = new ArrayList<>();
    }

    public ArrayList<Subtask> getSubtask() {
        return subtask;
    }

    public void setSubtask(Subtask subtask) {
        this.subtask.add(subtask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtask=" + subtask +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}'+"\n";
    }
}

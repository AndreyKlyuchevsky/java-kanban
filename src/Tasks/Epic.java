package Tasks ;
import java.util.ArrayList;

public class Epic  extends Task {
    protected ArrayList<Integer> subtaskId= new ArrayList<>();

    public Epic(String title, String description, int id) {
        super(title, description, id, "NEW");
    }

    public Epic(String title, String description) {
        super(title, description, "NEW");
    }
    public void removeSubtaskId(int id) {

        for (int i = 0; i < subtaskId.size(); i++) {
            if(subtaskId.get(i)==id){
                subtaskId.remove(i);
            }
        }
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
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", subtaskId=" + subtaskId +
                '}' + "\n";
    }
}

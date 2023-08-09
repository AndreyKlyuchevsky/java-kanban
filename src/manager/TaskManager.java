package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import java.util.List;


public interface TaskManager {

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(Subtask subtask);

    Task getTaskById(int id);

    List <Subtask> getSubTaskEpic(Epic epic);

    List <Task> getTaskAll();

    List <Epic> getEpicAll();

    List <Subtask> getSubTaskAll();



    void removeAll();

    void removeTaskAll();

    void removeEpicAll();

    void removeSubTaskAll();

    void removeTaskById(int id);

    List <Task> getHistory();
}

package manager;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;


public interface TaskManager {

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(SubTask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subtask);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubtaskById(int id);

    List<SubTask> getSubTaskEpic(Epic epic);

    List<Task> getTaskAll();

    List<Epic> getEpicAll();

    List<SubTask> getSubTaskAll();

    void removeTaskAll();

    void removeEpicAll();

    void removeSubTaskAll();

    void removeTaskById(int id);

    void removeSubTaskById(int id);

    void removeEpicById(int id);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}

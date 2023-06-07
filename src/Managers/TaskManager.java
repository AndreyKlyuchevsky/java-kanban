package Managers;

import Tasks.Epic;
import Tasks.Subtask;
import Tasks.Task;
import java.util.ArrayList;


public interface TaskManager {

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubTask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(Subtask subtask);

    Task getTaskById(int id);

    ArrayList<Subtask> getSubTaskEpic(Epic epic);

    ArrayList<Task> getTaskAll();

    ArrayList<Epic> getEpicAll();

    ArrayList<Subtask> getSubTaskAll();

    ArrayList<Task> getAll();

    void removeAll();

    void removeTaskAll();

    void removeEpicAll();

    void removeSubTaskAll();

    void removeTaskById(int id);

    ArrayList<Task> getHistory();
}

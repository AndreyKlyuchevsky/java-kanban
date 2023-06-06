package Managers;

import Tasks.Epic;
import Tasks.StatusTask;
import Tasks.Subtask;
import Tasks.Task;

import java.util.ArrayList;
import java.util.Map;

public interface TaskManager {

    // создаем простую задачу
    void addTask(Task task);

    //создаем задачу(Epic) с подзадачами
    void addEpic(Epic epic);

    //создаем подзадачу
    void addSubTask(Subtask subtask);

    // добавляем задачу в общий список
    void updateTask(Task task);

    //обновлем задачу (Epic)
    void updateEpic(Epic epic);

    //обновлем подзадачу (SubTask)
    void updateSubTask(Subtask subtask);

    //возвращаем объект по Id
    Task getTaskById(int id);

    //возвращаем список подзадач конкретноко Epic
    ArrayList<Subtask> getSubTaskToEpic(Epic epic);

    //возвращаем все объекты Task
    ArrayList<Task> getTaskAll();

    //возвращаем все объекты Epic
    ArrayList<Epic> getEpicAll();

    //возвращаем все объекты SubTask
    ArrayList<Subtask> getSubTaskAll();

    //возвращаем все объекты
    ArrayList<Task> getAll();

    //удаляем все задачи
    void removeAll();

    //удаляем все Task
    void removeTaskAll();

    //удаляем все Epic
    void removeEpicAll();

    //удаляем все SubTask
    void removeSubTaskAll();

    //удаляем задачу по Id
    void removeTaskToId(int id);

    //история просмотров
    ArrayList<Task> getHistory();
}

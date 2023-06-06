package Managers;

import Tasks.Task;

import java.util.ArrayList;


public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> history = new ArrayList<>();

    //добавляем элемент в историю просмотров
    @Override
    public void add(Task task) {
        if (history.size() >= 10) {
            history.remove(9);
        }
        history.add(0, task);
    }

    //история просмотров
    @Override
    public ArrayList<Task> getHistory() {
        return history;
    }
}

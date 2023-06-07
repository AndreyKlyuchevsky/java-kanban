package Managers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.LinkedList;



public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> history = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (history.size() > 10) {
            history.removeFirst();
        }
        history.add( task);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}

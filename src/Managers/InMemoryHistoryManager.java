package Managers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;//хранит первый элемент
    private Node<Task> tail;
    private int size = 0;
    private final HashMap<Integer, Node<Task>> taskNode = new HashMap<>();


    @Override
    public void add(Task task) {
        if (task != null) {
            if (taskNode.size() == 0) {
                final Node<Task> newNode = new Node<>(null, task, null);
                head = newNode;
                tail = newNode;
                taskNode.put(task.getId(), newNode);
            } else if (taskNode.containsKey(task.getId())) {
                removeNode(taskNode.get(task.getId()));
                taskNode.remove(task.getId());
                linkLast(task);

            } else {
                linkLast(task);
            }
        }
        size++;
    }

    private void linkLast(Task task) {
        final Node<Task> newNode;

        newNode = new Node<Task>(tail, task, null);
        tail.next = newNode;
        tail = newNode;
        taskNode.put(task.getId(), newNode);
    }


    private void removeNode(Node<Task> node) {
        if (size > 0) {
            if (size == 1) {
                head = null;
                tail = null;
            } else if (tail.equals(node)) {
                tail.prev.next = null;
                tail = node.prev;
            } else {
                if (head.equals(node)) {
                    node.next.prev = null;
                    head = node.next;
                } else {
                    node.prev.next = node.next;
                    node.next.prev = node.prev;
                }
            }
        }
    }


    @Override
    public void removeAll() {
        taskNode.clear();
        tail = null;
        head = null;
        size = 0;
    }

    @Override
    public void remove(int id) {
        if (taskNode.containsKey(id)) {
            removeNode(taskNode.get(id));
            taskNode.remove(id);
        }
        size--;
    }


    @Override
    public List<Task> getHistory() {
        if (taskNode.size() == 0) {
            return new ArrayList<>();
        } else {
            List<Task> listTasks = new ArrayList<>();
            Node<Task> next = head;
            for (int i = 0; i < size; i++) {
                if (next != null) {
                    listTasks.add(next.tasks);
                    next = next.next;
                }
            }
            return listTasks;
        }

    }
}

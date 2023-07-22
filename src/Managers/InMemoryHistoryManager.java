package Managers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> customLinkedList = new ArrayList<>();
    private final HashMap<Integer, Node<Task>> taskNode = new HashMap<>();
    private Node head;//хранит первый элемент
    private Node<Task> tail;
    private int size = 0;

    @Override
    public void add(Task task) {
        // Реализуйте метод
        if (customLinkedList.size() == 0) {
            final Node<Task> newNode = new Node<>(null, task, null);
            final Node<Task> oldTail = tail;
            taskNode.put(task.getId(), newNode);
            customLinkedList.add(task);
            size++;
            head = newNode;
        } else {
            if (taskNode.containsKey(task.getId())) {
                removeNode(taskNode.get(task.getId()));
            }
            final Node<Task> oldTail = tail;
            final Node<Task> newNode = new Node<Task>(tail, task, null);
            customLinkedList.add(task);
            taskNode.put(task.getId(), newNode);
            tail = newNode;
            if (oldTail == null)
                tail = newNode;
            else
                oldTail.prev = newNode;
            size++;
        }


    }


    private void removeNode(Node element) {
        customLinkedList.remove(element.tasks);
    }


    @Override
    public void remove(int id) {
        if (taskNode.containsKey(id)) {
            removeNode(taskNode.get(id));
            taskNode.remove(id);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        if (customLinkedList.size() == 0) {
            return null;
        }
        return new ArrayList<>(customLinkedList);
    }
}

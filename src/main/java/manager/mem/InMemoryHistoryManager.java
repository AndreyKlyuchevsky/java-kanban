package manager.mem;

import manager.HistoryManager;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;//хранит первый элемент
    private Node<Task> tail;
    private final HashMap<Integer, Node<Task>> taskNode = new HashMap<>();

    private static class Node<Task> {
        public Task tasks;
        public Node<Task> next;
        public Node<Task> prev;

        public Node(Node<Task> prev, Task tasks, Node<Task> next) {
            this.tasks = tasks;
            this.next = next;
            this.prev = prev;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return Objects.equals(head, that.head) && Objects.equals(tail, that.tail) && Objects.equals(taskNode, that.taskNode);
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            if (taskNode.size() == 0 || (head.tasks.equals(task) && tail.tasks.equals(task))) {
                final Node<Task> newNode = new Node<>(null, task, null);
                head = newNode;
                tail = newNode;
                taskNode.put(task.getId(), newNode);
            } else if (taskNode.containsKey(task.getId())) {
                removeNode(taskNode.get(task.getId()));
                taskNode.remove(task.getId());
                if (taskNode.size() == 0){
                    final Node<Task> newNode = new Node<>(null, task, null);
                    head = newNode;
                    tail = newNode;
                    taskNode.put(task.getId(), newNode);
                }else{
                    linkLast(task);
                }
            } else {
                linkLast(task);
            }
        }
    }

    private void linkLast(Task task) {
        final Node<Task> newNode;

        newNode = new Node<Task>(tail, task, null);
        tail.next = newNode;
        tail = newNode;
        taskNode.put(task.getId(), newNode);
    }


    private void removeNode(Node<Task> node) {
        if (taskNode.size() > 0) {
            if (tail.equals(node) && head.equals(node)) {
                tail = null;
                head = null;
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
    public void remove(int id) {
        if (taskNode.containsKey(id)) {
            removeNode(taskNode.get(id));
            taskNode.remove(id);
        }

    }


    @Override
    public List<Task> getHistory() {
        List<Task> listTasks = new ArrayList<>();
        Node<Task> next = head;
        while (next != null) {
            listTasks.add(next.tasks);
            next = next.next;
        }
        return listTasks;
    }
}

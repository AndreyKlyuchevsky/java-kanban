package Managers;

public class Node <Task> {
public Task tasks;
public Node<Task> next;
public Node<Task> prev;

public Node(Node<Task> prev, Task tasks, Node<Task> next) {
        this.tasks = tasks;
        this.next = next;
        this.prev = prev;
        }
        }
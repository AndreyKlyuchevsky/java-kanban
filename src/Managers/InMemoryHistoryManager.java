package Managers;

import Tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node<Task>> taskNode = new HashMap<>();
    private Node<Task> head;//хранит первый элемент
    private Node<Task> tail;
    private int size = 0;

    @Override
    public void add(Task task) {

        if (taskNode.size() == 0) {
            final Node<Task> newNode = new Node<>(null, task, null);
            head = newNode;
            tail= newNode;
            taskNode.put(task.getId(), newNode);
            size++;
        } else if (taskNode.containsKey(task.getId())) {
            overwritingNeighbors(taskNode.get(task.getId()));
            taskNode.remove(task.getId());
            linkLast(task);
            size++;
        } else{
            linkLast(task);
            size++;
    }
}

    private void linkLast(Task task) {
        final Node<Task> newNode;

            newNode = new Node<Task>(tail, task, null);
            taskNode.get(tail.tasks.getId()).next = newNode;
        tail = newNode;
        taskNode.put(task.getId(), newNode);
    }




    private void overwritingNeighbors (Node<Task> node){

        if (head.equals(node)){
            node.next.prev=null;
            head=node.next;
        }else {
            node.prev.next = node.next;
            node.next.prev = node.prev.next;
        }
    }



    private void removeNode(Node<Task> element) {
        if (size==1){
            head=null;
            tail=null;
        }else if (tail.equals(element)){
            tail.prev.next=null;
        }else{
            overwritingNeighbors(element);
        }

    }
@Override
    public void removeAll() {
        taskNode.clear();
        tail=null;
        head=null;
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
        if (taskNode.size() == 0) {
            return null;
        }else{
            List <Task> listTasks = new ArrayList<>();
            Node <Task> next = head;
            for (int i = 0; i < size; i++) {
                if(next!=null) {
                    listTasks.add(next.tasks);
                    next = next.next;
                }
            }
            return listTasks;
        }

    }
}

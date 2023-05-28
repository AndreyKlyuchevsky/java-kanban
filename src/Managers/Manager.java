package Managers;

import Tasks.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Manager {
    private int id = 1;
    private final HashMap<Integer, Task> taskList = new HashMap<>();
    private final HashMap<Integer, Epic> epicList = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskList = new HashMap<>();

    private int getId() {// присвоили ID, увыеличиваем ID
        return id++;
    }

    public void addTask(Task task) {// создаем простую задачу
        task.setId(getId());
        taskList.put(task.getId(), task);// добавляем задачу в общий список
    }

    public void addEpic(Epic epic) { //создаем задачу(Epic) с подзадачами
        epic.setId(getId());
        epicList.put(epic.getId(), epic);// добавляем задачу в общий список
    }

    public void addSubtask(Subtask subtask) {//создаем подзадачу
        subtask.setId(getId()); // присваиваем Id
        epicList.get(subtask.getEpicId()).setSubtaskId(subtask.getId());// передаем в epic Id Subtask
        subtaskList.put(subtask.getId(), subtask);// добавляем задачу в общий список
        updateStatus(subtask.getEpicId());// пересчитываем статус Epic

    }

    private void updateStatus(int EpicId) {// пересчитываем статус Epic
        Epic epic = epicList.get(EpicId); // из  HashMap<Integer, Epic> получаю объет Epic, ID беру из subtask
        ArrayList<Integer> idSubtask = epic.getSubtaskId(); // создаю массим ID Subtask
        int countNew=0;
        int countDone=0;

        if(idSubtask.size()==0){
            epic.setStatus(StatusTask.NEW);//если подзадач нет
        }else {
            for (Integer integer : idSubtask) {
                Subtask subtasks = subtaskList.get(integer);
                if (subtasks.getStatus() == StatusTask.NEW) {
                    countNew++;
                } else if (subtasks.getStatus() == StatusTask.DONE) {
                    countDone++;
                }
            }
            if (countNew == idSubtask.size()) {
                epic.setStatus(StatusTask.NEW);
            } else if (countDone == idSubtask.size()) {
                epic.setStatus(StatusTask.DONE);
            } else {
                epic.setStatus(StatusTask.IN_PROGRESS);
            }
        }
    }

    public void updateTask(Task task) {// обновлем простую задачу(Task)
        taskList.put(task.getId(), task);// добавляем задачу в общий список
    }

    public void updateEpic(Epic epic ) { //обновлем задачу (Epic)
        Epic updateEpics = epicList.get(epic.getId());
        updateEpics.setTitle(epic.getTitle());
        updateEpics.setDescription(epic.getDescription());
    }

    public void updateSubtask(Subtask subtask ) {//обновлем подзадачу (Subtask)
        Subtask subtaskUpdate = subtaskList.get(subtask.getId());
        subtaskUpdate.setTitle(subtask.getTitle());
        subtaskUpdate.setDescription(subtask.getDescription());
        subtaskUpdate.setStatus(subtask.getStatus());
        updateStatus (subtask.getEpicId());// пересчитываем статус Epic
    }

    public Task getTaskToId(int id) { //возвращаем объект по Id
        if (taskList.containsKey(id)) {
            return taskList.get(id);
        } else if (epicList.containsKey(id)) {
            return epicList.get(id);
        } else return subtaskList.getOrDefault(id, null);
    }

    public ArrayList<Subtask> getSubTaskToEpic(Epic epic) { //возвращаем список подзадач конкретноко Epic
        ArrayList<Subtask> ListSubTask =new ArrayList<>() ;
        if (epicList.containsKey(epic.getId())) {
            ArrayList<Integer> SubTaskId=epicList.get(epic.getId()).getSubtaskId();
            for (Integer integer : SubTaskId) {
                if(subtaskList.containsKey(integer)){
                    ListSubTask.add(subtaskList.get(integer));
                }
            }
        } return ListSubTask;
    }

    public ArrayList<Task> getTaskAll() {//возвращаем все объекты Task
        return new ArrayList<>(taskList.values());
    }

    public ArrayList<Epic> getEpicAll() {//возвращаем все объекты Epic
        return new ArrayList<> (epicList.values());
    }

    public ArrayList<Subtask> getSubtaskAll() {//возвращаем все объекты Subtask
        return new ArrayList<> (subtaskList.values());
    }



    public ArrayList<Task> getAll() {//возвращаем все объекты
        ArrayList<Task> listTask = new ArrayList<>(taskList.values());
        for (Map.Entry<Integer, Epic> integerEpicEntry : epicList.entrySet()) {
            listTask.add(integerEpicEntry.getValue());
        }
        for (Map.Entry<Integer, Subtask> integerSubtaskEntry : subtaskList.entrySet()) {
            listTask.add(integerSubtaskEntry.getValue());
        }
       return listTask;
    }

    //удаляем все задачи
    public void removeAll(){
        taskList.clear();
        epicList.clear();
        subtaskList.clear();
    }

    //удаляем все Task
    public void removeTaskAll(){
        taskList.clear();
    }

    //удаляем все Epic
    public void removeEpicAll(){
        removeSubtaskAll(); //удаляем все Subtask, так как без Epic, Subtask существовать не могут
        epicList.clear();
        subtaskList.clear();
    }

    //удаляем все Subtask
    public void removeSubtaskAll(){
        subtaskList.clear();
        for (Epic value : epicList.values()) { //после удаления всех Subtask, необходимо пересчитать статус
            value.removeSubtaskAll();
            updateStatus(value.getId());
        }
    }

    //удаляем задачу по Id
    public void removeTaskToId(int id){
        if (taskList.containsKey(id)) {
            taskList.remove(id);
        } else if (epicList.containsKey(id)) {
            for (Integer subtaskId : epicList.get(id).getSubtaskId()) {
                subtaskList.remove(subtaskId);
            }
             epicList.remove(id);
        } else if (subtaskList.containsKey(id)) {
            int idEpic = subtaskList.get(id).getEpicId();
            epicList.get(idEpic).removeSubtaskId(id);
            subtaskList.remove(id);
            updateStatus (idEpic);
        }
    }


}

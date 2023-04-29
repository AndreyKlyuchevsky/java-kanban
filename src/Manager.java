import Tasks.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Manager {
    private int id = 1;
    private HashMap<Integer, Task> taskList = new HashMap<>();
    private HashMap<Integer, Epic> epicList = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskList = new HashMap<>();

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
        statusCalculationEpic(subtask);// пересчитываем статус Epic

    }

    private void statusCalculationEpic(Subtask subtask) {// пересчитываем статус Epic
        Epic epic = epicList.get(subtask.getEpicId());
        ArrayList<Integer> idSubtask = epic.getSubtaskId();
        String status= subtaskList.get(idSubtask.get(0)).getStatus();
        for (int i = 1; i < idSubtask.size(); i++) {
            Subtask subtasks = subtaskList.get(idSubtask.get(i));
            if (subtasks.getStatus().equals("NEW") & status.equals("NEW")) {
                status = "NEW";
            } else if (subtasks.getStatus().equals("DONE") & !status.equals("DONE")) {
                status = "DONE";
            } else {
                status = "IN_PROGRESS";
            }
        }
        epic.setStatus(status);
    }

    public void updateTask(Task task,int oldIdTask) {// создаем простую задачу
        taskList.put(oldIdTask, task);// добавляем задачу в общий список
    }

    public void updateEpic(Epic epic,int oldIdEpic ) { //создаем задачу(Epic) с подзадачами
        epicList.put(oldIdEpic, epic);
    }

    public void updateSubtask(Subtask subtask,int oldIdSubtask ) {//создаем подзадачу
        subtaskList.put(oldIdSubtask, subtask);
        subtask.setId(oldIdSubtask);
        statusCalculationEpic(subtask);// пересчитываем статус Epic
    }

    public Task getId(int id) { //возвращаем объект по Id
        if (taskList.containsKey(id)) {
            return taskList.get(id);
        } else if (epicList.containsKey(id)) {
            return epicList.get(id);
        } else return subtaskList.getOrDefault(id, null);
    }

    public ArrayList<Task> printAll() {//возвращаем все объекты
        ArrayList<Task> listTask = new ArrayList<>(taskList.values());
        for (Map.Entry<Integer, Epic> integerEpicEntry : epicList.entrySet()) {
            listTask.add(integerEpicEntry.getValue());
        }
        for (Map.Entry<Integer, Subtask> integerSubtaskEntry : subtaskList.entrySet()) {
            listTask.add(integerSubtaskEntry.getValue());
        }
       return listTask;
    }

    public void removeAll(){
        taskList.clear();
        epicList.clear();
        subtaskList.clear();
    }

    public void removeId(int id){
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
        }
    }


}

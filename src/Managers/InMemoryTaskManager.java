package Managers;

import Tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    private final Map<Integer, Task> taskList = new HashMap<>();
    private final Map<Integer, Epic> epicList = new HashMap<>();
    private final Map<Integer, Subtask> subtaskList = new HashMap<>();
    private HistoryManager history = new Managers().getDefaultHistory();

    private int getId() {// присвоили ID, увыеличиваем ID
        return id++;
    }

    @Override
    public void addTask(Task task) {// создаем простую задачу
        task.setId(getId());
        taskList.put(task.getId(), task);// добавляем задачу в общий список
    }

    @Override
    public void addEpic(Epic epic) { //создаем задачу(Epic) с подзадачами
        epic.setId(getId());
        epicList.put(epic.getId(), epic);// добавляем задачу в общий список
    }

    @Override
    public void addSubTask(Subtask subtask) {//создаем подзадачу
        subtask.setId(getId()); // присваиваем Id
        epicList.get(subtask.getEpicId()).setSubtaskId(subtask.getId());// передаем в epic Id Subtask
        subtaskList.put(subtask.getId(), subtask);// добавляем задачу в общий список
        updateStatus(subtask.getEpicId());// пересчитываем статус Epic

    }


    private void updateStatus(int EpicId) {// пересчитываем статус Epic
        Epic epic = epicList.get(EpicId); // из  HashMap<Integer, Epic> получаю объет Epic, ID беру из subtask
        ArrayList<Integer> idSubtask = epic.getSubtaskId(); // создаю массим ID Subtask
        int countNew = 0;
        int countDone = 0;

        if (idSubtask.size() == 0) {
            epic.setStatus(StatusTask.NEW);//если подзадач нет
        } else {
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

    @Override
    public void updateTask(Task task) {// обновлем простую задачу(Task)
        taskList.put(task.getId(), task);// добавляем задачу в общий список
    }

    @Override
    public void updateEpic(Epic epic) { //обновлем задачу (Epic)
        Epic updateEpics = epicList.get(epic.getId());
        updateEpics.setName(epic.getName());
        updateEpics.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubTask(Subtask subtask) {//обновлем подзадачу (Subtask)
        Subtask subtaskUpdate = subtaskList.get(subtask.getId());
        subtaskUpdate.setName(subtask.getName());
        subtaskUpdate.setDescription(subtask.getDescription());
        subtaskUpdate.setStatus(subtask.getStatus());
        updateStatus(subtask.getEpicId());// пересчитываем статус Epic
    }

    @Override
    public Task getTaskById(int id) { //возвращаем объект по Id
        if (taskList.containsKey(id)) {
            history.add(taskList.get(id));
            return taskList.get(id);
        } else if (epicList.containsKey(id)) {
            history.add(epicList.get(id));
            return epicList.get(id);
        } else
            history.add(subtaskList.get(id));
        return subtaskList.getOrDefault(id, null);
    }

    @Override
    public ArrayList<Subtask> getSubTaskToEpic(Epic epic) { //возвращаем список подзадач конкретноко Epic
        ArrayList<Subtask> ListSubTask = new ArrayList<>();
        if (epicList.containsKey(epic.getId())) {
            ArrayList<Integer> SubTaskId = epicList.get(epic.getId()).getSubtaskId();
            for (Integer integer : SubTaskId) {
                if (subtaskList.containsKey(integer)) {
                    ListSubTask.add(subtaskList.get(integer));
                }
            }
        }
        return ListSubTask;
    }

    @Override
    public ArrayList<Task> getTaskAll() {//возвращаем все объекты Task

        return new ArrayList<>(taskList.values());
    }

    @Override
    public ArrayList<Epic> getEpicAll() {//возвращаем все объекты Epic
        return new ArrayList<>(epicList.values());
    }

    @Override
    public ArrayList<Subtask> getSubTaskAll() {//возвращаем все объекты Subtask
        return new ArrayList<>(subtaskList.values());
    }

    @Override
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

    @Override
    //удаляем все задачи
    public void removeAll() {
        taskList.clear();
        epicList.clear();
        subtaskList.clear();
    }

    @Override
    //удаляем все Task
    public void removeTaskAll() {
        taskList.clear();
    }

    @Override
    //удаляем все Epic
    public void removeEpicAll() {
        removeSubTaskAll(); //удаляем все Subtask, так как без Epic, Subtask существовать не могут
        epicList.clear();
        subtaskList.clear();
    }

    @Override
    //удаляем все Subtask
    public void removeSubTaskAll() {
        subtaskList.clear();
        for (Epic value : epicList.values()) { //после удаления всех Subtask, необходимо пересчитать статус
            value.removeSubtaskAll();
            updateStatus(value.getId());
        }
    }

    @Override
    //удаляем задачу по Id
    public void removeTaskToId(int id) {
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
            updateStatus(idEpic);
        }
    }

    //возвращает инсторию просмотров
    public ArrayList<Task> getHistory() {
        return history.getHistory();
    }

}

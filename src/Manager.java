import java.util.ArrayList;
import java.util.HashMap;


public class Manager {
    private int id = 1;
    protected HashMap<Integer, Object> taskList = new HashMap<>();
    protected HashMap<String, ArrayList<Integer>> arrayTaskTypes = new HashMap<>();

    public Manager() {
        this.arrayTaskTypes.put("Task", new ArrayList<>());
        this.arrayTaskTypes.put("Epic", new ArrayList<>());
        this.arrayTaskTypes.put("Subtask", new ArrayList<>());
    }

    private void idAssignment() { //присвоили ID
        id++;// увыеличиваем ID
    }

    private int getId() {// создаем простую задачу
        return id;
    }


    public HashMap<Integer, Object> getTaskList() {// возвращате список задач
        return taskList;
    }

    private void addTaskList(Integer id, Object task) {// добавляем задачу в общий список
        taskList.put(id, task);
        addArrayTaskTypes(id, task);
    }

    private void addArrayTaskTypes(Integer id, Object task) {// добавляем задачу в список по типу задачи

        if (task.getClass() == Task.class) {
            arrayTaskTypes.get("Task").add(id);
        } else if (task.getClass() == Epic.class) {
            arrayTaskTypes.get("Epic").add(id);
        } else if (task.getClass() == Subtask.class) {
            arrayTaskTypes.get("Subtask").add(id);
        }
    }


    private Object returnTask(Integer id) {// создаем простую задачу
        return this.taskList.get(id);
    }

    private String statusCalculationEpic(Epic epic) {// пересчитываем статус Epic
        String status=epic.subtask.get(0).status;;
        for (int i = 0; i < epic.subtask.size(); i++) {
            Subtask subtask = epic.subtask.get(i);
            if (subtask.status.equals("NEW") & status.equals("NEW")) {
                status = "NEW";
            } else if (subtask.status.equals("DONE") & !status.equals("DONE")) {
                status = "DONE";
            } else {
                status = "IN_PROGRESS";
            }
        }
        return status;
    }

    public void addTask(Task task) {// создаем простую задачу
        task.id=getId();
        task.status="NEW";
        addTaskList(task.id, task);;// добавляем задачу в общий список
        idAssignment();
    }

    public void addEpic(Epic epic) { //создаем задачу(Epic) с подзадачами
        epic.id=getId();
        epic.status="NEW";
        addTaskList(epic.id, epic);
        idAssignment();
    }

    public void addSubtask(Subtask subtask,Epic epic ) {//создаем подзадачу
        subtask.id= getId();
        subtask.status= "NEW";
        subtask.epic=epic;
        epic.setSubtask(subtask);// добавляем в Epic подзадачу
        addTaskList(subtask.id, subtask);// добавляем задачу в общий список
        epic.status=statusCalculationEpic(epic);// пересчитываем статус Epic
        idAssignment();
    }


    public void updateTask(String title, String description, String status,int id) {// создаем простую задачу
        Task tasks = new Task(title, description, id, status);
        addTaskList(getId(), tasks);// добавляем задачу в общий список

    }

    public void updateEpic(String title, String description, String status,int id) { //создаем задачу(Epic) с подзадачами
        Epic epic = new Epic(title, description, id, "NEW");
        addTaskList(getId(), epic);
    }

    public void updateSubtask(String title, String description,  String status,Epic epic, Subtask subtask ) {//создаем подзадачу
        subtask.title=title;
        subtask.description=description;
        subtask.status=status;
        epic.status=statusCalculationEpic(epic);// пересчитываем статус Epic
    }

    public void removeAll() {//удаляем все задачи
        if (taskList.size() > 0) {
            taskList.clear();
        }
        if (arrayTaskTypes.size() > 0) {
            arrayTaskTypes.clear();
        }
    }

    public void removeID(int id) {//удаляем по ID задачи
        if (taskList.containsKey(id)) {
            Object tasks=taskList.get(id);
            if (tasks.getClass() == Task.class) {
                taskList.remove(id);
                arrayTaskTypes.get("Subtask").remove(id);
            } else if (tasks.getClass() == Epic.class) {
                for (int i = 0; i < ((Epic) tasks).subtask.size(); i++) {
                    int idSubtask=((Epic) tasks).subtask.get(i).id;
                    for (int i1 = 0; i1 < arrayTaskTypes.get("Subtask").size(); i1++) {
                        if(arrayTaskTypes.get("Subtask").get(i1)==idSubtask){
                            arrayTaskTypes.get("Subtask").remove(i1);
                        }
                    }

                    taskList.remove(idSubtask);
                }
                ((Epic) tasks).subtask.clear();
                taskList.remove(id);
                arrayTaskTypes.get("Epic").add(id);
            } else if (tasks.getClass() == Subtask.class) {
                taskList.remove(id);
                arrayTaskTypes.get("Subtask").remove(id);
            }
        }
    }

    public Object GetByID(int id) { //Получение по идентификатору
        if (taskList.containsKey(id)) {
            return taskList.get(id);
        }
        return null;
    }

    public HashMap<Integer, Object> printAll() {
        return taskList;
    }

    public ArrayList<Subtask> EpicGetSubtask(int id) {
        if (taskList.containsKey(id)) {
            Epic epic = (Epic) taskList.get(id);
            return epic.subtask;
        }
        return null;
    }


}

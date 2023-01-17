import exception.TaskNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TaskService {

private static final Map<Integer, Task> TASKS=new HashMap<>();

private TaskService(){

}
public static void add(Task task){
    TASKS.put(task.getId(),task);
}
public static Collection<Task>getTasksByDay(LocalDate day) {
    Collection<Task> tasksByDay = new ArrayList<>();
    Collection<Task> allTasks = TASKS.values();
    for (Task task : allTasks) {
        LocalDateTime currentDateTime = task.getDateTime();
        LocalDateTime nextDateTimme = task.getRepeatability().nextTime(currentDateTime);
        if (nextDateTimme == null || currentDateTime.toLocalDate().equals(day)) {
            tasksByDay.add(task);
            continue;
        }
        do{
           if (nextDateTimme.toLocalDate().equals(day)){
               tasksByDay.add(task);
               break;
           }
            nextDateTimme = task.getRepeatability().nextTime(nextDateTimme);
        } while (nextDateTimme.toLocalDate(). isBefore(day));
    }
    return tasksByDay;
    }
    public static void removeById(int id) {
        if (!TASKS.remove(id)==null){
            throw new TaskNotFoundException(id);
        }

    }
}

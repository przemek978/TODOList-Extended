package com.example.todolistextended;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static final TaskStorage taskStorage= new TaskStorage();
    private final List<Task> tasks;
    public static TaskStorage getInstance(){
        return taskStorage;
    }
    private TaskStorage(){
        tasks= new ArrayList<>();
        for(int i=1;i<=5;i++){
            Task task=new Task();
            task.setName("Pilne zadanie nr "+i);
            task.setDone(i%3==0);
            tasks.add(task);
            if(i%3 == 0){
                task.setCategory(Category.STUDIES);
            }
            else{
                task.setCategory(Category.HOME);
            }
        }

    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(UUID taskId) {
        for(Task task:tasks){
            if(task.getId().equals(taskId)) {
                return task;
            }
        }
        return null;
    }
    public void addTask(Task task){
        tasks.add(task);
    }


}

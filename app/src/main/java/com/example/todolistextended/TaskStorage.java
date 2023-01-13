package com.example.todolistextended;
import android.app.Application;

import com.example.todolistextended.DB.TaskDao;
import com.example.todolistextended.DB.TaskDatabase;

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
//        TaskDatabase database=TaskDatabase.getDatabase(application);
//        TaskDao dao= database.taskDao();
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

    public Task getTask(int taskId) {
        for(Task task:tasks){
            if(task.getId()==taskId) {
                return task;
            }
        }
        return null;
    }
    public void addTask(Task task){
        tasks.add(task);
    }


    public void deleteTask(Task task) {
//        for(Task task1:tasks){
//            if(task.getId()==task1.getId())
//            {
//                tasks.remove(tasks.indexOf(task));
//            }
//        }
        for(int i=0;i<tasks.size();i++){
            if(task.getId()==tasks.get(i).getId()){
                tasks.remove(i);
            }
        }
    }
}

package com.example.todolistextended.DB;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.todolistextended.Task;

import java.util.List;

public class TaskRepository {
    private final TaskDao taskDao;
    private final LiveData<List<Task>> tasks;

    TaskRepository(Application application){
        TaskDatabase database=TaskDatabase.getDatabase(application);
        taskDao= database.taskDao();
        tasks=taskDao.findAll();
    }

    LiveData<List<Task>> findAllBooks(){
        return tasks;
    }

    void insert(Task task){
        TaskDatabase.databaseWriteExecutor.execute(()->taskDao.insert(task));
    }

    void update(Task task){
        TaskDatabase.databaseWriteExecutor.execute(()->taskDao.update(task));
    }

    void delete(Task task){
        TaskDatabase.databaseWriteExecutor.execute(()->taskDao.delete(task));
    }
}

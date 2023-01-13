package com.example.todolistextended.DB;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todolistextended.Task;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository taskRepository;
    private final LiveData<List<Task>> tasks;

    public TaskViewModel(@NonNull Application application){
        super(application);
        taskRepository=new TaskRepository(application);
        tasks=taskRepository.findAllBooks();
    }

    public LiveData<List<Task>> findAll() { return tasks;}

    public void insert (Task task){ taskRepository.insert(task);}

    public void update (Task task) {taskRepository.update(task);}

    public void delete (Task task) { taskRepository.delete(task);}


}

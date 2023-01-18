package com.example.todolistextended.DB;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todolistextended.Category;
import com.example.todolistextended.Task;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities={Task.class},version=1,exportSchema=false)
public abstract class TaskDatabase extends RoomDatabase {
    private static TaskDatabase databaseInstance;
    private static final List<String> days = Arrays.asList("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday");

    static final ExecutorService databaseWriteExecutor= Executors.newSingleThreadExecutor();

    public abstract TaskDao taskDao();

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                TaskDao dao = databaseInstance.taskDao();
                dao.deleteAll();
                for(int i=1;i<=5;i++) {
                    Task task = new Task();
                    task.setDay(days.get(i%7));
                    task.setName("Nowe zadanie"+i );
                    task.setDone(false);
                    dao.insert(task);
                }

            });

        }
    };
    public static TaskDatabase getDatabase(final Context context){
        if(databaseInstance==null){
            databaseInstance= Room.databaseBuilder(context.getApplicationContext(),TaskDatabase.class,"task_database").addCallback(sRoomDatabaseCallback).build();
            //databaseInstance= Room.databaseBuilder(context.getApplicationContext(),TaskDatabase.class,"task_database").build();
        }
        return databaseInstance;
    }
}
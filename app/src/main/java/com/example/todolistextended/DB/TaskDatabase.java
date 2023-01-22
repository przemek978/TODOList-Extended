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
import java.util.Date;
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
                int Y=2023,M=1,D=20,H=15,m=0;
                for(int i=1;i<=50;i++) {
                    Task task = new Task();
                    task.setDay(days.get(i%7));
                    task.setName("Nowe zadanie "+i );
                    task.setDescription("Jakis opis zadania nr "+i);
                    task.setDone(false);
                    task.setDate((new Date(Y,M,D,H,m)));
                    dao.insert(task);
                    D=D+3;
                    if(D>31){
                        D=1;
                        M++;
                    }
                    if(i%5==0){
                        H=(H+2)%25;
                        m=(m+30)%61;
                    }
                }
            });
        }
    };
    public static TaskDatabase getDatabase(final Context context){
        if(databaseInstance==null){
            databaseInstance= Room.databaseBuilder(context.getApplicationContext(),TaskDatabase.class,"task_database").addCallback(sRoomDatabaseCallback).build();
        }
        return databaseInstance;
    }
}
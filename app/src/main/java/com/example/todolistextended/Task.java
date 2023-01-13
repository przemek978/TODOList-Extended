package com.example.todolistextended;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.UUID;

@Entity(tableName="task")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    @TypeConverters(DateConverter.class)
    private Date date;
    private boolean done;
    Category category;

    public Task(){
        //id=UUID.randomUUID();
        date = new Date();
        category=Category.HOME;
    }
//    public Task(String name){
//        //id=UUID.randomUUID();
//        this.name=name;
//        date = new Date();
//        category=Category.HOME;
//    }
    public Category getCategory(){
        return category;
    }
    public void setCategory(Category c){
        category=c;
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date d){
        date=d;
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        this.name=s;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean isChecked) {
        this.done=isChecked;
    }


}

package com.example.todolistextended;

import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id;
    private String name;
    private Date date;
    private boolean done;
    Category category;

    public Task(){
        id=UUID.randomUUID();
        date = new Date();
        category=Category.HOME;
    }
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id=id;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean isChecked) {
        this.done=isChecked;
    }


}

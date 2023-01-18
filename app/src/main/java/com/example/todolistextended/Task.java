package com.example.todolistextended;

import android.location.Location;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName="task")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String day;
    private String name;
    @TypeConverters(DateConverter.class)
    private Date date;
    private boolean done;
    //private Location location;
    private double Latitude,Longitude;
    Category category;

    public Task(){
        //id=UUID.randomUUID();
        date = new Date();
        category=Category.HOME;
    }
    public Task(Location location){
        //id=UUID.randomUUID();
        date = new Date();
        category=Category.HOME;
        Latitude=location.getLatitude();
        Longitude=location.getLongitude();
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
//    public double getLatitude(){
//        return this.Latitude;
//    }
//    public double getLongitude(){
//        return this.Longitude;
//    }
    public void setDate(Date d){
        date=d;
    }
    public void setLocation(double Lat,double Long){
        this.Latitude=Lat;
        this.Longitude=Long;
        //this.location=location;
    }
    public void setLatitude(double Lat){
        this.Latitude=Lat;
    }
    public void setLongitude(double Long){
        this.Longitude=Long;
    }
    public double getLatitude(){
        return this.Latitude;
    }
    public double getLongitude(){
        return this.Longitude;
    }
    public String getDay() {
        return day;
    }

    public void setDay(String newDay){
        day=newDay;
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

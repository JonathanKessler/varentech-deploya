package com.varentech.entities;

import java.util.Calendar;

/**
 * Created by ParksW on 5/26/2016.
 */
public class Entries {
    private String name;
    private Calendar calendar = Calendar.getInstance();
    //TODO: These entries must be changed once we find out the data type we will be handling.
    private Object output = null;
    private Object jarFile = null;
    private Object hashValue = output.hashCode();


    //Default Constructor
    public Entries(){

    }

    //Constructor
    public Entries(String name, Calendar calendar, Object output, Object jarFile, Object hashValue){
        this.name = name;
        this.calendar = calendar;
        this.output = output;
        this.jarFile = jarFile;
        this.hashValue = hashValue;
    }

    //Setters
    public void setName(String name){
        this.name = name;
    }
    public void setCalendar(Calendar calendar){
        this.calendar = calendar;
    }
    public void setOutput(Object output){
        this.output = output;
    }
    public void setJarFile(Object jarFile){
        this.jarFile = jarFile;
    }
    public void setHashValue(Object hashValue){
        this.hashValue = hashValue;
    }

    //Getters
    public String getName(){
        return name;
    }
    public Calendar getCalendar(){
        return calendar;
    }
    public Object getOutput(){
        return output;
    }
    public Object getJarFile(){
        return jarFile;
    }
    public Object getHashValue(){
        return hashValue;
    }


}

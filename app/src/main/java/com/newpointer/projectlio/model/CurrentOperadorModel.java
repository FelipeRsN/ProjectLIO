package com.newpointer.projectlio.model;

/**
 * Created by felip on 25/08/2016.
 */
public class CurrentOperadorModel {
    private int id;
    private String name;

    public CurrentOperadorModel(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

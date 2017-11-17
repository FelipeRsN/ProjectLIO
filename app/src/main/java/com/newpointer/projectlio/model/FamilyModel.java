package com.newpointer.projectlio.model;

/**
 * Created by FelipeRsN on 6/28/16.
 */
public class FamilyModel {
    private int id;
    private String name;

    public FamilyModel(int id, String name){
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

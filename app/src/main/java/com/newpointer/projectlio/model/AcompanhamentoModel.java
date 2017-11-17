package com.newpointer.projectlio.model;

/**
 * Created by FelipeRsN on 7/6/16.
 */
public class AcompanhamentoModel {
    private int id;
    private String name;
    private int grupo;

    public AcompanhamentoModel(int id, String name, int grupo){
        this.id = id;
        this.name = name;
        this.grupo = grupo;
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

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }
}

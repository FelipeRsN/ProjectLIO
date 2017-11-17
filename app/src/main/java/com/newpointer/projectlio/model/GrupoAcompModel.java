package com.newpointer.projectlio.model;

/**
 * Created by FelipeRsN on 7/6/16.
 */
public class GrupoAcompModel {
    private int id;
    private String name;
    private int selecao;

    public GrupoAcompModel(int id, String name, int selecao){
        this.id = id;
        this.name = name;
        this.selecao = selecao;
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

    public int getSelecao() {
        return selecao;
    }

    public void setSelecao(int selecao) {
        this.selecao = selecao;
    }
}

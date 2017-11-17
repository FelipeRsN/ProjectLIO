package com.newpointer.projectlio.model;

/**
 * Created by FelipeRsN on 7/6/16.
 */
public class GroupAcomp_ListView_Model {
    private int id;
    private String desc;
    private int selecao;
    private int mode;

    public GroupAcomp_ListView_Model(int id, String desc, int selecao, int mode){
        this.id = id;
        this.desc = desc;
        this.selecao = selecao;
        this.mode = mode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSelecao() {
        return selecao;
    }

    public void setSelecao(int selecao) {
        this.selecao = selecao;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}


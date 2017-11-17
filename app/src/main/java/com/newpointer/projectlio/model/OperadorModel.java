package com.newpointer.projectlio.model;

/**
 * Created by felip on 27/07/2016.
 */
public class OperadorModel {
    private int id;
    private String name;
    private int fliniciar;
    private String password;
    private int flprimeiro;
    private int flperfil;
    private int cdperfil;

    public OperadorModel(int id, String name, int fliniciar, int flprimeiro, String password, int flperfil, int cdperfil){
        this.id = id;
        this.name = name;
        this.fliniciar = fliniciar;
        this.password = password;
        this.flprimeiro = flprimeiro;
        this.flperfil = flperfil;
        this.cdperfil = cdperfil;
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

    public int getFliniciar() {
        return fliniciar;
    }

    public void setFliniciar(int fliniciar) {
        this.fliniciar = fliniciar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFlprimeiro() {
        return flprimeiro;
    }

    public void setFlprimeiro(int flprimeiro) {
        this.flprimeiro = flprimeiro;
    }

    public int getFlperfil() {
        return flperfil;
    }

    public void setFlperfil(int flperfil) {
        this.flperfil = flperfil;
    }

    public int getCdperfil() {
        return cdperfil;
    }

    public void setCdperfil(int cdperfil) {
        this.cdperfil = cdperfil;
    }
}

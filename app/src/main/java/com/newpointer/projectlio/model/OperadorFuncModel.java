package com.newpointer.projectlio.model;

/**
 * Created by felip on 04/08/2016.
 */
public class OperadorFuncModel {
    private int cd;
    private String func;

    public OperadorFuncModel (int cd, String func){
        this.cd = cd;
        this.func = func;
    }

    public int getCd() {
        return cd;
    }

    public void setCd(int cd) {
        this.cd = cd;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }
}

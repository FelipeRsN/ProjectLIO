package com.newpointer.projectlio.model;

/**
 * Created by FelipeRsN on 6/28/16.
 */
public class ProductModel {
    private String id;
    private String name;
    private int fam;
    private String unit;
    private int fl_imp;
    private int cd_imp;
    private int fl_acomp;
    private String acomp;

    public ProductModel(String id, String name, int fam, String unit, int fl_imp, int cd_imp, int fl_acomp, String acomp){
        this.id = id;
        this.name = name;
        this.fam = fam;
        this.unit = unit;
        this.fl_imp = fl_imp;
        this.cd_imp = cd_imp;
        this.acomp = acomp;
        this.fl_acomp = fl_acomp;
    }
    public ProductModel(){

    }

    public int getCd_imp() {
        return cd_imp;
    }

    public void setCd_imp(int cd_imp) {
        this.cd_imp = cd_imp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFam() {
        return fam;
    }

    public void setFam(int fam) {
        this.fam = fam;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getFl_imp() {
        return fl_imp;
    }

    public void setFl_imp(int fl_imp) {
        this.fl_imp = fl_imp;
    }

    public int getFl_acomp() {
        return fl_acomp;
    }

    public void setFl_acomp(int fl_acomp) {
        this.fl_acomp = fl_acomp;
    }

    public String getAcomp() {
        return acomp;
    }

    public void setAcomp(String acomp) {
        this.acomp = acomp;
    }
}

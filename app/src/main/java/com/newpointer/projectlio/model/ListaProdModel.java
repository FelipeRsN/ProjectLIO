package com.newpointer.projectlio.model;

/**
 * Created by felip on 02/09/2016.
 */
public class ListaProdModel {
    private long id;
    private String name;
    private String quant;
    private String val;
    private double tot;
    private int taxa;
    private double indice;
    private String UN;

    public ListaProdModel(long id, String name, String qtd, String val, double tot, int taxa, double indice, String UN){
        this.id = id;
        this.name = name;
        this.quant = qtd;
        this.val = val;
        this.tot = tot;
        this.taxa = taxa;
        this.indice = indice;
        this.UN = UN;
    }

    public String getUN() {
        return UN;
    }

    public void setUN(String UN) {
        this.UN = UN;
    }

    public double getIndice() {
        return indice;
    }

    public void setIndice(double indice) {
        this.indice = indice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuant() {
        return quant;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public double getTot() {
        return tot;
    }

    public void setTot(double tot) {
        this.tot = tot;
    }

    public int getTaxa() {
        return taxa;
    }

    public void setTaxa(int taxa) {
        this.taxa = taxa;
    }
}

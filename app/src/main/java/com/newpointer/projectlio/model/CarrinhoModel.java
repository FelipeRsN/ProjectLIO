package com.newpointer.projectlio.model;

/**
 * Created by felip on 13/07/2016.
 */
public class CarrinhoModel {
    private int id_carrinho;
    private String id_prod;
    private String name_prod;
    private int qtd_prod;
    private String acomp_prod;
    private String obs_prod;

    public CarrinhoModel(int id_c, String id_p, String name, int qtd, String acomp, String obs){
        this.id_carrinho = id_c;
        this.id_prod = id_p;
        this.name_prod = name;
        this.qtd_prod = qtd;
        this.acomp_prod = acomp;
        this.obs_prod = obs;
    }

    public int getId_carrinho() {
        return id_carrinho;
    }

    public void setId_carrinho(int id_carrinho) {
        this.id_carrinho = id_carrinho;
    }

    public String getId_prod() {
        return id_prod;
    }

    public void setId_prod(String id_prod) {
        this.id_prod = id_prod;
    }

    public String getName_prod() {
        return name_prod;
    }

    public void setName_prod(String name_prod) {
        this.name_prod = name_prod;
    }

    public int getQtd_prod() {
        return qtd_prod;
    }

    public void setQtd_prod(int qtd_prod) {
        this.qtd_prod = qtd_prod;
    }

    public String getAcomp_prod() {
        return acomp_prod;
    }

    public void setAcomp_prod(String acomp_prod) {
        this.acomp_prod = acomp_prod;
    }

    public String getObs_prod() {
        return obs_prod;
    }

    public void setObs_prod(String obs_prod) {
        this.obs_prod = obs_prod;
    }
}

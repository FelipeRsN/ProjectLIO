package com.newpointer.projectlio.model;

/**
 * Created by FelipeRsN on 6/24/16.
 */
public class ConfigModel {
    private String string_bd;
    private String estacao;
    private Double taxa;
    private int digito_verificador;
    private int pergunta_mesa;
    private String titulo_loja;
    private String nmin_mesa;
    private String nmax_mesa;
    private String dbbkp_date;
    private int phone_selection;
    private int product_selection;
    private int preconta;
    private int conferencia;
    private int lio;

    public ConfigModel(String string_bd, String estacao, Double taxa, int digito_verificador, int pergunta_mesa, String titulo_loja, String nmin_mesa, String nmax_mesa, String dbbkp_date, int phone_selection, int product_selection, int preconta, int conferencia, int lio){
        this.string_bd = string_bd;
        this.estacao = estacao;
        this.taxa = taxa;
        this.digito_verificador = digito_verificador;
        this.pergunta_mesa = pergunta_mesa;
        this.titulo_loja = titulo_loja;
        this.nmin_mesa = nmin_mesa;
        this.nmax_mesa = nmax_mesa;
        this.dbbkp_date = dbbkp_date;
        this.phone_selection = phone_selection;
        this.product_selection = product_selection;
        this.preconta = preconta;
        this.conferencia = conferencia;
        this.lio = lio;
    }

    public int getLio() {
        return lio;
    }

    public void setLio(int lio) {
        this.lio = lio;
    }

    public int getPreconta() {
        return preconta;
    }

    public void setPreconta(int preconta) {
        this.preconta = preconta;
    }

    public int getConferencia() {
        return conferencia;
    }

    public void setConferencia(int conferencia) {
        this.conferencia = conferencia;
    }

    public String getString_bd() {
        return string_bd;
    }

    public void setString_bd(String string_bd) {
        this.string_bd = string_bd;
    }

    public String getEstacao() {
        return estacao;
    }

    public void setEstacao(String estacao) {
        this.estacao = estacao;
    }

    public Double getTaxa() {
        return taxa;
    }

    public void setTaxa(Double taxa) {
        this.taxa = taxa;
    }

    public int getDigito_verificador() {
        return digito_verificador;
    }

    public void setDigito_verificador(int digito_verificador) {
        this.digito_verificador = digito_verificador;
    }

    public int getPergunta_mesa() {
        return pergunta_mesa;
    }

    public void setPergunta_mesa(int pergunta_mesa) {
        this.pergunta_mesa = pergunta_mesa;
    }

    public String getTitulo_loja() {
        return titulo_loja;
    }

    public void setTitulo_loja(String titulo_loja) {
        this.titulo_loja = titulo_loja;
    }

    public String getNmin_mesa() {
        return nmin_mesa;
    }

    public void setNmin_mesa(String nmin_mesa) {
        this.nmin_mesa = nmin_mesa;
    }

    public String getNmax_mesa() {
        return nmax_mesa;
    }

    public void setNmax_mesa(String nmax_mesa) {
        this.nmax_mesa = nmax_mesa;
    }

    public String getDbbkp_date() {
        return dbbkp_date;
    }

    public void setDbbkp_date(String dbbkp_date) {
        this.dbbkp_date = dbbkp_date;
    }

    public int getPhone_selection() {
        return phone_selection;
    }

    public void setPhone_selection(int phone_selection) {
        this.phone_selection = phone_selection;
    }

    public int getProduct_selection() {
        return product_selection;
    }

    public void setProduct_selection(int product_selection) {
        this.product_selection = product_selection;
    }
}

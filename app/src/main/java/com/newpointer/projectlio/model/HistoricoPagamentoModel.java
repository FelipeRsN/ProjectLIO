package com.newpointer.projectlio.model;

/**
 * Created by Felipe Silveira on 10/18/2017.
 */

public class HistoricoPagamentoModel {

    public static final String TIPO_PGTO_DEBIT = "DEBITO";
    public static final String TIPO_PGTO_CREDIT = "CREDITO";
    public static final String TIPO_PGTO_VALE = "VISA VALE";

    public static final int FORMA_PGTO_MONEY = 1;
    public static final int FORMA_PGTO_CARD = 2;


    private int formaDePagamento;
    private String valor;
    private String tipoDePagamento;
    private String nsu;
    private String bin;
    private String operadora;

    public HistoricoPagamentoModel(String nsu, String tipoDePagamento, int formaDePagamento, String valor, String bin, String operadora) {
        this.formaDePagamento = formaDePagamento;
        this.valor = valor;
        this.nsu = nsu;
        this.tipoDePagamento = tipoDePagamento;
        this.operadora = operadora;
        this.bin = bin;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getOperadora() {
        return operadora;
    }

    public void setOperadora(String operadora) {
        this.operadora = operadora;
    }

    public int getFormaDePagamento() {
        return formaDePagamento;
    }

    public void setFormaDePagamento(int formaDePagamento) {
        this.formaDePagamento = formaDePagamento;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipoDePagamento() {
        return tipoDePagamento;
    }

    public void setTipoDePagamento(String tipoDePagamento) {
        this.tipoDePagamento = tipoDePagamento;
    }

    public String getNsu() {
        return nsu;
    }

    public void setNsu(String nsu) {
        this.nsu = nsu;
    }
}

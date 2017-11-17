package com.newpointer.projectlio.model;

import java.util.List;

/**
 * Created by Felipe Silveira on 9/4/2017.
 */

public class ConferenciaModel {
    private String mesa;
    private List<ConferenciaBean> itens;

    public ConferenciaModel(String mesa, List<ConferenciaBean> itens) {
        this.mesa = mesa;
        this.itens = itens;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public List<ConferenciaBean> getItens() {
        return itens;
    }

    public void setItens(List<ConferenciaBean> itens) {
        this.itens = itens;
    }

    public static class ConferenciaBean{
        private String produto;
        private String quantidade;

        public ConferenciaBean(String produto, String quantidade) {
            this.produto = produto;
            this.quantidade = quantidade;
        }

        public String getProduto() {
            return produto;
        }

        public void setProduto(String produto) {
            this.produto = produto;
        }

        public String getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(String quantidade) {
            this.quantidade = quantidade;
        }
    }
}

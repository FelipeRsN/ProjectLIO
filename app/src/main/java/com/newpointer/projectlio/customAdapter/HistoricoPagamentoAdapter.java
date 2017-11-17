package com.newpointer.projectlio.customAdapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.model.HistoricoPagamentoModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


/**
 * Created by felipersn on 10/05/17.
 */

public class HistoricoPagamentoAdapter extends RecyclerView.Adapter<HistoricoPagamentoAdapter.ViewHolder> {
    List<HistoricoPagamentoModel> list;

    private Context context;
    private Activity activity;

    public static final int FORMA_PGTO_DINHEIRO = 1;
    public static final int FORMA_PGTO_CARTAO = 2;

    public static final String TIPO_PGTO_CARTAO_CRED = "CREDITO";
    public static final String TIPO_PGTO_CARTAO_DEBT = "DEBITO";
    public static final String TIPO_PGTO_CARTAO_VALE = "VISA VALE";

    public HistoricoPagamentoAdapter(List<HistoricoPagamentoModel> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;

    }

    @Override
    public HistoricoPagamentoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_historico_pagamento, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final HistoricoPagamentoModel data = list.get(position);
        NumberFormat formatarFloat = new DecimalFormat("0.00");
        switch (data.getFormaDePagamento()){
            case FORMA_PGTO_DINHEIRO: // Dinheiro
                holder.root.setBackgroundColor(ContextCompat.getColor(context, R.color.historyDinheiro));
                holder.formaPgto.setText("Dinheiro");
                holder.value.setText("R$ "+formatarFloat.format(Double.parseDouble(data.getValor())));
                break;
            case FORMA_PGTO_CARTAO: // Cartao
                holder.root.setBackgroundColor(ContextCompat.getColor(context, R.color.historyCartao));
                holder.value.setText("R$ "+formatarFloat.format(Double.parseDouble(data.getValor())));
                switch (data.getTipoDePagamento()){
                    case TIPO_PGTO_CARTAO_CRED:
                        holder.formaPgto.setText("Cartão (CREDITO)");
                        break;
                    case TIPO_PGTO_CARTAO_DEBT:
                        holder.formaPgto.setText("Cartão (DEBITO)");
                        break;
                    case TIPO_PGTO_CARTAO_VALE:
                        holder.formaPgto.setText("Cartão (VISA VALE)");
                        break;
                }


                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView formaPgto;
        TextView value;
        LinearLayout root;

        public ViewHolder(View itemView) {
            super(itemView);
            formaPgto = (TextView) itemView.findViewById(R.id.formaPgto);
            value = (TextView) itemView.findViewById(R.id.value);
            root = (LinearLayout) itemView.findViewById(R.id.root);
        }
    }
}

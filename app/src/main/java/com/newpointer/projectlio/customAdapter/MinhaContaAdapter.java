package com.newpointer.projectlio.customAdapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.model.ListaProdModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;


/**
 * Created by felipersn on 10/05/17.
 */

public class MinhaContaAdapter extends RecyclerView.Adapter<MinhaContaAdapter.ViewHolder> {
    List<ListaProdModel> list;

    private Context context;
    private Activity activity;

    public MinhaContaAdapter(List<ListaProdModel> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;

    }

    @Override
    public MinhaContaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_preconta, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ListaProdModel data = list.get(position);

        holder.name.setText(data.getName());

        holder.qtd.setText(data.getQuant()+" "+data.getUN());

        NumberFormat formatarFloat = new DecimalFormat("0.00");

        holder.value.setText("R$ "+formatarFloat.format(Double.parseDouble(data.getVal())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView qtd;
        TextView value;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            qtd = (TextView) itemView.findViewById(R.id.qtd);
            value = (TextView) itemView.findViewById(R.id.value);
        }
    }
}

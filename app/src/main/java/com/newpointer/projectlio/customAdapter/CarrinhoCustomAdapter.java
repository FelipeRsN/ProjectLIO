package com.newpointer.projectlio.customAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.activity.CarinhoActivity;
import com.newpointer.projectlio.activity.MainActivity;
import com.newpointer.projectlio.connection.DBLiteConnection;
import com.newpointer.projectlio.model.CarrinhoModel;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.List;


/**
 * Created by felip on 14/07/2016.
 */
public class CarrinhoCustomAdapter extends BaseAdapter {
    Activity activity;
    Context context;
    BadgeView badge;

    protected List<CarrinhoModel> listCars;
    LayoutInflater inflater;

    public CarrinhoCustomAdapter(Activity activity, Context context, List<CarrinhoModel> listCars) {
        this.activity = activity;
        this.listCars = listCars;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return listCars.size();
    }

    @Override
    public Object getItem(int i) {
        return listCars.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listCars.get(i).getId_carrinho();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        badge = MainActivity.badge;
        if (view == null) {
            holder = new ViewHolder();
            view = this.inflater.inflate(R.layout.layout_carrinho, viewGroup, false);

            holder.nome = (TextView) view.findViewById(R.id.tv_carrinho_nome);

            holder.cod = (TextView) view.findViewById(R.id.tv_carrinho_codigo);

            holder.del = (Button) view.findViewById(R.id.bt_carrinho_del);

            holder.qtd = (TextView) view.findViewById(R.id.tv_carrinho_qtd);

            holder.aco = (TextView) view.findViewById(R.id.tv_carrinho_aco);

            holder.obs = (TextView) view.findViewById(R.id.tv_carrinho_obs);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final CarrinhoModel item = listCars.get(i);

        holder.nome.setText(item.getName_prod());
        String stringCod = String.format("%14s", item.getId_prod()).replace(' ', '0');
        holder.cod.setText(stringCod);
        holder.qtd.setText("Quantidade: "+item.getQtd_prod());
        holder.aco.setText(item.getAcomp_prod());
        holder.obs.setText("Observação: "+item.getObs_prod());
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBar pb = (ProgressBar) activity.findViewById(R.id.pb_carrinho_wait);
                pb.setVisibility(View.VISIBLE);
                DBLiteConnection dbl = DBLiteConnection.Companion.getInstance(context);
                dbl.deleteProdCarrinho(item.getId_carrinho());
                CarinhoActivity.carrinho.remove(i);
                int b = Integer.parseInt(badge.getText().toString());
                b--;
                badge.setText(""+b);
                if(dbl.haveProdInCarrinho()){
                    ListView lv = (ListView) activity.findViewById(R.id.lv_carrinho);
                    List<CarrinhoModel> list = dbl.selectCarrinho();
                    TextView qt = (TextView) activity.findViewById(R.id.tv_carrinho_nitens);
                    qt.setText("Itens: "+list.size());
                    CarrinhoCustomAdapter cca = new CarrinhoCustomAdapter(activity,context,list);

                    lv.setAdapter(cca);
                    pb.setVisibility(View.INVISIBLE);
                }else{
                    activity.finish();
                }

            }
        });
        return view;
    }

    private class ViewHolder {
        TextView nome;
        TextView cod;
        TextView aco;
        Button del;
        TextView obs;
        TextView qtd;
    }
}

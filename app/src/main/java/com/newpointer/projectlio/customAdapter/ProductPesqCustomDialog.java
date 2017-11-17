package com.newpointer.projectlio.customAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.connection.DBLiteConnection;
import com.newpointer.projectlio.model.ProductModel;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.List;

/**
 * Created by FelipeRsN on 7/4/16.
 */
public class ProductPesqCustomDialog extends Dialog implements View.OnClickListener{
    private Activity act;
    private Context ctx;
    private Button close;
    private ListView lv_prod;
    private ProgressBar prog;
    private DBLiteConnection dbl;
    private List<ProductModel> prod;
    private BadgeView badge;
    private String name;
    private TextView dialog;

    public ProductPesqCustomDialog(Context context, Activity act, BadgeView badge, String name) {
        super(context);
        this.ctx = context;
        this.act = act;
        this.badge = badge;
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_pesquisaprod);

        startVar();

        prod = dbl.selectProdByName(name);
        dialog.setText("Total encontrado: "+prod.size());
        ProductCustomAdapter pca = new ProductCustomAdapter(act,ctx,prod);
        lv_prod.setAdapter(pca);

        prog.setVisibility(View.INVISIBLE);

        lv_prod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductDetailCustomDialog pdcd = new ProductDetailCustomDialog(ctx,act,prod.get(position),badge);
                pdcd.setCancelable(false);
                pdcd.setCanceledOnTouchOutside(false);
                pdcd.show();
                dismiss();
            }
        });

        lv_prod.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ProductModel p = prod.get(position);
                String tobs = "";
                dbl.insertProdCarrinho(p.getId(),p.getName(),1,"",tobs);
                int b = Integer.parseInt(badge.getText().toString());
                b++;
                badge.setText(b+"");
                Toast.makeText(act, "Produto adicionado ao carrinho", Toast.LENGTH_SHORT).show();
                dismiss();
                return true;
            }
        });

    }

    private void startVar(){

        close = (Button) findViewById(R.id.bt_dialogpesq_close);
        lv_prod = (ListView) findViewById(R.id.lv_dialogpesq_prod);
        prog = (ProgressBar) findViewById(R.id.pb_dialogpesq_wait);
        dialog = (TextView) findViewById(R.id.tv_dialogpesq_tot);
        dbl = DBLiteConnection.Companion.getInstance(ctx);

        close.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == close){
            dismiss();
        }
    }
}

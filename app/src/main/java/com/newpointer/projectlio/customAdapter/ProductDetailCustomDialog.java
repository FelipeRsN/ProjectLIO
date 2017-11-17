package com.newpointer.projectlio.customAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.connection.DBLiteConnection;
import com.newpointer.projectlio.model.AcompanhamentoModel;
import com.newpointer.projectlio.model.GroupAcomp_ListView_Model;
import com.newpointer.projectlio.model.GrupoAcompModel;
import com.newpointer.projectlio.model.ProductModel;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by FelipeRsN on 7/4/16.
 */
public class ProductDetailCustomDialog extends Dialog implements View.OnClickListener{
    private Activity act;
    private Context ctx;
    private TextView prodname;
    private TextView prodcode;
    private TextView qtd;
    private FloatingActionButton plus;
    private FloatingActionButton minus;
    private Button add;
    private Button close;
    private ListView lv_adc;
    private ProgressBar prog;
    private DBLiteConnection dbl;
    private EditText obs;
    private ProductModel prod;
    private TextView tvacomp;
    private int intquant = 1;
    private List<GroupAcomp_ListView_Model> list;
    private String AcompCompleto = "";
    private BadgeView badge;

    public ProductDetailCustomDialog(Context context, Activity act, ProductModel prod, BadgeView badge) {
        super(context);
        this.ctx = context;
        this.act = act;
        this.prod = prod;
        this.badge = badge;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_product);

        startVar();

        prodname.setText(prod.getName());
        String stringCod = String.format("%14s", prod.getId()+"").replace(' ', '0');
        prodcode.setText(stringCod);

        if(prod.getFl_acomp() == 1){
            String aco = prod.getAcomp();
            if(aco.contains(",")){
                // mais de 1 acompanhamento
                String[] splitAco = aco.split(",");
                int i = 0;
                List<AcompanhamentoModel> ac = new ArrayList<AcompanhamentoModel>();
                while (i < splitAco.length){
                    ac.clear();
                    ac = dbl.selectAcompByGroup(Integer.parseInt(splitAco[i]));
                    GrupoAcompModel gam = dbl.selectGrupoAcomp(Integer.parseInt(splitAco[i]));
                    list.add(new GroupAcomp_ListView_Model(gam.getId(),gam.getName(),gam.getSelecao(),1));
                    int j = 0;
                    while (j < ac.size()){
                        list.add(new GroupAcomp_ListView_Model(ac.get(j).getId(), ac.get(j).getName(), gam.getSelecao(), 0));
                        j++;
                    }
                    i++;
                }
                AcompanhamentosCustomAdapter aca = new AcompanhamentosCustomAdapter(act,ctx,list);
                lv_adc.setAdapter(aca);
                prog.setVisibility(View.INVISIBLE);
            }else{
                // 1 acompanhamento
                List<AcompanhamentoModel> acompList = dbl.selectAcompByGroup(Integer.parseInt(aco));
                GrupoAcompModel gam = dbl.selectGrupoAcomp(Integer.parseInt(aco));
                list.add(new GroupAcomp_ListView_Model(gam.getId(),gam.getName(),gam.getSelecao(),1));
                int i = 0;
                while (i < acompList.size()){
                    list.add(new GroupAcomp_ListView_Model(acompList.get(i).getId(), acompList.get(i).getName(), gam.getSelecao(), 0));
                    i++;
                }
                AcompanhamentosCustomAdapter aca = new AcompanhamentosCustomAdapter(act,ctx,list);
                lv_adc.setAdapter(aca);
                prog.setVisibility(View.INVISIBLE);
            }
        }else{
            prog.setVisibility(View.INVISIBLE);
            tvacomp.setVisibility(View.VISIBLE);
        }

        lv_adc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(list.get(i).getMode() == 0){
                    CheckBox cb = (CheckBox) view.findViewById(R.id.cb_acomp_desc);
                    if(cb.isChecked()){
                        cb.setChecked(false);
                        AcompCompleto = AcompCompleto.replace("---" + list.get(i).getDesc()+"\n", "");
                        AcompCompleto = AcompCompleto.replace("\n---" + list.get(i).getDesc(), "");
                        AcompCompleto = AcompCompleto.replace("---" + list.get(i).getDesc(), "");
                    }else{
                        cb.setChecked(true);
                        if(AcompCompleto.equals("")){
                            AcompCompleto = AcompCompleto + "---" + list.get(i).getDesc();
                        }else{
                            AcompCompleto = AcompCompleto + "\n---" + list.get(i).getDesc();
                        }
                    }
                }
            }
        });

    }

    private void startVar(){
        prodname = (TextView) findViewById(R.id.tv_dialogprod_name);
        prodcode = (TextView) findViewById(R.id.tv_dialogprod_cod);
        qtd = (TextView) findViewById(R.id.tv_dialogprod_qtd);
        plus = (FloatingActionButton) findViewById(R.id.fab_dialogprod_plus);
        minus = (FloatingActionButton) findViewById(R.id.fab_dialogprod_minus);
        add = (Button) findViewById(R.id.bt_dialogprod_add);
        close = (Button) findViewById(R.id.bt_dialogprod_close);
        lv_adc = (ListView) findViewById(R.id.lv_dialogprod_adc);
        prog = (ProgressBar) findViewById(R.id.pb_dialogprod_wait);
        dbl = DBLiteConnection.Companion.getInstance(ctx);
        obs = (EditText) findViewById(R.id.et_dialogprod_obs);
        tvacomp = (TextView) findViewById(R.id.tv_dialogprod_acomp);
        list = new ArrayList<GroupAcomp_ListView_Model>();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        InputMethodManager inputManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(obs.getWindowToken(), 0);

        obs.clearFocus();

        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
        add.setOnClickListener(this);
        close.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        InputMethodManager inputManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(obs.getWindowToken(), 0);

        obs.clearFocus();
        if(view == close){
            act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            dismiss();
        }
        if(view == plus){
            intquant++;
            qtd.setText(intquant+"");
        }
        if(view == minus){
            if(intquant>1){
                intquant--;
                qtd.setText(intquant+"");
            }
        }
        if(view == add){
            String tobs = "";
            if(obs.getText().toString().length()>0){
                tobs = obs.getText().toString();
            }
            dbl.insertProdCarrinho(prod.getId(),prod.getName(),intquant,AcompCompleto,tobs);
            int b = Integer.parseInt(badge.getText().toString());
            b++;
            badge.setText(b+"");
            Toast.makeText(act, "Produto adicionado ao carrinho", Toast.LENGTH_SHORT).show();
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

            inputManager = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(obs.getWindowToken(), 0);

            obs.clearFocus();
            dismiss();
        }
    }
}

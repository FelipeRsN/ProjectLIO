package com.newpointer.projectlio.customAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.activity.MainActivity;
import com.newpointer.projectlio.connection.DBLiteConnection;


/**
 * Created by FelipeRsN on 7/4/16.
 */
public class PergMesaCustomDialog extends Dialog implements View.OnClickListener{
    private Activity act;
    private Context ctx;
    private Button bt_0;
    private Button bt_1;
    private Button bt_2;
    private Button bt_3;
    private Button bt_4;
    private Button bt_5;
    private Button bt_6;
    private Button bt_7;
    private Button bt_8;
    private Button bt_9;
    private ImageButton bt_back;
    private ImageButton bt_ok;
    private TextView mesa_cod;
    private String stringcodigo = "";
    private Button close;
    private String comanda;
    private TextView cd_comanda;
    private DBLiteConnection dbl;

    public PergMesaCustomDialog(Context context, Activity act, String comanda) {
        super(context);
        this.ctx = context;
        this.act = act;
        this.comanda = comanda;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_perguntamesa);

        startVar();

        cd_comanda.setText(dbl.selectConfig().getTitulo_loja()+": "+comanda);

    }

    private void startVar(){
        dbl = DBLiteConnection.Companion.getInstance(act);
        bt_0 = (Button) findViewById(R.id.bt_pergmesa_0);
        bt_1 = (Button) findViewById(R.id.bt_pergmesa_1);
        bt_2 = (Button) findViewById(R.id.bt_pergmesa_2);
        bt_3 = (Button) findViewById(R.id.bt_pergmesa_3);
        bt_4 = (Button) findViewById(R.id.bt_pergmesa_4);
        bt_5 = (Button) findViewById(R.id.bt_pergmesa_5);
        bt_6 = (Button) findViewById(R.id.bt_pergmesa_6);
        bt_7 = (Button) findViewById(R.id.bt_pergmesa_7);
        bt_8 = (Button) findViewById(R.id.bt_pergmesa_8);
        bt_9 = (Button) findViewById(R.id.bt_pergmesa_9);
        bt_back = (ImageButton) findViewById(R.id.ib_pergmesa_backspace);
        bt_ok = (ImageButton) findViewById(R.id.ib_pergmesa_ok);
        mesa_cod = (TextView) findViewById(R.id.tv_pergmesa_codigo);
        close = (Button) findViewById(R.id.bt_pergmesa_close);
        cd_comanda = (TextView) findViewById(R.id.tv_pergmesa_comanda);

        bt_0.setOnClickListener(this);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        bt_3.setOnClickListener(this);
        bt_4.setOnClickListener(this);
        bt_5.setOnClickListener(this);
        bt_6.setOnClickListener(this);
        bt_7.setOnClickListener(this);
        bt_8.setOnClickListener(this);
        bt_9.setOnClickListener(this);
        bt_back.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
        close.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == bt_ok){
            Intent i = new Intent();
            if(mesa_cod.getText().toString().length()>0){
                i.setClass(act, MainActivity.class);
                i.putExtra("numeroMesa",comanda);
                String stringmesa = String.format("%6s", mesa_cod.getText().toString()).replace(' ', '0');
                i.putExtra("mesa", stringmesa);
                act.startActivity(i);
                dismiss();
            }else{
                Toast.makeText(act, "Digito obrigatório", Toast.LENGTH_SHORT).show();
            }
        }
        if(view == bt_0){
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo+"0";
            mesa_cod.setText(stringcodigo);
        }
        if(view == bt_1){
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo+"1";
            mesa_cod.setText(stringcodigo);
        }
        if(view == bt_2){
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo+"2";
            mesa_cod.setText(stringcodigo);
        }
        if(view == bt_3){
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo+"3";
            mesa_cod.setText(stringcodigo);
        }
        if(view == bt_4){
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo+"4";
            mesa_cod.setText(stringcodigo);
        }
        if(view == bt_5){
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo+"5";
            mesa_cod.setText(stringcodigo);
        }
        if(view == bt_6){
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo+"6";
            mesa_cod.setText(stringcodigo);
        }
        if(view == bt_7){
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo+"7";
            mesa_cod.setText(stringcodigo);
        }
        if(view == bt_8){
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo+"8";
            mesa_cod.setText(stringcodigo);
        }
        if(view == bt_9){
            stringcodigo = mesa_cod.getText().toString();
            stringcodigo = stringcodigo+"9";
            mesa_cod.setText(stringcodigo);
        }
        if(view == bt_back){
            stringcodigo = mesa_cod.getText().toString();
            if (!stringcodigo.isEmpty()) stringcodigo = stringcodigo.substring(0, stringcodigo.length() - 1);
            mesa_cod.setText(stringcodigo);
        }
        if(view == close){
            dismiss();
        }
    }
}

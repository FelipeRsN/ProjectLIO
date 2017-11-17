package com.newpointer.projectlio.customAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.newpointer.projectlio.R;
import com.newpointer.projectlio.activity.PagamentoActivity;
import com.newpointer.projectlio.connection.DBLiteConnection;

import java.text.DecimalFormat;


/**
 * Created by FelipeRsN on 7/4/16.
 */
public class PergDocCustomDialog extends Dialog implements View.OnClickListener{
    private Activity act;
    private Context ctx;
    private Button cpf;
    private Button cnpj;
    private Button nenhum;
    private Button voltar;


    public PergDocCustomDialog(Context context, Activity act) {
        super(context);
        this.ctx = context;
        this.act = act;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cfe);

        startVar();

    }

    private void startVar(){
        cpf = (Button) findViewById(R.id.cpf);
        cnpj = (Button) findViewById(R.id.cnpj);
        nenhum = (Button) findViewById(R.id.nenhum);
        voltar = (Button) findViewById(R.id.voltar);

        cpf.setOnClickListener(this);
        cnpj.setOnClickListener(this);
        voltar.setOnClickListener(this);
        nenhum.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == cpf){
            PergDocumentoCustomDialog pergValor = new PergDocumentoCustomDialog(ctx, act, PergDocumentoCustomDialog.MODE_CPF);
            pergValor.setCancelable(false);
            pergValor.setCanceledOnTouchOutside(false);
            pergValor.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            pergValor.show();
            dismiss();
        }
        if(view == cnpj){
            PergDocumentoCustomDialog pergValor = new PergDocumentoCustomDialog(ctx, act, PergDocumentoCustomDialog.MODE_CNPJ);
            pergValor.setCancelable(false);
            pergValor.setCanceledOnTouchOutside(false);
            pergValor.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            pergValor.show();
            dismiss();
        }
        if(view == voltar){
            ((PagamentoActivity)ctx).openCFEDialog();
            dismiss();
        }
        if(view == nenhum){
            ((PagamentoActivity) ctx).concluirProcessamento("", -1);
            dismiss();
        }
    }
}

package com.newpointer.projectlio.customAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.activity.PagamentoActivity;
import com.newpointer.projectlio.connection.DBLiteConnection;

import br.com.concretesolutions.canarinho.validator.Validador;
import br.com.concretesolutions.canarinho.watcher.MascaraNumericaTextWatcher;


/**
 * Created by FelipeRsN on 7/4/16.
 */
public class PergDocumentoCustomDialog extends Dialog implements View.OnClickListener{
    private Activity act;
    private Context ctx;
    private Button ok;
    private Button cancel;
    private TextView title;
    private TextInputEditText documento;
    private DBLiteConnection dbl;
    private int mode;

    public static final int MODE_CPF = 1;
    public static final int MODE_CNPJ = 2;

    public PergDocumentoCustomDialog(Context context, Activity act, int mode) {
        super(context);
        this.ctx = context;
        this.act = act;
        this.mode = mode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_digitar_documento);

        startVar();
    }

    private void startVar(){
        dbl = DBLiteConnection.Companion.getInstance(act);
        ok = (Button) findViewById(R.id.OK);
        cancel = (Button) findViewById(R.id.Cancel);
        title = (TextView) findViewById(R.id.title);
        documento = (TextInputEditText) findViewById(R.id.documento);

        if(mode == MODE_CPF){
            title.setText("Digite o CPF:");
            documento.setHint("CPF");
            documento.addTextChangedListener(new MascaraNumericaTextWatcher.Builder()
                    .paraMascara("###.###.###-##")
                    .comValidador(Validador.CPF)
                    .build());

        }else{
            title.setText("Digite o CNPJ:");
            documento.setHint("CNPJ");
            documento.addTextChangedListener(new MascaraNumericaTextWatcher.Builder()
                    .paraMascara("##.###.###/####-##")
                    .comValidador(Validador.CNPJ)
                    .build());
        }



        documento.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                ok.callOnClick();
                return true;
            }
        });



        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == ok){
            if(mode == MODE_CPF) {
                if(Validador.CPF.ehValido(documento.getText().toString())){
                    ((PagamentoActivity) ctx).concluirProcessamento(documento.getText().toString(), mode);
                }else{
                    Toast.makeText(act, "O CPF digitado não é válido.", Toast.LENGTH_SHORT).show();
                }
            }else{
                if(Validador.CNPJ.ehValido(documento.getText().toString())){
                    ((PagamentoActivity) ctx).concluirProcessamento(documento.getText().toString(), mode);
                }else{
                    Toast.makeText(act, "O CNPJ digitado não é válido.", Toast.LENGTH_SHORT).show();
                }
            }
        }
        if(view == cancel){
            dismiss();
            ((PagamentoActivity) ctx).openCPFCNPJDialog();
        }
    }
}

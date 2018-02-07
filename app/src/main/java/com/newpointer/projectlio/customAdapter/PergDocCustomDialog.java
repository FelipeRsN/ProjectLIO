package com.newpointer.projectlio.customAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.activity.PagamentoActivity;

import br.com.concretesolutions.canarinho.validator.Validador;
import br.com.concretesolutions.canarinho.watcher.MascaraNumericaTextWatcher;


/**
 * Created by FelipeRsN on 7/4/16.
 */
public class PergDocCustomDialog extends Dialog implements View.OnClickListener{
    private Activity act;
    private Context ctx;
    private Button voltar;
    private Button continuar;
    private TextView CPF;
    private TextView CNPJ;
    private TextView NENHUM;
    private TextInputEditText documentoCPF;
    private TextInputEditText documentoCNPJ;

    private int mode = MODE_NENHUM;

    public static final int MODE_CPF = 1;
    public static final int MODE_CNPJ = 2;
    public static final int MODE_NENHUM = -1;


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
        CPF = (TextView) findViewById(R.id.selectorCPF);
        CNPJ = (TextView) findViewById(R.id.selectorCNPJ);
        NENHUM = (TextView) findViewById(R.id.selectorNENHUM);

        voltar = (Button) findViewById(R.id.voltar);
        continuar = (Button) findViewById(R.id.continuar);

        documentoCPF = (TextInputEditText) findViewById(R.id.documentoCPF);
        documentoCNPJ = (TextInputEditText) findViewById(R.id.documentoCNPJ);

        documentoCPF.addTextChangedListener(new MascaraNumericaTextWatcher.Builder()
                .paraMascara("###.###.###-##")
                .comValidador(Validador.CPF)
                .build());

        documentoCNPJ.addTextChangedListener(new MascaraNumericaTextWatcher.Builder()
                .paraMascara("##.###.###/####-##")
                .comValidador(Validador.CNPJ)
                .build());

        documentoCPF.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                continuar.performClick();
                return true;
            }
        });

        documentoCNPJ.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                continuar.performClick();
                return true;
            }
        });

        CPF.setOnClickListener(this);
        CNPJ.setOnClickListener(this);
        voltar.setOnClickListener(this);
        NENHUM.setOnClickListener(this);
        continuar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == CPF){
            changeSelectorChoise(MODE_CPF);
            enableButtonContinue(true);
            documentoCNPJ.clearFocus();
            documentoCNPJ.setText("");
            documentoCNPJ.setEnabled(false);
            documentoCNPJ.setVisibility(View.INVISIBLE);
            documentoCPF.setVisibility(View.VISIBLE);
            documentoCPF.setEnabled(true);
            InputMethodManager inputMethodManager =  (InputMethodManager)act.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(documentoCPF,InputMethodManager.SHOW_IMPLICIT);

        }
        if(view == CNPJ){
            changeSelectorChoise(MODE_CNPJ);
            enableButtonContinue(true);
            documentoCPF.clearFocus();
            documentoCPF.setText("");
            documentoCPF.setEnabled(false);
            documentoCPF.setVisibility(View.INVISIBLE);
            documentoCNPJ.setVisibility(View.VISIBLE);
            documentoCNPJ.setEnabled(true);
            InputMethodManager inputMethodManager =  (InputMethodManager)act.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(documentoCNPJ,InputMethodManager.SHOW_IMPLICIT);

        }
        if(view == NENHUM){
            changeSelectorChoise(MODE_NENHUM);
            enableButtonContinue(true);
            documentoCPF.setVisibility(View.INVISIBLE);
            documentoCPF.setEnabled(false);
            documentoCNPJ.setVisibility(View.INVISIBLE);
            documentoCNPJ.setEnabled(false);
            documentoCPF.setText("");
            documentoCNPJ.setText("");
        }
        if(view == voltar){
            ((PagamentoActivity)ctx).openCFEDialog();
            dismiss();
        }
        if(view == continuar){
            switch (mode){
                case MODE_CPF:
                    if(Validador.CPF.ehValido(documentoCPF.getText().toString())){
                        ((PagamentoActivity) ctx).concluirProcessamento(documentoCPF.getText().toString().replace(".","").replace("-","").replace("/",""), mode);
                        dismiss();
                    }else{
                        Toast.makeText(act, "O CPF digitado não é válido.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MODE_CNPJ:
                    if(Validador.CNPJ.ehValido(documentoCNPJ.getText().toString())){
                        ((PagamentoActivity) ctx).concluirProcessamento(documentoCNPJ.getText().toString().replace(".","").replace("-","").replace("/",""), mode);
                        dismiss();
                    }else{
                        Toast.makeText(act, "O CNPJ digitado não é válido.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MODE_NENHUM:
                    ((PagamentoActivity) ctx).concluirProcessamento("", mode);
                    dismiss();
                    break;
                default:
                    Toast.makeText(act, "Você precisa selecionar uma das opções", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeSelectorChoise(int choice){
        switch (choice){
            case MODE_CPF:
                CPF.setBackgroundResource(R.drawable.selector_left_selected);
                CNPJ.setBackgroundResource(R.drawable.selector_center);
                NENHUM.setBackgroundResource(R.drawable.selector_right);
                CPF.setTextColor(act.getResources().getColor(R.color.colorWhite));
                CNPJ.setTextColor(act.getResources().getColor(R.color.colorAccent));
                NENHUM.setTextColor(act.getResources().getColor(R.color.colorAccent));
                break;
            case MODE_CNPJ:
                CPF.setBackgroundResource(R.drawable.selector_left);
                CNPJ.setBackgroundResource(R.drawable.selector_center_selected);
                NENHUM.setBackgroundResource(R.drawable.selector_right);
                CPF.setTextColor(act.getResources().getColor(R.color.colorAccent));
                CNPJ.setTextColor(act.getResources().getColor(R.color.colorWhite));
                NENHUM.setTextColor(act.getResources().getColor(R.color.colorAccent));
                break;
            case MODE_NENHUM:
                CPF.setBackgroundResource(R.drawable.selector_left);
                CNPJ.setBackgroundResource(R.drawable.selector_center);
                NENHUM.setBackgroundResource(R.drawable.selector_right_selected);
                CPF.setTextColor(act.getResources().getColor(R.color.colorAccent));
                CNPJ.setTextColor(act.getResources().getColor(R.color.colorAccent));
                NENHUM.setTextColor(act.getResources().getColor(R.color.colorWhite));
                break;
        }
        mode = choice;
    }

    private void enableButtonContinue(boolean enable){
        continuar.setEnabled(enable);
        continuar.setClickable(enable);
        continuar.setAlpha(1f);
    }
}

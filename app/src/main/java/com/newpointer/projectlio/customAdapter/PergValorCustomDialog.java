package com.newpointer.projectlio.customAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
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
public class PergValorCustomDialog extends Dialog implements View.OnClickListener{
    private Activity act;
    private Context ctx;
    private Button ok;
    private Button cancel;
    private TextView title;
    private TextView valorRestante;
    private CurrencyEditText valorDigitado;
    private DBLiteConnection dbl;
    private int mode;
    private double valorTotal;

    public static final int MODE_DINHEIRO = 1;
    public static final int MODE_CARTAO = 2;

    public PergValorCustomDialog(Context context, Activity act, double valorTotal, int mode) {
        super(context);
        this.ctx = context;
        this.act = act;
        this.valorTotal = valorTotal;
        this.mode = mode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_digitar_valor);

        startVar();

    }

    private void startVar(){
        dbl = DBLiteConnection.Companion.getInstance(act);
        ok = (Button) findViewById(R.id.OK);
        cancel = (Button) findViewById(R.id.Cancel);
        title = (TextView) findViewById(R.id.title);
        valorRestante = (TextView) findViewById(R.id.valorRestante);
        valorDigitado = (CurrencyEditText) findViewById(R.id.moneyInput);

        long value = (long) (PagamentoActivity.round(valorTotal, 2) * 100);

       valorDigitado.setValue(value);

        if(mode == MODE_DINHEIRO){
            title.setText("Digite o valor a ser pago em dinheiro:");
        }else{
            title.setText("Digite o valor a ser pago no cartão:");
        }

        DecimalFormat formatarFloat = new DecimalFormat("0.00");
        valorRestante.setText("Valor restante: R$ "+formatarFloat.format(PagamentoActivity.round(valorTotal, 2)));

        valorDigitado.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
            if(PagamentoActivity.round(valorDigitado.getRawValue(), 2) > 0) {
                if ((PagamentoActivity.round(valorDigitado.getRawValue(), 2) / 100.00) > PagamentoActivity.round(valorTotal, 2)) {
                    Toast.makeText(act, "Valor digitado maior que o valor total restante.", Toast.LENGTH_SHORT).show();
                } else {
                    if (mode == MODE_DINHEIRO) {
                        ((PagamentoActivity) ctx).registerMoneyPayment(valorDigitado.getRawValue());
                    } else {
                        ((PagamentoActivity) ctx).registerCardPayment(valorDigitado.getRawValue());
                    }
                    dismiss();
                }
            }else{
                Toast.makeText(act, "Por favor, digite um valor válido.", Toast.LENGTH_SHORT).show();
            }
        }
        if(view == cancel){
            dismiss();
        }
    }
}

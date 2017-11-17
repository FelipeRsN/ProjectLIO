package com.newpointer.projectlio.customAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.activity.ConfigurationActivity;
import com.newpointer.projectlio.connection.DBLiteConnection;
import com.newpointer.projectlio.model.OperadorModel;


/**
 * Created by FelipeRsN on 7/4/16.
 */
public class OperadorCustomDialog extends Dialog implements View.OnClickListener{
    private Activity act;
    private Context ctx;
    private Button close;
    private Button enviar;
    private EditText login;
    private EditText pass;
    private DBLiteConnection dbl;
    private OperadorModel op;
    public OperadorCustomDialog(Context context, Activity act) {
        super(context);
        this.ctx = context;
        this.act = act;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialogoperador);

        startVar();

    }

    private void startVar(){

        close = (Button) findViewById(R.id.bt_dialogop_close);
        enviar = (Button) findViewById(R.id.bt_dialogop_enviar);
        login = (EditText) findViewById(R.id.et_dialogop_codigo);
        pass = (EditText) findViewById(R.id.et_dialogop_senha);
        dbl = DBLiteConnection.Companion.getInstance(ctx);
        login.requestFocus();
        enviar.setOnClickListener(this);
        close.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == close){
            dismiss();
        }
        if(view == enviar){
            if(login.getText().toString().length()<1){
                Toast.makeText(act, "Código não preenchido", Toast.LENGTH_SHORT).show();
                login.requestFocus();
            }else{
                if(pass.getText().toString().length()<1){
                    Toast.makeText(act, "Senha não preenchida", Toast.LENGTH_SHORT).show();
                    pass.requestFocus();
                }else{
                    op = dbl.selectOp(Integer.parseInt(login.getText().toString()));
                    if(op == null){
                        login.setText("");
                        pass.setText("");
                        login.requestFocus();
                        Toast.makeText(act, "Operador não encontrado", Toast.LENGTH_SHORT).show();
                    }else{
                        if(op.getFliniciar() == 1 || op.getFlprimeiro() == 1){
                            login.setText("");
                            pass.setText("");
                            login.requestFocus();
                            Toast.makeText(act, "Usuário novo. É preciso cadastrar uma senha de acesso.", Toast.LENGTH_SHORT).show();
                        }else{
                            if(pass.getText().toString().equalsIgnoreCase(op.getPassword())){
                                if(op.getFlperfil() == 0){
                                    String func = dbl.selectOpFunc(Integer.parseInt(login.getText().toString()));
                                    if(func.equalsIgnoreCase("CONFIG_FUN")){
                                        Intent i = new Intent();
                                        i.setClass(act, ConfigurationActivity.class);
                                        act.startActivity(i);
                                        dismiss();
                                    }else{
                                        if(login.getText().toString().equalsIgnoreCase("0") && pass.getText().toString().equalsIgnoreCase("794613")){
                                            Intent i = new Intent();
                                            i.setClass(act, ConfigurationActivity.class);
                                            act.startActivity(i);
                                            dismiss();
                                        }else{
                                            login.setText("");
                                            pass.setText("");
                                            login.requestFocus();
                                            Toast.makeText(act, "Usuário sem permissão para esta função", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }else{
                                    String func = dbl.selectPerfilFunc(op.getCdperfil());
                                    if(func.equalsIgnoreCase("CONFIG_FUN")){
                                        Intent i = new Intent();
                                        i.setClass(act, ConfigurationActivity.class);
                                        act.startActivity(i);
                                        dismiss();
                                    }else{
                                        if(login.getText().toString().equalsIgnoreCase("0") && pass.getText().toString().equalsIgnoreCase("794613")){
                                            Intent i = new Intent();
                                            i.setClass(act, ConfigurationActivity.class);
                                            act.startActivity(i);
                                            dismiss();
                                        }else{
                                            login.setText("");
                                            pass.setText("");
                                            login.requestFocus();
                                            Toast.makeText(act, "Usuário sem permissão para esta função", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }else{
                                login.setText("");
                                pass.setText("");
                                login.requestFocus();
                                Toast.makeText(act, "Senha inválida", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        }
    }
}

package com.newpointer.projectlio.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.connection.DBLiteConnection;
import com.newpointer.projectlio.customAdapter.OperadorCustomDialog;
import com.newpointer.projectlio.customAdapter.PergMesaCustomDialog;
import com.newpointer.projectlio.model.ConfigModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;


public class StartActivity extends AppCompatActivity implements View.OnClickListener{
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
    private Button configuration;
    private FrameLayout my_account;
    private FrameLayout make_order;
    private FrameLayout pgto;
    private TextView comanda;
    private TextView titulo;
    private String nmesa = "";
    private DBLiteConnection dbl;
    private ConfigModel config;
    private Intent i;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startVar();

        config = dbl.selectConfig();
        titulo.setText(config.getTitulo_loja());

        pd = new ProgressDialog(StartActivity.this);
        pd.setMessage("Processando informações abertas de pagamento...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
    }

    private void startVar(){
        bt_0 = (Button) findViewById(R.id.bt_start_0);
        bt_1 = (Button) findViewById(R.id.bt_start_1);
        bt_2 = (Button) findViewById(R.id.bt_start_2);
        bt_3 = (Button) findViewById(R.id.bt_start_3);
        bt_4 = (Button) findViewById(R.id.bt_start_4);
        bt_5 = (Button) findViewById(R.id.bt_start_5);
        bt_6 = (Button) findViewById(R.id.bt_start_6);
        bt_7 = (Button) findViewById(R.id.bt_start_7);
        bt_8 = (Button) findViewById(R.id.bt_start_8);
        bt_9 = (Button) findViewById(R.id.bt_start_9);
        bt_back = (ImageButton) findViewById(R.id.ib_start_backspace);
        my_account = (FrameLayout) findViewById(R.id.preconta);
        make_order = (FrameLayout) findViewById(R.id.pedido);
        pgto = (FrameLayout) findViewById(R.id.pgto);
        configuration = (Button) findViewById(R.id.bt_start_config);
        comanda = (TextView) findViewById(R.id.tv_start_comanda);
        titulo = (TextView) findViewById(R.id.tv_start_tmesa);
        dbl = DBLiteConnection.Companion.getInstance(this);
        i = new Intent();

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
        my_account.setOnClickListener(this);
        make_order.setOnClickListener(this);
        pgto.setOnClickListener(this);
        configuration.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == bt_0){
            nmesa = comanda.getText().toString();
            nmesa = nmesa+"0";
            comanda.setText(nmesa);
        }
        if(view == bt_1){
            nmesa = comanda.getText().toString();
            nmesa = nmesa+"1";
            comanda.setText(nmesa);
        }
        if(view == bt_2){
            nmesa = comanda.getText().toString();
            nmesa = nmesa+"2";
            comanda.setText(nmesa);
        }
        if(view == bt_3){
            nmesa = comanda.getText().toString();
            nmesa = nmesa+"3";
            comanda.setText(nmesa);
        }
        if(view == bt_4){
            nmesa = comanda.getText().toString();
            nmesa = nmesa+"4";
            comanda.setText(nmesa);
        }
        if(view == bt_5){
            nmesa = comanda.getText().toString();
            nmesa = nmesa+"5";
            comanda.setText(nmesa);
        }
        if(view == bt_6){
            nmesa = comanda.getText().toString();
            nmesa = nmesa+"6";
            comanda.setText(nmesa);
        }
        if(view == bt_7){
            nmesa = comanda.getText().toString();
            nmesa = nmesa+"7";
            comanda.setText(nmesa);
        }
        if(view == bt_8){
            nmesa = comanda.getText().toString();
            nmesa = nmesa+"8";
            comanda.setText(nmesa);
        }
        if(view == bt_9){
            nmesa = comanda.getText().toString();
            nmesa = nmesa+"9";
            comanda.setText(nmesa);
        }
        if(view == bt_back){
            nmesa = comanda.getText().toString();
            if (!nmesa.isEmpty()) nmesa = nmesa.substring(0, nmesa.length() - 1);
            comanda.setText(nmesa);
        }
        if(view == make_order){
            if(comanda.getText().toString().isEmpty()){
                Toast.makeText(StartActivity.this, "Digito da mesa obrigatório", Toast.LENGTH_SHORT).show();
            }else{
                int ncomanda = Integer.parseInt(comanda.getText().toString());
                if(ncomanda < Integer.parseInt(config.getNmin_mesa()) || ncomanda > Integer.parseInt(config.getNmax_mesa())){
                    Toast.makeText(StartActivity.this, "Numero da mesa fora do limite", Toast.LENGTH_SHORT).show();
                    comanda.setText("");
                    nmesa = "";
                }else{
                    if(dbl.selectConfig().getDigito_verificador() == 1){
                        String stringComanda = String.format("%6s", ncomanda).replace(' ', '0');
                        int impar = ((Integer.parseInt(stringComanda.substring(4, 5))) + (Integer.parseInt(stringComanda.substring(2, 3))) + (Integer.parseInt(stringComanda.substring(0, 1)))) * 3;
                        int par = (Integer.parseInt(stringComanda.substring(3, 4))) + (Integer.parseInt(stringComanda.substring(1, 2)));
                        int soma = par + impar;
                        String so = ""+ soma;
                        int tam = so.length();
                        int digito = Integer.parseInt(so.substring(tam - 1));
                        if(digito != 0){
                            digito = digito -10;
                        }
                        digito = digito *(-1);
                        if(digito != (Integer.parseInt(stringComanda.substring(5, 6)))){
                            Toast.makeText(StartActivity.this, "Digito verificador incorreto.", Toast.LENGTH_SHORT).show();
                            comanda.setText("");
                            nmesa = "";
                        }else{
                            comanda.setText("");
                            nmesa = "";
                            if(config.getPergunta_mesa() == 1){
                                PergMesaCustomDialog pmcd = new PergMesaCustomDialog(StartActivity.this, StartActivity.this,stringComanda);
                                pmcd.setCancelable(false);
                                pmcd.setCanceledOnTouchOutside(false);
                                pmcd.show();
                            }else{
                                i.setClass(StartActivity.this, MainActivity.class);
                                i.putExtra("numeroMesa",stringComanda);
                                startActivity(i);
                            }
                        }
                    }else{
                        String stringComanda = String.format("%6s", ncomanda).replace(' ', '0');
                        comanda.setText("");
                        nmesa = "";
                        if(config.getPergunta_mesa() == 1){
                            PergMesaCustomDialog pmcd = new PergMesaCustomDialog(StartActivity.this, StartActivity.this,stringComanda);
                            pmcd.setCancelable(false);
                            pmcd.setCanceledOnTouchOutside(false);
                            pmcd.show();
                        }else {
                            i.setClass(StartActivity.this, MainActivity.class);
                            i.putExtra("numeroMesa",stringComanda);
                            startActivity(i);
                        }
                    }
                }
            }
        }
        if(view == my_account){
            if(comanda.getText().toString().isEmpty()){
                Toast.makeText(StartActivity.this, "Digito da mesa obrigatório", Toast.LENGTH_SHORT).show();
            }else{
                int ncomanda = Integer.parseInt(comanda.getText().toString());
                if(ncomanda < Integer.parseInt(config.getNmin_mesa()) || ncomanda > Integer.parseInt(config.getNmax_mesa())){
                    Toast.makeText(StartActivity.this, "Numero da mesa fora do limite", Toast.LENGTH_SHORT).show();
                }else{
                    if(dbl.selectConfig().getDigito_verificador() == 1){
                        String stringComanda = String.format("%6s", ncomanda).replace(' ', '0');
                        int impar = ((Integer.parseInt(stringComanda.substring(4, 5))) + (Integer.parseInt(stringComanda.substring(2, 3))) + (Integer.parseInt(stringComanda.substring(0, 1)))) * 3;
                        int par = (Integer.parseInt(stringComanda.substring(3, 4))) + (Integer.parseInt(stringComanda.substring(1, 2)));
                        int soma = par + impar;
                        String so = ""+ soma;
                        int tam = so.length();
                        int digito = Integer.parseInt(so.substring(tam - 1));
                        if(digito != 0){
                            digito = digito -10;
                        }
                        digito = digito *(-1);
                        if(digito != (Integer.parseInt(stringComanda.substring(5, 6)))){
                            Toast.makeText(StartActivity.this, "Digito verificador incorreto.", Toast.LENGTH_SHORT).show();
                            comanda.setText("");
                            nmesa = "";
                        }else{
                            comanda.setText("");
                            nmesa = "";
                            i.setClass(StartActivity.this, MinhaContaActivity.class);
                            i.putExtra("numeroMesa",stringComanda);
                            i.putExtra("gerador",dbl.selectConfig().getTitulo_loja());
                            startActivity(i);
                        }
                    }else{
                        String stringComanda = String.format("%6s", ncomanda).replace(' ', '0');
                        comanda.setText("");
                        nmesa = "";
                        i.setClass(StartActivity.this, MinhaContaActivity.class);
                        i.putExtra("numeroMesa",stringComanda);
                        i.putExtra("gerador",dbl.selectConfig().getTitulo_loja());
                        startActivity(i);
                    }
                }
            }
        }
        if(view == pgto){
            if(comanda.getText().toString().isEmpty()){
                Toast.makeText(StartActivity.this, "Digito da mesa obrigatório", Toast.LENGTH_SHORT).show();
            }else{
                int ncomanda = Integer.parseInt(comanda.getText().toString());
                if(ncomanda < Integer.parseInt(config.getNmin_mesa()) || ncomanda > Integer.parseInt(config.getNmax_mesa())){
                    Toast.makeText(StartActivity.this, "Numero da mesa fora do limite", Toast.LENGTH_SHORT).show();
                    comanda.setText("");
                    nmesa = "";
                }else{
                    if(dbl.selectConfig().getDigito_verificador() == 1){
                        String stringComanda = String.format("%6s", ncomanda).replace(' ', '0');
                        int impar = ((Integer.parseInt(stringComanda.substring(4, 5))) + (Integer.parseInt(stringComanda.substring(2, 3))) + (Integer.parseInt(stringComanda.substring(0, 1)))) * 3;
                        int par = (Integer.parseInt(stringComanda.substring(3, 4))) + (Integer.parseInt(stringComanda.substring(1, 2)));
                        int soma = par + impar;
                        String so = ""+ soma;
                        int tam = so.length();
                        int digito = Integer.parseInt(so.substring(tam - 1));
                        if(digito != 0){
                            digito = digito -10;
                        }
                        digito = digito *(-1);
                        if(digito != (Integer.parseInt(stringComanda.substring(5, 6)))){
                            Toast.makeText(StartActivity.this, "Digito verificador incorreto.", Toast.LENGTH_SHORT).show();
                            comanda.setText("");
                            nmesa = "";
                        }else{
                            comanda.setText("");
                            nmesa = "";
                            //openPagamentoActivity(stringComanda);
                            pd.show();
                            new CheckIfPaymentIsOpen(stringComanda, dbl.selectConfig().getTitulo_loja(), true).execute();
                        }
                    }else{
                        String stringComanda = String.format("%6s", ncomanda).replace(' ', '0');
                        comanda.setText("");
                        nmesa = "";
                        if(config.getPergunta_mesa() == 1){
                            PergMesaCustomDialog pmcd = new PergMesaCustomDialog(StartActivity.this, StartActivity.this,stringComanda);
                            pmcd.setCancelable(false);
                            pmcd.setCanceledOnTouchOutside(false);
                            pmcd.show();
                        }else {
                            //openPagamentoActivity(stringComanda);
                            pd.show();
                            new CheckIfPaymentIsOpen(stringComanda, dbl.selectConfig().getTitulo_loja(), true).execute();
                        }
                    }
                }
            }
        }
        if(view == configuration){
            OperadorCustomDialog ocd = new OperadorCustomDialog(StartActivity.this, StartActivity.this);
            ocd.setCanceledOnTouchOutside(false);
            ocd.setCancelable(false);
            ocd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            ocd.show();
        }


    }

    private void openPagamentoActivity(String comanda){
        Intent i = new Intent();
        i.setClass(StartActivity.this, PagamentoActivity.class);
        i.putExtra("numeroMesa",comanda);
        i.putExtra("gerador",dbl.selectConfig().getTitulo_loja());
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(dbl != null){
            if(dbl.havePaymentOpened()){
                final String mesaPendente = dbl.getMesaWithPaymentOpened();
                final String comandaPendente = config.getTitulo_loja();

                pd.show();
                new CheckIfPaymentIsOpen(mesaPendente, comandaPendente, false).execute();
            }
        }
    }

    public class CheckIfPaymentIsOpen extends AsyncTask<String, Object, Integer> {
        String mesa;
        String comanda;
        private boolean isError = false;
        private String error = "";
        private boolean openingPayment;

        CheckIfPaymentIsOpen(String mesa, String comanda, boolean openingPayment){
            this.mesa = mesa;
            this.comanda = comanda;
            this.openingPayment = openingPayment;
        }

        @Override
        protected Integer doInBackground(String... bt) {
            try {
                Class.forName("org.firebirdsql.jdbc.FBDriver");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            try {
                Properties props = new Properties();
                props.setProperty("user", "POINTER");
                props.setProperty("password", "sysadmin");
                props.setProperty("encoding", "WIN1252");

                Log.d("FECHACONTA", "MESA: "+mesa);

                Connection conn = DriverManager.getConnection("jdbc:firebirdsql://" + dbl.selectConfig().getString_bd() + "", props);
                String sSql = "execute procedure VERIFICAR_CONTA_ABERTA_ANDROID ('"+mesa+"','"+dbl.selectConfig().getEstacao()+"')";
                Log.d("FECHACONTA ", "PROCEDURE: "+sSql);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sSql);
                while (rs.next()){
                    String result = rs.getString("RESULTADO");
                    Log.d("FECHACONTA ", result);
                    if(result.contains("OK")){
                        isError = false;
                    }else{
                        isError = true;
                        String[] erroSplit = result.split("\\|");
                        if(erroSplit[1] != null){
                            Log.d("FECHACONTA ", "ERRO: "+erroSplit[1]);
                            error = erroSplit[1];
                        }
                    }
                }
                rs.close();
                conn.close();
                stmt.close();
                return 1;
            } catch (Exception e1) {
                e1.printStackTrace();
                isError = true;
                error = e1.toString();
                return 0;
            }
        }

        @Override
        public void onPostExecute(Integer i) {
            if(!openingPayment) {
                pd.dismiss();
                if (isError) {
                    Log.d("FECHACONTA", "TRUE: " + error);
                    if (error.equalsIgnoreCase("CONTA EM USO POR OUTRA ESTACAO!")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this, R.style.YourDialogStyle);
                        builder.setTitle("Erro ao continuar");
                        builder.setMessage("Você iniciou um pagamento na " + comanda.toLowerCase() + " "+mesa+" e ele agora está em uso por outra estação.\n\nCaso ja tenha finalizado este pagamento em outra estação, entre em configurações e limpe os dados de pagamento.");
                        builder.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pd.show();
                                new CheckIfPaymentIsOpen(mesa, comanda, false).execute();
                            }
                        });
                        builder.setNegativeButton("Configurações", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                configuration.callOnClick();
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();
                    } else {
                        Toast.makeText(StartActivity.this, "O pagamento da "+comanda+" "+mesa+" foi finalizado por outra estação.", Toast.LENGTH_SHORT).show();
                        dbl.resetPgtoTable();
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this, R.style.YourDialogStyle);
                    builder.setTitle("Pagamento em andamento");
                    builder.setMessage("Este dispositivo possui um pagamento em aberto na " + comanda + " " + mesa + ".\n\nFinalize o pagamento ou acesse as configurações para limpar os dados de pagamento");
                    builder.setPositiveButton("Continuar pagamento", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openPagamentoActivity(mesa);
                        }
                    });
                    builder.setNegativeButton("Configurações", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            configuration.callOnClick();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            }else{
                if (isError) {
                    pd.dismiss();
                    Log.d("FECHACONTA", "TRUE: " + error);
                    if (error.equalsIgnoreCase("CONTA EM USO POR OUTRA ESTACAO!")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this, R.style.YourDialogStyle);
                        builder.setTitle("Erro ao continuar");
                        builder.setMessage("Essa " + comanda.toLowerCase() + " esta em uso por outra estação, aguarde a liberação da conta para finalizar o processo de pagamento e liberar esta estação\n\nCaso não deseje esperar, entre em configurações e limpe os dados de pagamento.");
                        builder.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new CheckIfPaymentIsOpen(mesa, comanda, true).execute();
                            }
                        });
                        builder.setNegativeButton("Configurações", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                configuration.callOnClick();
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this, R.style.YourDialogStyle);
                        builder.setTitle("Erro ao continuar");
                        builder.setMessage("Essa " + comanda.toLowerCase() + " esta fechada.");
                        builder.setPositiveButton("OK", null);
                        builder.setCancelable(false);
                        builder.show();
                    }
                } else {
                    new OpenAndCloseAccount(mesa, comanda, "U").execute();
                }
            }
        }

    }

    public class OpenAndCloseAccount extends AsyncTask<String, Object, Integer> {
        String mesa;
        String comanda;
        String openClose;
        private boolean isError = false;
        private String error = "";

        OpenAndCloseAccount(String mesa, String comanda, String openClose){
            this.mesa = mesa;
            this.comanda = comanda;
            this.openClose = openClose;
        }

        @Override
        protected Integer doInBackground(String... bt) {
            try {
                Class.forName("org.firebirdsql.jdbc.FBDriver");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            try {
                Properties props = new Properties();
                props.setProperty("user", "POINTER");
                props.setProperty("password", "sysadmin");
                props.setProperty("encoding", "WIN1252");

                Log.d("FECHACONTA", "MESA: "+mesa);

                Connection conn = DriverManager.getConnection("jdbc:firebirdsql://" + dbl.selectConfig().getString_bd() + "", props);
                String sSql = "execute procedure BLOQUEIA_LIBERA_CONTA_ANDROID  ('"+mesa+"','"+dbl.selectConfig().getEstacao()+"', '"+openClose+"')";
                Log.d("FECHACONTA ", "PROCEDURE: "+sSql);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sSql);
                while (rs.next()){
                    String result = rs.getString("RESULTADO");
                    Log.d("FECHACONTA ", result);
                    if(result.contains("OK")){
                        isError = false;
                    }else{
                        isError = true;
                        String[] erroSplit = result.split("\\|");
                        if(erroSplit[1] != null){
                            Log.d("FECHACONTA ", "ERRO: "+erroSplit[1]);
                            error = erroSplit[1];
                        }
                    }
                }
                rs.close();
                conn.close();
                stmt.close();
                return 1;
            } catch (Exception e1) {
                e1.printStackTrace();
                isError = true;
                error = e1.toString();
                return 0;
            }
        }

        @Override
        public void onPostExecute(Integer i) {
            pd.dismiss();
            if(isError){
                AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this, R.style.YourDialogStyle);
                builder.setTitle("Erro ao continuar");
                builder.setMessage(error);
                builder.setPositiveButton("OK", null);
                builder.setCancelable(false);
                builder.show();
            }else{
                openPagamentoActivity(mesa);
            }
        }
    }
}

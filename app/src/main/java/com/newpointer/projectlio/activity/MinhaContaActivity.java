package com.newpointer.projectlio.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.connection.DBLiteConnection;
import com.newpointer.projectlio.customAdapter.MinhaContaAdapter;
import com.newpointer.projectlio.model.ListaProdModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Properties;


public class MinhaContaActivity extends AppCompatActivity implements View.OnClickListener{
//    private ImageButton back;
//    private TextView conta;
    private Intent j;
    private TextView aviso;
//    private TableLayout tl;
    private String message;
    private String mesa;
    private DBLiteConnection dbl;
    private ProgressBar pb;
//    private TextView tv_conta_prod;
//    private TextView tv_conta_tax;
//    private TextView tv_conta_tot;
//    private TextView preconta;

    private FrameLayout voltar;
    private FrameLayout imprimir;
    private FrameLayout pgto;

    private TextView tvmesa;
    private TextView subtotal;
    private TextView tvTotalTaxa;
    private TextView tvTotalGeral;
    private RecyclerView list;

    private ArrayList<ListaProdModel> prod = new ArrayList<ListaProdModel>();

    private Double totalTaxa = 0.0;
    private Double totalGeral = 0.0;

    MinhaContaAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);

        startVar();

        GetProduct gp = new GetProduct();
        gp.execute();
    }

    private void startVar() {
        voltar = (FrameLayout) findViewById(R.id.voltar);
        imprimir = (FrameLayout) findViewById(R.id.imprimir);
        pgto = (FrameLayout) findViewById(R.id.pgto);
        pb = (ProgressBar) findViewById(R.id.pb_minhaconta);
        aviso = (TextView) findViewById(R.id.tv_minhaconta_aviso);

        list = (RecyclerView) findViewById(R.id.list);

        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new MinhaContaAdapter(prod ,MinhaContaActivity.this, MinhaContaActivity.this);
        list.setAdapter(adapter);

        tvmesa = (TextView) findViewById(R.id.mesa);
        subtotal = (TextView) findViewById(R.id.valueSubTotal);
        tvTotalTaxa = (TextView) findViewById(R.id.valueTax);
        tvTotalGeral = (TextView) findViewById(R.id.valueTotal);

        dbl = DBLiteConnection.Companion.getInstance(this);

        voltar.setOnClickListener(this);
        imprimir.setOnClickListener(this);
        pgto.setOnClickListener(this);

        j = getIntent();
        mesa = j.getStringExtra("numeroMesa");
        tvmesa.setText(j.getStringExtra("gerador")+": "+mesa);
        DBLiteConnection dbl = DBLiteConnection.Companion.getInstance(this);


    }

    @Override
    public void onClick(View v) {
        if(v == voltar){
            finish();
        }
        if(v == imprimir){
            if(dbl.selectConfig().getLio() == 1)
                new GeneratePreConta().execute();
            else{
                //TODO ADICIONAR IMPRESSAO LOCAL
                Toast.makeText(this, "LIO V2, IMPRIMIR LOCAL", Toast.LENGTH_SHORT).show();
            }
        }
        if(v == pgto){
            Intent i = new Intent(this, PagamentoActivity.class);
            i.putExtra("numeroMesa", mesa);
            i.putExtra("gerador", getIntent().getStringExtra("gerador"));
            startActivity(i);
        }
    }

    public class GeneratePreConta extends AsyncTask<String,Object,Integer> {
        @Override
        protected Integer doInBackground(String... bt) {
            try{
                Class.forName("org.firebirdsql.jdbc.FBDriver");
            }catch(Exception e){
                System.err.println(e.getMessage());
            }
            try{
                Properties props = new Properties();
                props.setProperty("user", "POINTER");
                props.setProperty("password", "sysadmin");
                props.setProperty("encoding", "WIN1252");
                Connection conn = DriverManager.getConnection("jdbc:firebirdsql://"+dbl.selectConfig().getString_bd()+"", props);
                String sSql = "EXECUTE PROCEDURE GERA_IMPRESSAO_PRECONTA_ANDROID("+mesa+")";
                Statement stmt = conn.createStatement();
                stmt.executeQuery(sSql);
                conn.close();
                stmt.close();
                return 1;
            }
            catch(SQLException e1){
                e1.printStackTrace();
                return 0;
            }
        }

        @Override
        public void onPostExecute(Integer i) {
            Toast.makeText(MinhaContaActivity.this, "Pedido de pré-conta enviado para impressão", Toast.LENGTH_LONG).show();
        }
    }

    public class GetProduct extends AsyncTask<String,Object,Integer> {
        private int id_conta = 0;
        private boolean haveConta = false;
        private double total = 0.0;
        @Override
        protected Integer doInBackground(String... bt) {
            try{
                Class.forName("org.firebirdsql.jdbc.FBDriver");
            }catch(Exception e){
                System.err.println(e.getMessage());
            }
            try{
                Properties props = new Properties();
                props.setProperty("user", "POINTER");
                props.setProperty("password", "sysadmin");
                props.setProperty("encoding", "WIN1252");
                Connection conn = DriverManager.getConnection("jdbc:firebirdsql://"+dbl.selectConfig().getString_bd()+"", props);
                String sSql = "Select * from minha_conta_android('"+dbl.selectConfig().getTitulo_loja()+"','"+dbl.selectConfig().getEstacao()+"','"+mesa+"')";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sSql);
                while (rs.next()){
                    if(rs.getString("RETORNO").equalsIgnoreCase("OK")){
                        haveConta = true;
                        Log.i("Retorno",rs.getString("RETORNO"));
                        Log.i("CD_PRODUTO",rs.getString("CD_PRODUTO"));
                        Log.i("DS_PRO",rs.getString("DS_PRO"));
                        Log.i("QT_CONSUMO",rs.getString("QT_CONSUMO"));
                        Log.i("VL_CONSUMO",rs.getString("VL_CONSUMO"));
                        Log.i("TP_ESTACAO",rs.getString("TP_ESTACAO"));
                        Log.i("VL_INDICE",rs.getString("VL_INDICE"));
                        Log.i("UN_PRO",rs.getString("UN_PRO"));
                        Double val_prod = 0.0;

                        if(rs.getString("VL_CONSUMO") != null){
                            val_prod = Double.parseDouble(rs.getString("VL_CONSUMO"));
                        }else{
                            val_prod = 0.0;
                        }
                        total = total + val_prod;

                        try {
                            if (Integer.parseInt(rs.getString("TP_ESTACAO")) > 0) {
                                totalTaxa = totalTaxa + (Double.parseDouble(rs.getString("VL_CONSUMO")) * Double.parseDouble(rs.getString("VL_INDICE")));
                            }
                        }catch (Exception e){
                            totalTaxa = total;
                        }

                        Log.i("INDICE", "doInBackground: "+rs.getString("VL_INDICE")+" - "+rs.getString("TP_ESTACAO"));
                        prod.add(new ListaProdModel(Long.parseLong(rs.getString("CD_PRODUTO")),rs.getString("DS_PRO"),rs.getString("QT_CONSUMO"), rs.getString("VL_CONSUMO"),Double.parseDouble(rs.getString("VL_CONSUMO")),Integer.parseInt(rs.getString("TP_ESTACAO")), Double.parseDouble(rs.getString("VL_INDICE")), rs.getString("UN_PRO")));
                    }else{
                        message = rs.getString("RETORNO");
                    }

                }
                conn.close();
                stmt.close();
                return 1;
            }
            catch(SQLException e1){
                e1.printStackTrace();
                return 0;
            }
        }

        @Override
        public void onPostExecute(Integer i) {
            if(i == 1){
                if(haveConta){
                    Log.i("TESTE","NUMERO DE ITENS: "+prod.size());
                    adapter.notifyDataSetChanged();
                    NumberFormat formatarFloat = new DecimalFormat("0.00");
                    pb.setVisibility(View.INVISIBLE);
                    subtotal.setText("R$ "+formatarFloat.format(total));
                    tvTotalTaxa.setText("R$ "+formatarFloat.format(totalTaxa));
                    tvTotalGeral.setText("R$ "+formatarFloat.format(total + totalTaxa));
                }else{
                    pb.setVisibility(View.GONE);
                    aviso.setText(message);
                    aviso.setVisibility(View.VISIBLE);
                }
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(MinhaContaActivity.this, R.style.YourDialogStyle);
                builder.setTitle("Falha ao conectar-se");
                builder.setMessage("Falha ao tentar conexão com o banco de dados. Verifique o sinal do WiFi e tente novamente.");
                builder.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pb.setVisibility(View.VISIBLE);
                        GetProduct gp = new GetProduct();
                        gp.execute();
                    }
                });
                builder.setNegativeButton("Fechar", null);
                builder.setCancelable(false);
                builder.show();
            }
        }
    }
}

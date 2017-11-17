package com.newpointer.projectlio.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.connection.DBLiteConnection;
import com.newpointer.projectlio.customAdapter.CarrinhoCustomAdapter;
import com.newpointer.projectlio.model.CarrinhoModel;
import com.readystatesoftware.viewbadger.BadgeView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;


public class CarinhoActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageButton back;
    private ImageButton clear;
    private FloatingActionButton send;
    private ProgressBar prog;
    private ListView lv_itens;
    private DBLiteConnection dbl;
    public static List<CarrinhoModel> carrinho;
    private BadgeView badge;
    private TextView qtd_itens;
    private ProgressDialog pd;
    private String comanda = "";
    private String smesa = "0";
    private Intent j;
    private int id_conta = 0;
    private String procedure_estacao = "";
    private String procedure_comanda = "";
    private String procedure_titulo = "";
    private String procedure_codoperador = "";
    private String procedure_cdprod = "";
    private String procedure_quant = "";
    private String procedure_acomp = "";
    private String procedure_obs = "";
    private String procedure_nomeoperador = "";
    private String procedure_mesa = "";
    private String procedure_valorindice = "";
    private String procedure_tpestacao = "1";

    private boolean contaAberta = false;
    private boolean hasResult = false;
    private boolean sqlResponse = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carinho);

        startVar();

        j = getIntent();
        comanda = j.getStringExtra("comanda");
        if(dbl.selectConfig().getPergunta_mesa() == 1){
            smesa = j.getStringExtra("mesa");
        }
        carrinho = dbl.selectCarrinho();
        qtd_itens.setText("Itens: "+carrinho.size());
        CarrinhoCustomAdapter cca = new CarrinhoCustomAdapter(CarinhoActivity.this,CarinhoActivity.this,carrinho);
        lv_itens.setAdapter(cca);
        prog.setVisibility(View.INVISIBLE);

        badge = MainActivity.badge;

    }

    private void startVar(){
        back = (ImageButton) findViewById(R.id.ib_carrinho_return);
        clear = (ImageButton) findViewById(R.id.ib_carrinho_clear);
        prog = (ProgressBar) findViewById(R.id.pb_carrinho_wait);
        lv_itens = (ListView) findViewById(R.id.lv_carrinho);
        send = (FloatingActionButton) findViewById(R.id.fab_carrinho_send);
        qtd_itens = (TextView) findViewById(R.id.tv_carrinho_nitens);
        dbl = DBLiteConnection.Companion.getInstance(this);
        send.setOnClickListener(this);
        back.setOnClickListener(this);
        clear.setOnClickListener(this);
        pd = new ProgressDialog(CarinhoActivity.this);
    }

    @Override
    public void onClick(View v) {
        if(v == send){
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.YourDialogStyle);
            builder.setTitle("Concluir pedido");
            builder.setMessage("Concluir pedido e enviar para cozinha?");
            builder.setPositiveButton("Concluir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enviarProdutos();
                }
            });
            builder.setNegativeButton("Cancelar", null);
            builder.setCancelable(false);
            builder.show();
        }
        if(v == back){
            finish();
        }
        if(v == clear){
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.YourDialogStyle);
            builder.setTitle("Apagar carrinho");
            builder.setMessage("Remover todos os itens do carrinho?");
            builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbl.deleteAllCarrinho();
                    Toast.makeText(CarinhoActivity.this, "Todos os itens foram removidos do carrinho", Toast.LENGTH_SHORT).show();
                    badge.setText("0");
                    finish();
                }
            });
            builder.setNegativeButton("Cancelar", null);
            builder.setCancelable(false);
            builder.show();
        }
    }

    private void enviarProdutos(){
        pd.setMessage("Testando conexão e enviando produtos... 15");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new EnviaProdutos().execute();
        new CountDownTimer(15000, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                pd.setMessage("Testando conexão e enviando produtos... " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                if(!hasResult){
                    if(!sqlResponse) {
                        pd.hide();
                        AlertDialog.Builder builder = new AlertDialog.Builder(CarinhoActivity.this, R.style.YourDialogStyle);
                        builder.setTitle("Problema ao enviar produtos");
                        builder.setMessage("Ocorreu um problema ao tentar enviar os produtos para o servidor e o tempo se esgotou, verifique sua conexão com a internet e clique em tentar novamente.");
                        builder.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                enviarProdutos();
                            }
                        });
                        builder.setNegativeButton("Cancelar", null);
                        builder.setCancelable(false);
                        builder.show();
                    }
                }
            }
        }.start();
    }

    public class EnviaProdutos extends AsyncTask<String, Object, Integer> {
        private String return_procedure = "";
        private boolean isError = false;
        private String error = "";

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
                int cont = 0;
                while (cont < carrinho.size()) {
                    procedure_cdprod = procedure_cdprod+carrinho.get(cont).getId_prod()+"|";
                    procedure_quant = procedure_quant+carrinho.get(cont).getQtd_prod()+"|";
                    procedure_acomp = procedure_acomp+carrinho.get(cont).getAcomp_prod()+"|";
                    procedure_obs = procedure_obs+carrinho.get(cont).getObs_prod()+"|";
                    cont++;
                }

                procedure_nomeoperador = dbl.selectCurrentOp().getName();
                procedure_comanda = comanda;
                procedure_mesa = smesa;
                procedure_codoperador = ""+dbl.selectCurrentOp().getId();
                procedure_titulo = dbl.selectConfig().getTitulo_loja();
                procedure_estacao = dbl.selectConfig().getEstacao();
                procedure_tpestacao = "1";
                procedure_valorindice = dbl.selectConfig().getTaxa()+"";

                Log.i("prod",procedure_cdprod);
                Log.i("qtd",procedure_quant);
                Log.i("obs",procedure_obs);
                Log.i("acomp",procedure_acomp);
                Log.i("nomeop",procedure_nomeoperador);
                Log.i("comanda",procedure_comanda);
                Log.i("codop",procedure_codoperador);
                Log.i("mesa",procedure_mesa);
                Log.i("estacao",procedure_estacao);
                Log.i("tpestacao",procedure_tpestacao);
                Log.i("taxa",procedure_valorindice);
                Log.i("titulo",procedure_titulo);

                Connection conn = DriverManager.getConnection("jdbc:firebirdsql://" + dbl.selectConfig().getString_bd() + "", props);
                String sSql = "execute procedure GRAVA_CONSUMO_ANDROID('"+procedure_estacao+"','"+procedure_comanda+"','"+procedure_titulo+"'," +
                        ""+procedure_codoperador+",'"+procedure_cdprod+"','"+procedure_quant+"','"+procedure_acomp+"','"+procedure_obs+"'," +
                        "'"+procedure_nomeoperador+"',"+procedure_tpestacao+","+procedure_valorindice+",'"+procedure_mesa+"')";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sSql);
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                while (rs.next()) {
                    hasResult = true;
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(",  ");
                        String columnValue = rs.getString(i);
                        System.out.print(columnValue + " " + rsmd.getColumnName(i));
                    }
                    System.out.println("");
                    if(rs.getString("RETORNO") == null) return_procedure = "null";
                    else return_procedure = rs.getString("RETORNO");
                    //return_procedure = rs.getString("RETORNO");
                }
                rs.close();
                conn.close();
                stmt.close();
                return 1;
            } catch (SQLException e1) {
                e1.printStackTrace();
                isError = true;
                error = e1.toString();
                return 0;
            }
        }

        @Override
        public void onPostExecute(Integer i) {
            procedure_nomeoperador = "";
            procedure_valorindice = "";
            procedure_acomp = "";
            procedure_tpestacao = "";
            procedure_codoperador = "";
            procedure_estacao = "";
            procedure_comanda = "";
            procedure_mesa = "";
            procedure_obs = "";
            procedure_quant = "";
            procedure_valorindice = "";
            procedure_titulo = "";
            pd.hide();
            if(i == 1){
                if(return_procedure.equalsIgnoreCase("OK")){
                    MainActivity.fa.finish();
                    pd.hide();
                    Toast.makeText(CarinhoActivity.this, "Produtos enviados com sucesso", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    if(return_procedure.equalsIgnoreCase("null")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(CarinhoActivity.this, R.style.YourDialogStyle);
                        builder.setTitle("Procedure returnou valor NULL. Favor comunicar o administrador");
                        builder.setMessage("Retorno da procedure: "+return_procedure);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        if(isError){
                            builder.setNegativeButton("Exibir Erro", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CarinhoActivity.this, R.style.YourDialogStyle);
                                    builder.setTitle("Log de erro");
                                    builder.setMessage(error);
                                    builder.setPositiveButton("Fechar", null);
                                    builder.setCancelable(false);
                                    builder.show();
                                }
                            });
                        }
                        builder.setCancelable(false);
                        builder.show();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(CarinhoActivity.this, R.style.YourDialogStyle);
                        builder.setTitle("Falha ao enviar produtos");
                        builder.setMessage("Retorno da procedure: "+return_procedure);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();
                    }
                }
            }else{
                sqlResponse = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(CarinhoActivity.this, R.style.YourDialogStyle);
                builder.setTitle("Falha ao conectar-se");

                builder.setMessage("Falha ao tentar conexão com o banco de dados. Verifique o sinal do WiFi e tente novamente.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                if(isError){
                    builder.setNegativeButton("Exibir Erro", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CarinhoActivity.this, R.style.YourDialogStyle);
                            builder.setTitle("Log de erro");
                            builder.setMessage(error);
                            builder.setPositiveButton("Fechar", null);
                            builder.setCancelable(false);
                            builder.show();
                        }
                    });
                }
                builder.setCancelable(false);
                builder.show();
            }

        }
    }
}

package com.newpointer.projectlio.activity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.connection.DBLiteConnection;
import com.newpointer.projectlio.model.ConferenciaModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class ConferenciaActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton back;
    private TextView aviso;
    private TableLayout tl;
    private DBLiteConnection dbl;
    private ProgressBar pb;
    private boolean haveConta;
    private String message;
    private List<ConferenciaModel> prod = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferencia);

        startVar();

        new ExecuteConferencia().execute();

    }

    private void startVar(){
        back = (ImageButton) findViewById(R.id.ib_conta_return);
        tl = (TableLayout) findViewById(R.id.tl_conferencia);
        pb = (ProgressBar) findViewById(R.id.pb_minhaconta);
        aviso = (TextView) findViewById(R.id.tv_minhaconta_aviso);

        dbl = DBLiteConnection.Companion.getInstance(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == back){
            finish();
        }
    }

    public class ExecuteConferencia extends AsyncTask<String,Object,Integer> {
        private String currentGerador = "";
        private List<ConferenciaModel.ConferenciaBean> itens = new ArrayList<>();
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
                String sSql = "EXECUTE PROCEDURE BUSCA_CONFERENCIA()";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sSql);
                while (rs.next()){
                    if(rs.getString("RETORNO").equalsIgnoreCase("OK")){
                        haveConta = true;
                        Log.i("Retorno",rs.getString("RETORNO"));
                        Log.i("PDS_PRO",rs.getString("PDS_PRO"));
                        Log.i("PQT_CONSUMO",rs.getString("PQT_CONSUMO"));
                        Log.i("PNR_GERADOR",rs.getString("PNR_GERADOR"));

                        if(!currentGerador.equalsIgnoreCase(rs.getString("PNR_GERADOR"))) {
                            if(itens.size()>0){
                                prod.add(new ConferenciaModel(currentGerador, itens));
                            }
                            currentGerador = rs.getString("PNR_GERADOR");
                            itens.clear();
                        }
                        itens.add(new ConferenciaModel.ConferenciaBean(rs.getString("PDS_PRO"),rs.getString("PQT_CONSUMO")));
                    }else{
                        message = rs.getString("RETORNO");
                    }
                }
                prod.add(new ConferenciaModel(currentGerador, itens));
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
                    int cont = 0;
                    while (cont < prod.size()){
                        TableRow tr = new TableRow(ConferenciaActivity.this);
                        tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                        tr.setWeightSum(1);

                        TextView mesa = new TextView(ConferenciaActivity.this);
                        mesa.setText(prod.get(cont).getMesa());
                        mesa.setTextColor(ContextCompat.getColor(ConferenciaActivity.this, R.color.colorWhite));
                        mesa.setTextSize(14);
                        mesa.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                        mesa.setBackgroundResource(R.drawable.border_radius);
                        mesa.setGravity(Gravity.CENTER);

                        tr.addView(mesa);
                        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                        for (ConferenciaModel.ConferenciaBean conferenciaBean : prod.get(cont).getItens()) {
                            TableRow tr1 = new TableRow(ConferenciaActivity.this);
                            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                            tr.setWeightSum(2);

                            TextView produto = new TextView(ConferenciaActivity.this);
                            produto.setText(conferenciaBean.getProduto());
                            produto.setTextSize(14);
                            produto.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                            produto.setBackgroundResource(R.drawable.border_radius);
                            produto.setGravity(Gravity.LEFT);

                            TextView quantidade = new TextView(ConferenciaActivity.this);
                            quantidade.setText(conferenciaBean.getQuantidade());
                            quantidade.setTextSize(14);
                            quantidade.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                            quantidade.setBackgroundResource(R.drawable.border_radius);
                            quantidade.setGravity(Gravity.RIGHT);

                            tr.addView(produto);
                            tr.addView(quantidade);
                            tl.addView(tr1, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                        }
                        cont++;
                    }
                }else{
                    pb.setVisibility(View.INVISIBLE);
                    aviso.setText(message);
                    aviso.setVisibility(View.VISIBLE);
                }
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(ConferenciaActivity.this, R.style.YourDialogStyle);
                builder.setTitle("Falha ao conectar-se");
                builder.setMessage("Falha ao tentar conexão com o banco de dados. Verifique o sinal do WiFi e tente novamente.");
                builder.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pb.setVisibility(View.VISIBLE);
                        new ExecuteConferencia().execute();
                    }
                });
                builder.setNegativeButton("Fechar", null);
                builder.setCancelable(false);
                builder.show();
            }
        }
    }
}

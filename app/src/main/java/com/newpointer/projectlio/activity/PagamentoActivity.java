package com.newpointer.projectlio.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.connection.DBLiteConnection;
import com.newpointer.projectlio.customAdapter.GetProductsFromPayment;
import com.newpointer.projectlio.customAdapter.HistoricoPagamentoAdapter;
import com.newpointer.projectlio.customAdapter.PergDocCustomDialog;
import com.newpointer.projectlio.customAdapter.PergValorCustomDialog;
import com.newpointer.projectlio.model.HistoricoPagamentoModel;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import cielo.orders.domain.Credentials;
import cielo.orders.domain.Order;
import cielo.sdk.order.OrderManager;
import cielo.sdk.order.ServiceBindListener;
import cielo.sdk.order.payment.PaymentError;
import cielo.sdk.order.payment.PaymentListener;

/**
 * Created by Felipe Silveira on 10/24/2017.
 */

public class PagamentoActivity extends AppCompatActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.mesa)
    TextView mesa;
    @BindView(R.id.group1)
    Group group1;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.aviso)
    TextView aviso;
    @BindView(R.id.listTitle)
    TextView listTitle;
    @BindView(R.id.formaPgto)
    TextView formaPgto;
    @BindView(R.id.valorPago)
    TextView valorPago;
    @BindView(R.id.pgtoHistory)
    RecyclerView pgtoHistory;
    @BindView(R.id.historico)
    ConstraintLayout historico;
    @BindView(R.id.subtotal)
    TextView subtotal;
    @BindView(R.id.restante)
    TextView restante;
    @BindView(R.id.dinheiro)
    Button dinheiro;
    @BindView(R.id.cartao)
    Button cartao;
    @BindView(R.id.voltar)
    FrameLayout voltar;
    @BindView(R.id.ll_start_button)
    LinearLayout llStartButton;
    private String nummesa = "";
    private String gerador = "";
    private DBLiteConnection dbl;
    private Double valorRestante = 0.0;
    private ArrayList<HistoricoPagamentoModel> listaPagamentos = new ArrayList<HistoricoPagamentoModel>();
    private HistoricoPagamentoAdapter adapter;
    private DecimalFormat formatarFloat = new DecimalFormat("0.00");

    private ProgressDialog pd;
    private int numberOfCalls = 0;

    private double totalGeral = 0.0;
    private double totalTaxa = 0.0;

    //SDK de pagamento
    private OrderManager orderManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);
        ButterKnife.bind(this);

        dbl = DBLiteConnection.Companion.getInstance(this);

        configureSDK();

        nummesa = getIntent().getStringExtra("numeroMesa");
        gerador = getIntent().getStringExtra("gerador");

        mesa.setText(gerador+": "+nummesa);
        pgtoHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        new GetProductsFromPayment(nummesa, this).execute();
        onClickFunctions();

        pd = new ProgressDialog(PagamentoActivity.this);
        pd.setMessage("Enviando dados de pagamento...");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
    }

    private void configureSDK(){
        Credentials credentials = new Credentials("PSxawUgstUHy", "hhzhdmJpXHoT");
        orderManager = new OrderManager(credentials, this);

        ServiceBindListener serviceBindListener = new ServiceBindListener() {
            @Override
            public void onServiceBound() {
                // O serviço está vinculado
                Log.i("SDK_PAGMENTO", "SERVICO VINCULADO");
            }

            @Override
            public void onServiceUnbound() {
                // O serviço foi desvinculado
                Log.i("SDK_PAGMENTO", "SERVICO DESVINCULADO");
            }
        };
        orderManager.bind(this, serviceBindListener);
    }

    private void getPayment(){
        listaPagamentos = dbl.getPgto(nummesa);
        adapter = new HistoricoPagamentoAdapter(listaPagamentos, this, this);
        pgtoHistory.setAdapter(adapter);

        if(listaPagamentos.size() > 0){
            for (HistoricoPagamentoModel listaPagamento : listaPagamentos) {
                valorRestante -= round(Double.parseDouble(listaPagamento.getValor()), 2);
            }
            restante.setText("Valor restante: R$ " + formatarFloat.format(valorRestante));
        }

        if(round(valorRestante, 2) == 0.00){
            openCFEDialog();
        }
    }

    private void onClickFunctions(){
        cartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PergValorCustomDialog pergValor = new PergValorCustomDialog(PagamentoActivity.this, PagamentoActivity.this, round(valorRestante, 2), PergValorCustomDialog.MODE_CARTAO);
                pergValor.setCancelable(false);
                pergValor.setCanceledOnTouchOutside(false);
                pergValor.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                pergValor.show();
            }
        });

        dinheiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PergValorCustomDialog pergValor = new PergValorCustomDialog(PagamentoActivity.this, PagamentoActivity.this, round(valorRestante, 2), PergValorCustomDialog.MODE_DINHEIRO);
                pergValor.setCancelable(false);
                pergValor.setCanceledOnTouchOutside(false);
                pergValor.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                pergValor.show();
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        voltar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                numberOfCalls++;
                if(numberOfCalls == 3){
                    Toast.makeText(PagamentoActivity.this, "Base de dados de pagamento apagada.", Toast.LENGTH_SHORT).show();
                    dbl.resetPgtoTable();
                    finish();
                }
                return true;
            }
        });
    }

    public void registerMoneyPayment(long valor){
        Double tot = valor / 100.00;
        valorRestante -= round(tot, 2);
        restante.setText("Valor restante: R$ " + formatarFloat.format(valorRestante));
        dbl.insertPgtoToDB(nummesa,"",HistoricoPagamentoAdapter.FORMA_PGTO_DINHEIRO,"", round(tot, 2)+"", "", "");
        listaPagamentos.add(new HistoricoPagamentoModel("","", HistoricoPagamentoAdapter.FORMA_PGTO_DINHEIRO, round(tot, 2)+"","",""));
        adapter.notifyDataSetChanged();

        if(round(valorRestante, 2) == 0.00){
            openCFEDialog();
        }
    }

    public void registerCardPayment(final long valor){
        Order order = orderManager.createDraftOrder("REFERÊNCIA_DO_PEDIDO");

        // Identificação do produto (Stock Keeping Unit)
        String sku = "1234567890";
        String name = "Item aleatorio";

        // Preço unitário em centavos
        int unitPrice = (int) valor;
        int quantity = 1;

        // Unidade de medida do produto String
        String unityOfMeasure = "UNIDADE";

        order.addItem(sku, name, unitPrice, quantity, unityOfMeasure);

        orderManager.placeOrder(order);

        PaymentListener paymentListener = new PaymentListener() {
            @Override
            public void onStart() {
                Log.i("SDK_PAGMENTO", "O pagamento começou.");
            }

            @Override
            public void onPayment(@NotNull Order order) {
                Log.i("SDK_PAGMENTO", "Um pagamento foi realizado. "+order.getPayments().get(0).getCieloCode()+" || "+order.getPayments().get(0).getPaymentFields().get("primaryProductName"));
                Double tot = valor / 100.00;
                valorRestante -= round(tot, 2);
                restante.setText("Valor restante: R$ " + formatarFloat.format(valorRestante));
                dbl.insertPgtoToDB(nummesa, order.getPayments().get(0).getCieloCode(), HistoricoPagamentoAdapter.FORMA_PGTO_CARTAO, order.getPayments().get(0).getPaymentFields().get("primaryProductName"), round(tot, 2)+"", order.getPayments().get(0).getPaymentFields().get("pan"), order.getPayments().get(0).getPaymentFields().get("cardLabelApplication"));
                listaPagamentos.add(new HistoricoPagamentoModel(order.getPayments().get(0).getCieloCode(), order.getPayments().get(0).getPaymentFields().get("primaryProductName"), HistoricoPagamentoAdapter.FORMA_PGTO_CARTAO, round(tot, 2)+"", order.getPayments().get(0).getPaymentFields().get("pan"), order.getPayments().get(0).getPaymentFields().get("cardLabelApplication")));
                adapter.notifyDataSetChanged();

                if(round(valorRestante, 2) == 0.00){
                    openCFEDialog();
                }
            }

            @Override public void onCancel() {
                Log.i("SDK_PAGMENTO", "A operação foi cancelada.");
                Toast.makeText(PagamentoActivity.this, "A operação foi cancelada.", Toast.LENGTH_LONG).show();
            }

            @Override public void onError(@NotNull PaymentError paymentError) {
                Log.i("SDK_PAGMENTO", "Houve um erro no pagamento.");
                AlertDialog.Builder builder = new AlertDialog.Builder(PagamentoActivity.this, R.style.YourDialogStyle);
                builder.setTitle("Houve um erro ao efetuar o pagamento");
                builder.setMessage("Verifique o sinal do aparelho, o modo de pagamento e/ou a senha digitada");
                builder.setPositiveButton("OK", null);
                builder.setCancelable(false);
                builder.show();
            }
        };

        String orderId = order.getId();
        long value = valor;

        orderManager.checkoutOrder(orderId, value, paymentListener);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public void openCFEDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PagamentoActivity.this, R.style.YourDialogStyle);
        builder.setTitle("Pagamento concluído");
        builder.setMessage("Deseja imprimir CF-e?");
        builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openCPFCNPJDialog();
            }
        });
        builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                concluirProcessamento("", 0);

            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void openCPFCNPJDialog(){
        PergDocCustomDialog pergDoc = new PergDocCustomDialog(PagamentoActivity.this, PagamentoActivity.this);
        pergDoc.setCancelable(false);
        pergDoc.setCanceledOnTouchOutside(false);
        pergDoc.show();
//        AlertDialog.Builder builder = new AlertDialog.Builder(PagamentoActivity.this, R.style.YourDialogStyle);
//        builder.setTitle("Documento");
//        builder.setMessage("Deseja incluir CPF ou CNPJ?");
//        builder.setPositiveButton("CPF", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                PergDocumentoCustomDialog pergValor = new PergDocumentoCustomDialog(PagamentoActivity.this, PagamentoActivity.this, PergDocumentoCustomDialog.MODE_CPF);
//                pergValor.setCancelable(false);
//                pergValor.setCanceledOnTouchOutside(false);
//                pergValor.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//                pergValor.show();
//            }
//        });
//        builder.setNegativeButton("CNPJ", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                PergDocumentoCustomDialog pergValor = new PergDocumentoCustomDialog(PagamentoActivity.this, PagamentoActivity.this, PergDocumentoCustomDialog.MODE_CNPJ);
//                pergValor.setCancelable(false);
//                pergValor.setCanceledOnTouchOutside(false);
//                pergValor.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//                pergValor.show();
//            }
//        });
//        builder.setCancelable(false);
//        builder.show();
    }

    public void concluirProcessamento(final String documento, int mode){
        pd.show();
        //final String pagamentos = "|DIN|0.01||||CREDITO|CRT|0.01|731895|8322|MasterCard|DEBITO|CRT|0.01|731896|4312|Debit|";
        String pagamentos = "";

        for (HistoricoPagamentoModel listaPagamento : listaPagamentos) {
            if(listaPagamento.getFormaDePagamento() == HistoricoPagamentoModel.FORMA_PGTO_MONEY){
                //pagamentos = pagamentos +"Dinheiro - R$"+listaPagamento.getValor()+" | NSU: "+listaPagamento.getNsu()+"\n";
                pagamentos = pagamentos+"|DIN|"+listaPagamento.getValor()+"|"+listaPagamento.getNsu()+"|"+listaPagamento.getBin().replace("*","")+"|"+listaPagamento.getOperadora()+"|";
            }else{
                //pagamentos = pagamentos +"Cartao("+listaPagamento.getTipoDePagamento()+") - R$"+listaPagamento.getValor()+" | NSU: "+listaPagamento.getNsu()+"\n";
                pagamentos = pagamentos+""+listaPagamento.getTipoDePagamento()+"|CRT|"+listaPagamento.getValor()+"|"+listaPagamento.getNsu()+"|"+listaPagamento.getBin().replace("*","")+"|"+listaPagamento.getOperadora()+"|";
            }
        }

        Log.d("PAGAMENTO",pagamentos);

        new FinalizaPagamento(pagamentos, documento).execute();
//                pd.dismiss();
//                AlertDialog.Builder builder = new AlertDialog.Builder(PagamentoActivity.this, R.style.YourDialogStyle);
//                builder.setTitle("Tudo certo");
//                builder.setMessage("O pagamento deste(a) "+gerador.toLowerCase()+" foi finalizado com sucesso!");
//                builder.setPositiveButton("FINALIZAR", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(PagamentoActivity.this, R.style.YourDialogStyle);
//                        builder.setTitle("Debug de dados");
//                        builder.setMessage(pagamentos);
//                        builder.setPositiveButton("FINALIZAR", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dbl.resetPgtoTable();
//                                finish();
//                            }
//                        });
//                        builder.setCancelable(false);
//                        builder.show();
//                    }
//                });
//                builder.setCancelable(false);
//                builder.show();
    }

    public class FinalizaPagamento extends AsyncTask<String, Object, Integer> {
        String pagamentos;
        String documento;
        private boolean isError = false;
        private String error = "";

        FinalizaPagamento(String pagamentos, String documento){
            this.pagamentos = pagamentos;
            this.documento = documento;
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

                Log.d("FECHACONTA", "PNM_ESTACAO: "+dbl.selectConfig().getEstacao());
                Log.d("FECHACONTA ","PNR_GERADOR: "+nummesa+"");
                Log.d("FECHACONTA ", "PFORMA_VL_PAGTO:"+ pagamentos);
                Log.d("FECHACONTA ", "PTOTAL_GERAL: "+totalGeral+"");
                Log.d("FECHACONTA ", "PVL_ACRESCIMO:"+ totalTaxa+"");
                Log.d("FECHACONTA ", "PCPF_CLIENTE: "+documento);
                Log.d("FECHACONTA ", "PCD_OPERADOR: "+dbl.selectCurrentOp().getId()+"");

                Connection conn = DriverManager.getConnection("jdbc:firebirdsql://" + dbl.selectConfig().getString_bd() + "", props);
                String sSql = "execute procedure FECHA_CONTA_ANDROID ('"+dbl.selectConfig().getEstacao()+"','"+nummesa+"','"+pagamentos+"'," +
                        ""+totalGeral+","+totalTaxa+",'"+documento+"',"+dbl.selectCurrentOp().getId()+")";
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
                Log.d("FECHACONTA", "TRUE: "+error);
                AlertDialog.Builder builder = new AlertDialog.Builder(PagamentoActivity.this, R.style.YourDialogStyle);
                builder.setTitle("Erro ao processar pagamento");
                builder.setMessage("Ocorreu um problema no processamento do pagamento, verifique o sinal do wi-fi e tente novamente.\n\nCaso o erro persista, entre em contato com o suporte.");
                builder.setPositiveButton("TENTAR NOVAMENTE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pd.show();
                        new FinalizaPagamento(pagamentos, documento).execute();
                    }
                });
                builder.setNegativeButton("EXIBIR ERRO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PagamentoActivity.this, R.style.YourDialogStyle);
                        builder.setTitle("Descrição do erro");
                        builder.setMessage(""+error);
                        builder.setPositiveButton("TENTAR NOVAMENTE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                pd.show();
                                new FinalizaPagamento(pagamentos, documento).execute();
                            }
                        });
                        builder.setPositiveButton("SAIR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(PagamentoActivity.this, R.style.YourDialogStyle);
                builder.setTitle("Tudo certo");
                builder.setMessage("O pagamento deste(a) "+gerador.toLowerCase()+" foi finalizado com sucesso!");
                builder.setPositiveButton("FINALIZAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbl.resetPgtoTable();
                        Intent i = new Intent();
                        i.setClass(PagamentoActivity.this, StartActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        }
    }

    public void setTotal(Double totalGeral, Double totalTaxa){
        pb.setVisibility(View.GONE);
        group1.setVisibility(View.VISIBLE);
        aviso.setVisibility(View.GONE);
        valorRestante = round(totalGeral,2);
        this.totalGeral = round(totalGeral, 2);
        this.totalTaxa = round(totalTaxa, 2);
        subtotal.setText("Total a pagar: R$ " + formatarFloat.format(totalGeral));
        restante.setText("Valor restante: R$ " + formatarFloat.format(valorRestante));

        getPayment();
    }

    public void noPaymentAvailable(){
        pb.setVisibility(View.GONE);
        group1.setVisibility(View.GONE);
        aviso.setVisibility(View.VISIBLE);
    }

    public void errorOnGetProduct(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PagamentoActivity.this, R.style.YourDialogStyle);
        builder.setTitle("Falha ao conectar-se");
        builder.setMessage("Falha ao tentar conexão com o banco de dados. Verifique o sinal do WiFi e tente novamente.");
        builder.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pb.setVisibility(View.VISIBLE);
                group1.setVisibility(View.GONE);
                aviso.setVisibility(View.GONE);
                new GetProductsFromPayment(nummesa, PagamentoActivity.this).execute();
            }
        });
        builder.setNegativeButton("Fechar", null);
        builder.setCancelable(false);
        builder.show();
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

        }
    }

    @Override
    protected void onDestroy() {
        orderManager.unbind();
        if(listaPagamentos.size() > 0){
            Log.d("FECHACONTA","JA COMECOU PAGAMENTO");
        }else{
            new OpenAndCloseAccount(nummesa, gerador, "A").execute();
        }
        super.onDestroy();
    }
}

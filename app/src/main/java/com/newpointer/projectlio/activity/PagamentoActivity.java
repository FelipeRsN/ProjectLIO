package com.newpointer.projectlio.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.newpointer.projectlio.customAdapter.PergDocumentoCustomDialog;
import com.newpointer.projectlio.customAdapter.PergValorCustomDialog;
import com.newpointer.projectlio.model.HistoricoPagamentoModel;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;

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
    }

    private void onClickFunctions(){
        cartao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PergValorCustomDialog pergValor = new PergValorCustomDialog(PagamentoActivity.this, PagamentoActivity.this, valorRestante, PergValorCustomDialog.MODE_CARTAO);
                pergValor.setCancelable(false);
                pergValor.setCanceledOnTouchOutside(false);
                pergValor.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                pergValor.show();
            }
        });

        dinheiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PergValorCustomDialog pergValor = new PergValorCustomDialog(PagamentoActivity.this, PagamentoActivity.this, valorRestante, PergValorCustomDialog.MODE_DINHEIRO);
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
        dbl.insertPgtoToDB(nummesa,"",HistoricoPagamentoAdapter.FORMA_PGTO_DINHEIRO,"", round(tot, 2)+"");
        listaPagamentos.add(new HistoricoPagamentoModel("","", HistoricoPagamentoAdapter.FORMA_PGTO_DINHEIRO, round(tot, 2)+""));
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
                dbl.insertPgtoToDB(nummesa, order.getPayments().get(0).getCieloCode(), HistoricoPagamentoAdapter.FORMA_PGTO_CARTAO, order.getPayments().get(0).getPaymentFields().get("primaryProductName"), round(tot, 2)+"");
                listaPagamentos.add(new HistoricoPagamentoModel(order.getPayments().get(0).getCieloCode(), order.getPayments().get(0).getPaymentFields().get("primaryProductName"), HistoricoPagamentoAdapter.FORMA_PGTO_CARTAO, round(tot, 2)+""));
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

    public void concluirProcessamento(String documento, int mode){
        //TODO ADICIONAR CHAMADA WS
        pd.show();
        new CountDownTimer(1000, 3000){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                pd.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(PagamentoActivity.this, R.style.YourDialogStyle);
                builder.setTitle("Tudo certo");
                builder.setMessage("O pagamento deste(a) "+gerador.toLowerCase()+" foi finalizado com sucesso!");
                builder.setPositiveButton("FINALIZAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String pagamentos = "";

                        for (HistoricoPagamentoModel listaPagamento : listaPagamentos) {
                            if(listaPagamento.getFormaDePagamento() == HistoricoPagamentoModel.FORMA_PGTO_MONEY){
                                pagamentos = pagamentos +"Dinheiro - R$"+listaPagamento.getValor()+" | NSU: "+listaPagamento.getNsu()+"\n";
                            }else{
                                pagamentos = pagamentos +"Cartao("+listaPagamento.getTipoDePagamento()+") - R$"+listaPagamento.getValor()+" | NSU: "+listaPagamento.getNsu()+"\n";
                            }
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(PagamentoActivity.this, R.style.YourDialogStyle);
                        builder.setTitle("Debug de dados");
                        builder.setMessage(pagamentos);
                        builder.setPositiveButton("FINALIZAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbl.resetPgtoTable();
                                finish();
                            }
                        });
                        builder.setCancelable(false);
                        builder.show();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        }.start();
    }

    public void setTotal(Double totalGeral){
        pb.setVisibility(View.GONE);
        group1.setVisibility(View.VISIBLE);
        aviso.setVisibility(View.GONE);
        valorRestante = totalGeral;
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

    @Override
    protected void onDestroy() {
        orderManager.unbind();
        super.onDestroy();
    }
}

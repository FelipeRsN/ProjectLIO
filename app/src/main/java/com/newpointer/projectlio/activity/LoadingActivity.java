package com.newpointer.projectlio.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.newpointer.projectlio.R;
import com.newpointer.projectlio.connection.DBLiteConnection;
import com.newpointer.projectlio.customAdapter.OperadorCustomDialog;
import com.newpointer.projectlio.model.ConfigModel;
import com.newpointer.projectlio.model.OperadorModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Properties;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoadingActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar prog;
    private ProgressBar prog_circle;
    private EditText string_bd;
    private EditText taxa;
    private EditText estacao;
    private Switch digito_verificador;
    private Switch pergunta_pesa;
    private FloatingActionButton next;
    private TextView status_system;
    private LinearLayout ll_form;
    private int progress = 0;
    private int progressSave = 0;
    private String string_ip = "";
    private String nMin = "";
    private String nMax = "";
    private String nTitle = "";
    private boolean hasResult = false;
    private String nStringBD = "";
    private String nStringEst = "";
    private Double nTaxa = 0.0;
    private int nDigitoVerif = 0;
    private int nPerguntaMesa = 0;
    private DBLiteConnection dbl;
    private int progressAnimationDuration = 2000;
    private ImageView iv_logo;
    private int clickCount = 1;
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
    private ImageButton bt_ok;
    private TextView op_cod;
    private TextView lioV1;
    private TextView lioV2;
    private TextView version;
    private RelativeLayout rl_keepdata;
    private Button bt_keepdata;
    private TextView tv_date;
    private Button bt_updatedata;
    private String Stringoperador = "";
    private RelativeLayout rl_operador;
    private boolean haveConfiguratedNow = false;
    private int selecModo;
    private int selecProd;
    private int selectedLio = 1;
    public static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        startVar();

        ctx = this;

        //connection
        //186.202.178.185:3050/c:/fenix/bd/ricco.fdb
        //191.252.58.113:3050/c:/fenix/bd/fenix.fdb
        //200.98.129.243:3050/c:/fenix/fenix.fdb

        //Atualizado
        //191.252.61.172:3050/c:/fenix/BD/ricco.fdb

        new CountDownTimer(300,1000) {
            @Override
            public void onTick(long millis) {
            }
            @Override
            public void onFinish() {
                progress = 20;
                ObjectAnimator animation = ObjectAnimator.ofInt(prog, "progress", progress);
                animation.setDuration(progressAnimationDuration);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();
                status_system.setText("Verificando conexão... 15");
                new CountDownTimer(300,1000) {
                    @Override
                    public void onTick(long millis) {
                    }
                    @Override
                    public void onFinish() {
                        progress = 40;
                        if(Build.VERSION.SDK_INT >= 11){
                            ObjectAnimator animation = ObjectAnimator.ofInt(prog, "progress", progress);
                            animation.setDuration(progressAnimationDuration);
                            animation.setInterpolator(new DecelerateInterpolator());
                            animation.start();
                        }else{
                            prog.setProgress(progress);
                        }
                        string_ip = "";
                        if(dbl.isConfigurated()){
                            string_ip = dbl.selectConfig().getString_bd().toString();
                            final TestaConn tc = new TestaConn();
                            tc.execute();
                            new CountDownTimer(15000, 1000){

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    if(!hasResult) {
                                        status_system.setText("Verificando conexão... " + millisUntilFinished / 1000);
                                    }
                                }

                                @Override
                                public void onFinish() {
                                    if(!hasResult){
                                        status_system.setText("Verificando conexão... 0");
                                        tc.cancel(true);
                                        if(dbl.haveProd() && dbl.haveFam()){
                                            tv_date.setText("Data da última atualização: "+dbl.selectConfig().getDbbkp_date());
                                            rl_keepdata.setVisibility(View.VISIBLE);
                                            prog_circle.setVisibility(View.INVISIBLE);
                                        }else {
                                            if(dbl.isConfigurated()){
                                                ConfigModel conf = dbl.selectConfig();
                                                string_bd.setText(conf.getString_bd().toString());
                                                estacao.setText(conf.getEstacao().toString());
                                                taxa.setText(conf.getTaxa()+"");
                                                if(conf.getPergunta_mesa() == 1) pergunta_pesa.setChecked(true);
                                                else pergunta_pesa.setChecked(false);
                                                if(conf.getDigito_verificador() == 1) digito_verificador.setChecked(true);
                                                else digito_verificador.setChecked(false);
                                                if(conf.getLio() == 1) enableLioV1();
                                                else enableLioV2();
                                            }else{
                                                string_bd.setText(nStringBD);
                                                estacao.setText(nStringEst);
                                                taxa.setText(nTaxa+"");
                                                if(nPerguntaMesa == 1) pergunta_pesa.setChecked(true);
                                                else pergunta_pesa.setChecked(false);
                                                if(nDigitoVerif == 1) digito_verificador.setChecked(true);
                                                else digito_verificador.setChecked(false);
                                            }
                                            status_system.setText("Falha de conexão.\nVerifique diretório e sinal wi-fi");
                                            ll_form.setVisibility(View.VISIBLE);
                                            next.setVisibility(View.VISIBLE);
                                            prog_circle.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                }
                            }.start();
                        }
                        else{
                            status_system.setText("Falha de conexão.\nVerifique diretório e sinal wi-fi");
                            prog_circle.setVisibility(View.INVISIBLE);
                            ll_form.setVisibility(View.VISIBLE);
                            next.setVisibility(View.VISIBLE);
                        }

                    }
                }.start();
            }
        }.start();
    }

    private void startVar(){
        prog = (ProgressBar) findViewById(R.id.pb_loading);
        prog_circle = (ProgressBar) findViewById(R.id.pb_loading_center);
        string_bd = (EditText) findViewById(R.id.et_loading_string);
        taxa = (EditText) findViewById(R.id.et_loading_taxa);
        estacao = (EditText) findViewById(R.id.et_loading_estacao);
        digito_verificador = (Switch) findViewById(R.id.sw_loading_digito);
        pergunta_pesa = (Switch) findViewById(R.id.sw_loading_mesa);
        next = (FloatingActionButton) findViewById(R.id.fab_loading_next);
        status_system = (TextView) findViewById(R.id.tv_loading_status);
        ll_form = (LinearLayout) findViewById(R.id.ll_loading_form);
        iv_logo = (ImageView) findViewById(R.id.iv_loading_logo);
        dbl = DBLiteConnection.Companion.getInstance(this);
        bt_0 = (Button) findViewById(R.id.bt_loading_0);
        bt_1 = (Button) findViewById(R.id.bt_loading_1);
        bt_2 = (Button) findViewById(R.id.bt_loading_2);
        bt_3 = (Button) findViewById(R.id.bt_loading_3);
        bt_4 = (Button) findViewById(R.id.bt_loading_4);
        bt_5 = (Button) findViewById(R.id.bt_loading_5);
        bt_6 = (Button) findViewById(R.id.bt_loading_6);
        bt_7 = (Button) findViewById(R.id.bt_loading_7);
        bt_8 = (Button) findViewById(R.id.bt_loading_8);
        bt_9 = (Button) findViewById(R.id.bt_loading_9);
        tv_date = (TextView) findViewById(R.id.tv_loading_bkpdate);
        bt_back = (ImageButton) findViewById(R.id.ib_loading_backspace);
        bt_ok = (ImageButton) findViewById(R.id.ib_loading_ok);
        op_cod = (TextView) findViewById(R.id.tv_loading_codigo);
        lioV1 = (TextView) findViewById(R.id.lioV1);
        lioV2 = (TextView) findViewById(R.id.lioV2);
        version = (TextView) findViewById(R.id.version);
        rl_operador = (RelativeLayout) findViewById(R.id.rl_loading_operador);
        rl_keepdata = (RelativeLayout) findViewById(R.id.rl_loading_connectionbkp);
        bt_keepdata = (Button) findViewById(R.id.bt_loading_keepbase);
        bt_updatedata = (Button) findViewById(R.id.bt_loading_attbase);
        bt_updatedata.setOnClickListener(this);
        bt_keepdata.setOnClickListener(this);
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
        next.setOnClickListener(this);
        bt_back.setOnClickListener(this);
        bt_ok.setOnClickListener(this);
        lioV2.setOnClickListener(this);
        lioV1.setOnClickListener(this);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version.setText("V "+pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version.setText("");
        }






        Retrofit retrofit = getRetrofitProprieties("https://smarketapi.azurewebsites.net/API/Login/");

    }

    public static Retrofit getRetrofitProprieties(String urlDaTalita){
        return new Retrofit.Builder()
                .baseUrl(urlDaTalita)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }





















    public void nextClicked(boolean comeFromDialog){
        if(!comeFromDialog && dbl.isConfigurated()){
            OperadorCustomDialog ocd = new OperadorCustomDialog(LoadingActivity.this, LoadingActivity.this, true);
            ocd.setCanceledOnTouchOutside(false);
            ocd.setCancelable(false);
            ocd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            ocd.show();
        }else {
            prog_circle.setVisibility(View.VISIBLE);
            ll_form.setVisibility(View.INVISIBLE);
            next.setVisibility(View.INVISIBLE);
            status_system.setText("Verificando conexão... 15");
            progress = progressSave;
            prog.setProgress(progress);
            new CountDownTimer(300, 1000) {
                @Override
                public void onTick(long millis) {
                }

                @Override
                public void onFinish() {
                    progress = 50;
                    if (Build.VERSION.SDK_INT >= 11) {
                        ObjectAnimator animation = ObjectAnimator.ofInt(prog, "progress", progress);
                        animation.setDuration(progressAnimationDuration);
                        animation.setInterpolator(new DecelerateInterpolator());
                        animation.start();
                    } else {
                        prog.setProgress(progress);
                    }
                    string_ip = string_bd.getText().toString();
                    haveConfiguratedNow = true;
                    nStringBD = string_bd.getText().toString();
                    nStringEst = estacao.getText().toString();
                    nTaxa = Double.parseDouble(taxa.getText().toString());
                    if (pergunta_pesa.isChecked()) nPerguntaMesa = 1;
                    else nPerguntaMesa = 0;
                    if (digito_verificador.isChecked()) nDigitoVerif = 1;
                    else nDigitoVerif = 0;
                    dbl.deleteConfig();
                    dbl.insertConfig(nStringBD, nStringEst, nTaxa, nDigitoVerif, nPerguntaMesa, nTitle, nMin, nMax, "", selecModo, selecProd, 1, 0, selectedLio);
                    final TestaConn tc = new TestaConn();
                    tc.execute();
                    //Timeout
                    new CountDownTimer(10000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            if (!hasResult) {
                                status_system.setText("Verificando conexão... " + millisUntilFinished / 1000);
                            }
                        }

                        @Override
                        public void onFinish() {
                            if (!hasResult) {
                                status_system.setText("Verificando conexão... 0");
                                tc.cancel(true);
                                if (dbl.haveProd() && dbl.haveFam()) {
                                    tv_date.setText("Data da última atualização: " + dbl.selectConfig().getDbbkp_date());
                                    rl_keepdata.setVisibility(View.VISIBLE);
                                    prog_circle.setVisibility(View.INVISIBLE);
                                } else {
                                    if (dbl.isConfigurated()) {
                                        ConfigModel conf = dbl.selectConfig();
                                        string_bd.setText(conf.getString_bd().toString());
                                        estacao.setText(conf.getEstacao().toString());
                                        taxa.setText(conf.getTaxa() + "");
                                        if (conf.getPergunta_mesa() == 1)
                                            pergunta_pesa.setChecked(true);
                                        else pergunta_pesa.setChecked(false);
                                        if (conf.getDigito_verificador() == 1)
                                            digito_verificador.setChecked(true);
                                        else digito_verificador.setChecked(false);
                                        if (conf.getLio() == 1) enableLioV1();
                                        else enableLioV2();
                                    } else {
                                        string_bd.setText(nStringBD);
                                        estacao.setText(nStringEst);
                                        taxa.setText(nTaxa + "");
                                        if (nPerguntaMesa == 1) pergunta_pesa.setChecked(true);
                                        else pergunta_pesa.setChecked(false);
                                        if (nDigitoVerif == 1) digito_verificador.setChecked(true);
                                        else digito_verificador.setChecked(false);
                                    }
                                    status_system.setText("Falha de conexão.\nVerifique diretório e sinal wi-fi");
                                    ll_form.setVisibility(View.VISIBLE);
                                    next.setVisibility(View.VISIBLE);
                                    prog_circle.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                    }.start();
                }
            }.start();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == next){
            if(string_bd.getText().toString().length() > 16){
                if(estacao.getText().toString().length() > 3){
                    if(taxa.getText().toString().length() > 1){
                        nextClicked(false);
                    }else{
                        taxa.setError("Campo obrigatório, caso não use taxa digite 0.0");
                    }
                }else{
                    estacao.setError("Campo obrigatório para identificação do dispositivo e operador");
                }
            }else{
                string_bd.setError("Campo obrigatório para conexão");
            }
        }
        if(view == bt_ok){
            if(op_cod.getText().toString().length()<1){
                Toast.makeText(LoadingActivity.this, "Digito de operador obrigatório", Toast.LENGTH_SHORT).show();
            }else{
                OperadorModel op = dbl.selectOp(Integer.parseInt(Stringoperador));
                if(op == null){
                    Stringoperador = "";
                    op_cod.setText("");
                    Toast.makeText(LoadingActivity.this, "Operador não encontrado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoadingActivity.this, "Bem vindo "+op.getName(), Toast.LENGTH_SHORT).show();
                    dbl.deleteCurrentOP();
                    dbl.insertCurrentOp(op.getId(), op.getName());
                    Intent i = new Intent();
                    i.setClass(LoadingActivity.this, StartActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }
        if(view == bt_0){
            Stringoperador = op_cod.getText().toString();
            Stringoperador = Stringoperador+"0";
            op_cod.setText(Stringoperador);
        }
        if(view == bt_1){
            Stringoperador = op_cod.getText().toString();
            Stringoperador = Stringoperador+"1";
            op_cod.setText(Stringoperador);
        }
        if(view == bt_2){
            Stringoperador = op_cod.getText().toString();
            Stringoperador = Stringoperador+"2";
            op_cod.setText(Stringoperador);
        }
        if(view == bt_3){
            Stringoperador = op_cod.getText().toString();
            Stringoperador = Stringoperador+"3";
            op_cod.setText(Stringoperador);
        }
        if(view == bt_4){
            Stringoperador = op_cod.getText().toString();
            Stringoperador = Stringoperador+"4";
            op_cod.setText(Stringoperador);
        }
        if(view == bt_5){
            Stringoperador = op_cod.getText().toString();
            Stringoperador = Stringoperador+"5";
            op_cod.setText(Stringoperador);
        }
        if(view == bt_6){
            Stringoperador = op_cod.getText().toString();
            Stringoperador = Stringoperador+"6";
            op_cod.setText(Stringoperador);
        }
        if(view == bt_7){
            Stringoperador = op_cod.getText().toString();
            Stringoperador = Stringoperador+"7";
            op_cod.setText(Stringoperador);
        }
        if(view == bt_8){
            Stringoperador = op_cod.getText().toString();
            Stringoperador = Stringoperador+"8";
            op_cod.setText(Stringoperador);
        }
        if(view == bt_9){
            Stringoperador = op_cod.getText().toString();
            Stringoperador = Stringoperador+"9";
            op_cod.setText(Stringoperador);
        }
        if(view == bt_back){
            Stringoperador = op_cod.getText().toString();
            if (!Stringoperador.isEmpty()) Stringoperador = Stringoperador.substring(0, Stringoperador.length() - 1);
            op_cod.setText(Stringoperador);
        }
        if(view == bt_keepdata){
            progress = 101;
            if(android.os.Build.VERSION.SDK_INT >= 11){
                ObjectAnimator animation = ObjectAnimator.ofInt(prog, "progress", progress);
                animation.setDuration(progressAnimationDuration);
                animation.setInterpolator(new DecelerateInterpolator());
                animation.start();
            }else{
                prog.setProgress(progress);
            }
            rl_keepdata.setVisibility(View.INVISIBLE);
            status_system.setVisibility(View.INVISIBLE);
            prog_circle.setVisibility(View.INVISIBLE);
            rl_operador.setVisibility(View.VISIBLE);
        }
        if(view == bt_updatedata){
            dbl.deleteAllProd();
            dbl.deleteAllFam();
            dbl.deleteAllAcomp();
            dbl.deleteAllGrupoAcomp();
            dbl.deleteAllCarrinho();
            dbl.deleteAllOP();
            dbl.deleteAllOPFunc();
            dbl.deleteAllPerfilFunc();
            dbl.deleteAssociados();
            rl_keepdata.setVisibility(View.INVISIBLE);
            prog_circle.setVisibility(View.VISIBLE);
            status_system.setText("Atualizando dados...");
            CarregaDadosBD cdb = new CarregaDadosBD();
            cdb.execute();
        }
        if(view == lioV1){
            enableLioV1();
        }
        if(view == lioV2){
            enableLioV2();
        }
    }

    private void enableLioV1(){
        selectedLio = 1;
        lioV2.setBackgroundResource(R.drawable.filter_weekday_normal);
        lioV2.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        lioV1.setBackgroundResource(R.drawable.filter_weekday_selected);
        lioV1.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
    }

    private void enableLioV2(){
        selectedLio = 2;
        lioV1.setBackgroundResource(R.drawable.filter_weekday_normal);
        lioV1.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

        lioV2.setBackgroundResource(R.drawable.filter_weekday_selected);
        lioV2.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
    }

    public class TestaConn extends AsyncTask<String,Object,Integer> {
        private int i = 0;

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
                Connection conn = DriverManager.getConnection("jdbc:firebirdsql://"+string_ip+"", props);
                String sSql = "SELECT CD_CHAVE, VL_CHAVE FROM TAB_PARAM";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sSql);
                String aux = "";
                while (rs.next()){
                    hasResult = true;
                    i++;
                    aux = rs.getString("CD_CHAVE");
                    if(aux.equalsIgnoreCase("TITULO_NR_GERADOR")){
                        nTitle = rs.getString("VL_CHAVE");
                    }
                    if(aux.equalsIgnoreCase("NR_INICIAL_MANUAL")){
                        nMin = rs.getString("VL_CHAVE");
                    }
                    if(aux.equalsIgnoreCase("NR_FINAL_MANUAL")){
                        nMax = rs.getString("VL_CHAVE");
                    }
                }
                rs.close();
                conn.close();
                stmt.close();
                if(i == 0){
                    return 0;
                }else{
                    if(i>0) return 1;
                }
            }
            catch(SQLException e1){
                e1.printStackTrace();
                return 0;
            }
            return 0;
        }

        @Override
        public void onPostExecute(Integer i) {
            if(i == 1){
                hasResult = true;
                status_system.setText("Conexão estabelecida.\nVerificando dados...");
                new CountDownTimer(300,1000) {
                    @Override
                    public void onTick(long millis) {
                    }
                    @Override
                    public void onFinish() {
                        progress = 70;
                        prog.setProgress(progress);
                        String date = "";
                        int preconta = 0, conferencia = 0;
                        if(haveConfiguratedNow){
                            nStringBD = string_bd.getText().toString();
                            nStringEst = estacao.getText().toString();
                            nTaxa = Double.parseDouble(taxa.getText().toString());
                            if(digito_verificador.isChecked()) nDigitoVerif = 1;
                            else nDigitoVerif = 0;
                            if(pergunta_pesa.isChecked()) nPerguntaMesa = 1;
                            else nPerguntaMesa = 0;
                            date = "";
                            selecModo = 0;
                            selecProd = 1;
                        }else{
                            ConfigModel config = dbl.selectConfig();
                            nStringBD = config.getString_bd();
                            nStringEst = config.getEstacao();
                            nTaxa = config.getTaxa();
                            nDigitoVerif = config.getDigito_verificador();
                            nPerguntaMesa = config.getPergunta_mesa();
                            date = config.getDbbkp_date();
                            selecModo = config.getPhone_selection();
                            selecProd = config.getProduct_selection();
                            preconta = config.getPreconta();
                            conferencia = config.getConferencia();
                            if(config.getLio() == 1) enableLioV1();
                            else enableLioV2();
                        }

                        dbl.deleteConfig();
                        dbl.insertConfig(nStringBD, nStringEst, nTaxa, nDigitoVerif, nPerguntaMesa, nTitle, nMin, nMax, date, selecModo, selecProd,preconta,conferencia, selectedLio);

                        if(dbl.haveProd() && dbl.haveFam()){
                            tv_date.setText("Data da última atualização: "+dbl.selectConfig().getDbbkp_date());
                            rl_keepdata.setVisibility(View.VISIBLE);
                            prog_circle.setVisibility(View.INVISIBLE);
                        }else {
                            dbl.deleteAllProd();
                            dbl.deleteAllFam();
                            dbl.deleteAllAcomp();
                            dbl.deleteAllGrupoAcomp();
                            dbl.deleteAllCarrinho();
                            dbl.deleteAllOP();
                            dbl.deleteAllOPFunc();
                            dbl.deleteAllPerfilFunc();
                            status_system.setText("Atualizando dados...");
                            CarregaDadosBD cdb = new CarregaDadosBD();
                            cdb.execute();
                        }
                    }
                }.start();
            }
            else {
                hasResult = true;
                if(dbl.haveProd() && dbl.haveFam()){
                    tv_date.setText("Data da última atualização: "+dbl.selectConfig().getDbbkp_date());
                    rl_keepdata.setVisibility(View.VISIBLE);
                    prog_circle.setVisibility(View.INVISIBLE);
                }else {
                    if(dbl.isConfigurated()){
                        ConfigModel conf = dbl.selectConfig();
                        string_bd.setText(conf.getString_bd().toString());
                        estacao.setText(conf.getEstacao().toString());
                        taxa.setText(conf.getTaxa()+"");
                        if(conf.getPergunta_mesa() == 1) pergunta_pesa.setChecked(true);
                        else pergunta_pesa.setChecked(false);
                        if(conf.getDigito_verificador() == 1) digito_verificador.setChecked(true);
                        else digito_verificador.setChecked(false);
                        if(conf.getLio() == 1)enableLioV1();
                        else enableLioV2();
                    }else{
                        string_bd.setText(nStringBD);
                        estacao.setText(nStringEst);
                        taxa.setText(nTaxa+"");
                        if(nPerguntaMesa == 1) pergunta_pesa.setChecked(true);
                        else pergunta_pesa.setChecked(false);
                        if(nDigitoVerif == 1) digito_verificador.setChecked(true);
                        else digito_verificador.setChecked(false);
                    }
                    status_system.setText("Falha de conexão.\nVerifique diretório e sinal wi-fi");
                    ll_form.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    prog_circle.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    public class CarregaDadosBD extends AsyncTask<String,Object,Integer> {
        private int p = 0;
        private int f = 0;
        private int a = 0;
        private int ga = 0;
        private int o = 0;

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
                Connection conn = DriverManager.getConnection("jdbc:firebirdsql://"+string_ip+"", props);
                String sSql = "SELECT CD_PRO,DS_PRO, DS_RESUMIDA_PRO,UN_PRO,CDFAM_PRO,FL_IMPREMOTA_PRO,CD_IMPREMOTA_PRO,FL_ACOMPANHAMENTO_PRO,TP_ACOMPANHAMENTO_PRO, FL_HAB_ATALHO_ANDROID_PRO " +
                              "FROM PRODUTO P, FAMILIA F " +
                              "WHERE FL_ATIVO_PRO = 1 AND P.CDFAM_PRO = F.ID_FAM AND F.MOSTRAR_TOUCH_FAM = 1 AND P.FL_NAOVENDER_PRO <> 1 ORDER BY DS_PRO";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sSql);
                while(rs.next())
                {
                    p++;
                    String cd = rs.getString("CD_PRO");
                    int cdfam = Integer.parseInt(rs.getString("CDFAM_PRO"));
                    int cdimp = 0;
                    int flimp = 0;
                    int atalho = 0;
                    String acomp = "";
                    int flaco = 0;
                    if(rs.getString("FL_IMPREMOTA_PRO").equalsIgnoreCase("1")){
                        cdimp = Integer.parseInt(rs.getString("CD_IMPREMOTA_PRO"));
                        flimp = 1;
                    }
                    if(rs.getInt("FL_ACOMPANHAMENTO_PRO") == 1){
                        acomp = rs.getString("TP_ACOMPANHAMENTO_PRO");
                        flaco = 1;
                    }
                    if(rs.getInt("FL_HAB_ATALHO_ANDROID_PRO") == 1){
                        atalho = 1;
                    }
                    String ds = rs.getString("DS_RESUMIDA_PRO");
                    Log.i("DESC", "DS RESUMIDA: "+ds);
                    if(ds == null || ds.isEmpty() || ds.equalsIgnoreCase("") || ds.equalsIgnoreCase("null")){
                        ds = rs.getString("DS_PRO");
                        Log.i("DESC", "NAO TEM DS RESUMIDA, DS: "+ds);
                    }
                    dbl.insertProd(cd, ds, cdfam, rs.getString("UN_PRO"), flimp, cdimp, flaco, acomp, atalho);
                    Log.i("produtos",cd+" | "+rs.getString("DS_PRO")+" | "+cdfam+" | "+rs.getString("UN_PRO")+" | "+flimp+" | "+cdimp+" | "+flaco+" | "+acomp+" | "+atalho);
                }
                rs.close();

                String sSql2 = "SELECT ID_FAM, DS_FAM FROM FAMILIA WHERE FL_ATIVO_FAM = 1 and MOSTRAR_TOUCH_FAM = 1 ORDER BY DS_FAM";
                ResultSet rs2 = stmt.executeQuery(sSql2);
                while(rs2.next())
                {
                    f++;
                    int cd = Integer.parseInt(rs2.getString("ID_FAM"));
                    dbl.insertFam(cd, rs2.getString("DS_FAM"));
                }
                rs2.close();

                String sSql3 = "SELECT ID_GR_ACO, DS_GR_ACO, FL_SELECAO_GR_ACO FROM GRUPO_ACOMPANHAMENTO WHERE FL_ATIVO_GR_ACO = 1 ORDER BY DS_GR_ACO";
                ResultSet rs3 = stmt.executeQuery(sSql3);
                while(rs3.next())
                {
                    ga++;
                    int selecao;
                    int cd = Integer.parseInt(rs3.getString("ID_GR_ACO"));
                    if(rs3.getString("FL_SELECAO_GR_ACO") == null){
                         selecao = 0;
                    }else {
                         selecao = Integer.parseInt(rs3.getString("FL_SELECAO_GR_ACO"));
                    }
                    dbl.insertGrupoAcomp(cd, rs3.getString("DS_GR_ACO"), selecao);
                }
                rs3.close();

                String sSql4 = "SELECT ID_ACO, DS_ACO, CDGP_ACO FROM ACOMPANHAMENTO WHERE FL_ATIVO_ACO = 1 ORDER BY DS_ACO";
                ResultSet rs4 = stmt.executeQuery(sSql4);
                while(rs4.next())
                {
                    a++;
                    int cd = Integer.parseInt(rs4.getString("ID_ACO"));
                    int grupo = Integer.parseInt(rs4.getString("CDGP_ACO"));
                    dbl.insertAcomp(cd, rs4.getString("DS_ACO"), grupo);
                }
                rs4.close();

                String sSql5 = "SELECT * FROM USUARIO WHERE FL_ATIVO_USU = 1";
                ResultSet rs5 = stmt.executeQuery(sSql5);
                while(rs5.next())
                {
                    o++;
                    int cd = Integer.parseInt(rs5.getString("ID_USU"));
                    int flprim = 0;
                    int flinic = 0;
                    int flperfil = 0;
                    int cdperfil = 0;
                    if(rs5.getString("FL_PRIMEIRO_ACESSO_USU") == null || rs5.getString("FL_PRIMEIRO_ACESSO_USU").equalsIgnoreCase("null")){
                        flprim = 0;
                    }else{
                        if(rs5.getString("FL_PRIMEIRO_ACESSO_USU").equalsIgnoreCase("1")){
                            flprim = 1;
                        }
                    }
                    if(rs5.getString("FL_INICIAR_USU") == null || rs5.getString("FL_INICIAR_USU").equalsIgnoreCase("null")){
                        flinic = 0;
                    }else{
                        if(rs5.getString("FL_INICIAR_USU").equalsIgnoreCase("1")){
                            flinic = 1;
                        }
                    }

                    String senha = "";
                    if(flinic == 0 && flprim == 0) senha = rs5.getString("SENHA_USU");

                    if(rs5.getString("FL_PERFIL_USU") == null || rs5.getString("FL_PERFIL_USU").equalsIgnoreCase("null")){
                        flperfil = 0;
                    }else{
                        if(rs5.getString("FL_PERFIL_USU").equalsIgnoreCase("1")){
                            flperfil = 1;
                            if(rs5.getString("CD_PERFIL_USU") == null || rs5.getString("CD_PERFIL_USU").equalsIgnoreCase("null")){
                                cdperfil = 0;
                            }else{
                                cdperfil = Integer.parseInt(rs5.getString("CD_PERFIL_USU"));
                            }
                        }
                    }

                    dbl.insertOp(cd,rs5.getString("NM_USU"),flinic,flprim,senha,flperfil,cdperfil);
                }
                rs5.close();

                String sSql6 = "SELECT * FROM USUARIO_FUNCAO";
                ResultSet rs6 = stmt.executeQuery(sSql6);
                while(rs6.next()) {
                    dbl.insertOpFunc(Integer.parseInt(rs6.getString("CD_USUARIO")),rs6.getString("CD_FUNCAO"));
                }
                rs6.close();

                String sSql7 = "SELECT * FROM PERFIL_FUNCAO";
                ResultSet rs7 = stmt.executeQuery(sSql7);
                while(rs7.next()) {
                    dbl.insertPerfilFuncao(Integer.parseInt(rs7.getString("CD_PERFIL")),rs7.getString("CD_FUNCAO"));
                }
                rs7.close();

                String sSql8 = "SELECT * FROM PRODUTO_ASSOCIADO";
                ResultSet rs8 = stmt.executeQuery(sSql8);
                while(rs8.next()) {
                    Log.i("ASSOCIADOS", "ID_ASSOCIADO: "+rs8.getString("CDPROASSOCIADO_PAS")+" --- CD_PRO: "+rs8.getString("CDPRO_PRINCIPAL_PAS"));
                    dbl.insertAssociados(rs8.getString("CDPROASSOCIADO_PAS"),rs8.getString("CDPRO_PRINCIPAL_PAS"));
                }
                rs8.close();

                dbl.getAllAssociados();

                //////////////////////////////SELECT CD_PRO,DS_PRO,UN_PRO,CDFAM_PRO,FL_IMPREMOTA_PRO,CD_IMPREMOTA_PRO,FL_ACOMPANHAMENTO_PRO,TP_ACOMPANHAMENTO_PRO, FL_HAB_ATALHO_ANDROID_PRO " +
                //"FROM PRODUTO P, FAMILIA F " +
                //"WHERE FL_ATIVO_PRO = 1 AND P.CDFAM_PRO = F.ID_FAM AND F.MOSTRAR_TOUCH_FAM = 1 AND P.FL_NAOVENDER_PRO <> 1 ORDER BY DS_PRO
                // Log.i("produtos",cd+" | "+rs.getString("DS_PRO")+" | "+cdfam+" | "+rs.getString("UN_PRO")+" | "+flimp+" | "+cdimp+" | "+flaco+" | "+acomp+" | "+atalho);
//
//                String sSql8 = "INSERT INTO PRODUTO (CD_PRO, DS_PRO, UN_PRO, CDFAM_PRO, FL_IMPREMOTA_PRO,CD_IMPREMOTA_PRO,FL_ACOMPANHAMENTO_PRO,TP_ACOMPANHAMENTO_PRO,FL_HAB_ATALHO_ANDROID_PRO,FL_NAOVENDER_PRO, FL_ATIVO_PRO)" +
//                        " VALUES('00885909630998','Agua Com gas garrafa','UN', 3, 1, 3, 0, '', 0, 0, 1)";
//                stmt.executeUpdate(sSql8);
//

               // String sSql9 = "UPDATE PRODUTO SET CD_PRO = '07896830106080' WHERE CD_PRO = '00789693010680'";
//                        //" VALUES('00789693010680','Soro','UN', 3, 0, 0, 0, '', 0, 0, 1)";
           // stmt.executeUpdate(sSql9);


                //String sSql8 = "DELETE FROM PRODUTO WHERE CD_PRO = '07894900531008'";
               // stmt.executeUpdate(sSql8);

                //7894900531008
                //07894900531008
                //07894900531008

                rs7.close();
                conn.close();
                stmt.close();
            }
            catch(SQLException e1){
                e1.printStackTrace();
                return 0;
            }
            return 1;
        }

        @Override
        public void onPostExecute(Integer i) {
            if(p > 0 && f > 0 && o > 0){
                progress = 101;
                if(android.os.Build.VERSION.SDK_INT >= 11){
                    ObjectAnimator animation = ObjectAnimator.ofInt(prog, "progress", progress);
                    animation.setDuration(progressAnimationDuration);
                    animation.setInterpolator(new DecelerateInterpolator());
                    animation.start();
                }else{
                    prog.setProgress(progress);
                }
                status_system.setText("Base de dados atualizada.\nIniciando sistema...");
                new CountDownTimer(300,1000) {
                    @Override
                    public void onTick(long millis) {

                    }
                    @Override
                    public void onFinish() {
                        ConfigModel config = dbl.selectConfig();
                        Calendar cld = Calendar.getInstance();
                        String date = cld.get(Calendar.DAY_OF_MONTH)+"/"+(cld.get(Calendar.MONTH)+1)+"/"+cld.get(Calendar.YEAR);
                        dbl.deleteConfig();
                        dbl.insertConfig(config.getString_bd(),config.getEstacao(),config.getTaxa(),config.getDigito_verificador(),config.getPergunta_mesa(),config.getTitulo_loja(),config.getNmin_mesa(),config.getNmax_mesa(),date,config.getPhone_selection(),config.getProduct_selection(),config.getPreconta(),config.getConferencia(), config.getLio());
                        status_system.setVisibility(View.INVISIBLE);
                        prog_circle.setVisibility(View.INVISIBLE);
                        rl_operador.setVisibility(View.VISIBLE);
                    }
                }.start();
            }
            else {
                status_system.setText("Falha de conexão.\nVerifique diretório e sinal wi-fi");
                prog_circle.setVisibility(View.INVISIBLE);
                ConfigModel conf = dbl.selectConfig();
                string_bd.setText(conf.getString_bd());
                estacao.setText(conf.getEstacao());
                taxa.setText(conf.getTaxa()+"");
                if(conf.getPergunta_mesa() == 1) pergunta_pesa.setChecked(true);
                if(conf.getDigito_verificador() == 1) digito_verificador.setChecked(true);
                ll_form.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
            }
        }
    }
}

package com.newpointer.projectlio.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.newpointer.projectlio.R;
import com.newpointer.projectlio.connection.DBLiteConnection;
import com.newpointer.projectlio.customAdapter.FamilyCustomAdapter;
import com.newpointer.projectlio.customAdapter.ProductCustomAdapter;
import com.newpointer.projectlio.customAdapter.ProductDetailCustomDialog;
import com.newpointer.projectlio.customAdapter.ProductPesqCustomDialog;
import com.newpointer.projectlio.model.ConfigModel;
import com.newpointer.projectlio.model.FamilyModel;
import com.newpointer.projectlio.model.ProductModel;
import com.readystatesoftware.viewbadger.BadgeView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent i;
    private Intent j;
    private TextView tvncomanda;
    private TextView tvnmesa;
    private DBLiteConnection dbl;
    private ConfigModel config;
    private ImageButton iv_return;
    private Button bt_atalhos;
    private Button bt_family;
    private ImageButton bt_pad;
    private View view_atalhos;
    private View view_pad;
    private View view_family;
    private EditText auto_search;
    private ImageButton bt_search;
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
    private TextView prod_cod;
    private String StringCodigo = "";
    private RelativeLayout rl_pad;
    private RelativeLayout rl_atalhos;
    private RelativeLayout rl_family;
    private ImageButton carrinho;
    public static BadgeView badge;
    private ListView lv_family;
    private ListView lv_prod;
    private ImageButton return_family;
    private ProgressBar pb_family;
    private TextView tfam;
    private ArrayList<FamilyModel> fm;
    private ArrayList<ProductModel> pm;
    private ArrayList<ProductModel> atalhos;
    private ListView lv_atalho;
    private ProgressBar pb_atalho;
    private TextView status_atalho;
    private String scomanda;
    private String snmesa;
    private ImageView scanner_click;
    public static Activity fa;
    private Barcode barcodeResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fa = this;
        startVar();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        scomanda = j.getStringExtra("numeroMesa");
        ;
        tvncomanda.setText(config.getTitulo_loja() + ": " + scomanda);
        if (config.getPergunta_mesa() == 1) {
            snmesa = j.getStringExtra("mesa");
            tvnmesa.setText("MESA: " + snmesa);
            tvnmesa.setVisibility(View.VISIBLE);
        }

        if (config.getPhone_selection() == 1) {
            final float scale = getResources().getDisplayMetrics().density;
            int pixels = (int) (100 * scale + 0.5f);
            bt_0.getLayoutParams().width = pixels;
            bt_0.getLayoutParams().height = pixels;
            bt_1.getLayoutParams().width = pixels;
            bt_1.getLayoutParams().height = pixels;
            bt_2.getLayoutParams().width = pixels;
            bt_2.getLayoutParams().height = pixels;
            bt_3.getLayoutParams().width = pixels;
            bt_3.getLayoutParams().height = pixels;
            bt_4.getLayoutParams().width = pixels;
            bt_4.getLayoutParams().height = pixels;
            bt_5.getLayoutParams().width = pixels;
            bt_5.getLayoutParams().height = pixels;
            bt_6.getLayoutParams().width = pixels;
            bt_6.getLayoutParams().height = pixels;
            bt_7.getLayoutParams().width = pixels;
            bt_7.getLayoutParams().height = pixels;
            bt_8.getLayoutParams().width = pixels;
            bt_8.getLayoutParams().height = pixels;
            bt_9.getLayoutParams().width = pixels;
            bt_9.getLayoutParams().height = pixels;
            bt_ok.getLayoutParams().width = pixels;
            bt_ok.getLayoutParams().height = pixels;
            int font = (int) (40 * scale + 0.5f);
            bt_0.setTextSize(font);
            bt_1.setTextSize(font);
            bt_2.setTextSize(font);
            bt_3.setTextSize(font);
            bt_4.setTextSize(font);
            bt_5.setTextSize(font);
            bt_6.setTextSize(font);
            bt_7.setTextSize(font);
            bt_8.setTextSize(font);
            bt_9.setTextSize(font);
        }

        switch (config.getProduct_selection()) {
            case 0:
                bt_atalhos.callOnClick();
                break;
            case 1:
                bt_pad.callOnClick();
                break;
            case 2:
                bt_family.callOnClick();
                break;
        }

        fm = dbl.selectAllFam();

        FamilyCustomAdapter fca = new FamilyCustomAdapter(MainActivity.this, MainActivity.this, fm);
        lv_family.setAdapter(fca);
        pb_family.setVisibility(View.INVISIBLE);
        lv_family.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pb_family.setVisibility(View.VISIBLE);
                pm = dbl.selectProdByFam(fm.get(position).getId());
                tfam.setText(fm.get(position).getName());
                ProductCustomAdapter pca = new ProductCustomAdapter(MainActivity.this, MainActivity.this, pm);
                lv_prod.setAdapter(pca);
                return_family.setVisibility(View.VISIBLE);
                lv_family.setVisibility(View.INVISIBLE);
                lv_prod.setVisibility(View.VISIBLE);
                pb_family.setVisibility(View.INVISIBLE);
            }
        });
        lv_prod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ProductDetailCustomDialog pdcd = new ProductDetailCustomDialog(MainActivity.this, MainActivity.this, pm.get(position), badge);
                pdcd.setCanceledOnTouchOutside(false);
                pdcd.setCancelable(false);
                pdcd.show();
            }
        });

        lv_prod.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ProductModel p = atalhos.get(position);
                String tobs = "";
                dbl.insertProdCarrinho(p.getId(),p.getName(),1,"",tobs);
                int b = Integer.parseInt(badge.getText().toString());
                b++;
                badge.setText(b+"");
                Toast.makeText(MainActivity.this, "Produto adicionado ao carrinho", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        if (dbl.foundSelectProdAtalhos()) {
            atalhos = dbl.selectProdByAtalho();
            ProductCustomAdapter pca = new ProductCustomAdapter(MainActivity.this, MainActivity.this, atalhos);
            lv_atalho.setAdapter(pca);
            pb_atalho.setVisibility(View.INVISIBLE);
        } else {
            status_atalho.setVisibility(View.VISIBLE);
            pb_atalho.setVisibility(View.INVISIBLE);
        }

        lv_atalho.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductDetailCustomDialog pdcd = new ProductDetailCustomDialog(MainActivity.this, MainActivity.this, atalhos.get(position), badge);
                pdcd.setCanceledOnTouchOutside(false);
                pdcd.setCancelable(false);
                pdcd.show();
            }
        });
        lv_atalho.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ProductModel p = atalhos.get(position);
                String tobs = "";
                dbl.insertProdCarrinho(p.getId(),p.getName(),1,"",tobs);
                int b = Integer.parseInt(badge.getText().toString());
                b++;
                badge.setText(b+"");
                Toast.makeText(MainActivity.this, "Produto adicionado ao carrinho", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    private void startVar() {
        dbl = DBLiteConnection.Companion.getInstance(this);
        config = dbl.selectConfig();
        tvncomanda = (TextView) findViewById(R.id.tv_main_comanda);
        tvnmesa = (TextView) findViewById(R.id.tv_main_nmesa);
        iv_return = (ImageButton) findViewById(R.id.ib_main_return);
        bt_atalhos = (Button) findViewById(R.id.bt_main_atalhos);
        bt_family = (Button) findViewById(R.id.bt_main_family);
        bt_pad = (ImageButton) findViewById(R.id.ib_main_pad);
        bt_search = (ImageButton) findViewById(R.id.ib_main_search);
        auto_search = (EditText) findViewById(R.id.et_main_descprod);
        view_atalhos = (View) findViewById(R.id.view_main_atalhos);
        view_pad = (View) findViewById(R.id.view_main_pad);
        view_family = (View) findViewById(R.id.view_main_family);
        tfam = (TextView) findViewById(R.id.tv_main_ifamily);
        bt_0 = (Button) findViewById(R.id.bt_main_0);
        bt_1 = (Button) findViewById(R.id.bt_main_1);
        bt_2 = (Button) findViewById(R.id.bt_main_2);
        bt_3 = (Button) findViewById(R.id.bt_main_3);
        bt_4 = (Button) findViewById(R.id.bt_main_4);
        bt_5 = (Button) findViewById(R.id.bt_main_5);
        bt_6 = (Button) findViewById(R.id.bt_main_6);
        bt_7 = (Button) findViewById(R.id.bt_main_7);
        bt_8 = (Button) findViewById(R.id.bt_main_8);
        bt_9 = (Button) findViewById(R.id.bt_main_9);
        bt_back = (ImageButton) findViewById(R.id.ib_main_backspace);
        bt_ok = (ImageButton) findViewById(R.id.ib_main_ok);
        prod_cod = (TextView) findViewById(R.id.tv_main_codigo);
        rl_pad = (RelativeLayout) findViewById(R.id.rl_main_pad);
        rl_family = (RelativeLayout) findViewById(R.id.rl_main_fam);
        return_family = (ImageButton) findViewById(R.id.ib_main_return_family);
        lv_family = (ListView) findViewById(R.id.lv_main_family);
        lv_prod = (ListView) findViewById(R.id.lv_main_product);
        pb_family = (ProgressBar) findViewById(R.id.pb_main_family);
        carrinho = (ImageButton) findViewById(R.id.ib_main_carrinho);
        pb_atalho = (ProgressBar) findViewById(R.id.pb_main_atalho);
        lv_atalho = (ListView) findViewById(R.id.lv_main_atalho);
        rl_atalhos = (RelativeLayout) findViewById(R.id.rl_main_atalho);
        status_atalho = (TextView) findViewById(R.id.tv_main_statusatalho);
        scanner_click = (ImageView) findViewById(R.id.iv_main_scan);
        badge = new BadgeView(this, carrinho);
        dbl.deleteAllCarrinho();
        badge.setText("0");
        badge.setBadgeBackgroundColor(Color.parseColor("#FFFFFF"));
        badge.setTextColor(Color.parseColor("#035c8c"));
        badge.show();


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
        bt_ok.setOnClickListener(this);
        iv_return.setOnClickListener(this);
        bt_search.setOnClickListener(this);
        bt_atalhos.setOnClickListener(this);
        bt_pad.setOnClickListener(this);
        bt_family.setOnClickListener(this);
        carrinho.setOnClickListener(this);
        return_family.setOnClickListener(this);
        scanner_click.setOnClickListener(this);

        i = new Intent();
        j = getIntent();

        auto_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                bt_search.callOnClick();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (dbl.haveProdInCarrinho()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.YourDialogStyle);
            builder.setTitle("Sair do pedido");
            builder.setMessage("Existem produtos pendentes no carrinho, deseja apagar todos os itens e sair?");
            builder.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dbl.deleteAllCarrinho();
                    finish();
                }
            });
            builder.setNegativeButton("Cancelar", null);
            builder.setCancelable(false);
            builder.show();
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == iv_return) {
            if (dbl.haveProdInCarrinho()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.YourDialogStyle);
                builder.setTitle("Sair do pedido");
                builder.setMessage("Existem produtos pendentes no carrinho, deseja apagar todos os itens e sair?");
                builder.setPositiveButton("Apagar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbl.deleteAllCarrinho();
                        finish();
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.setCancelable(false);
                builder.show();
            } else {
                finish();
            }
        }
        if (view == bt_search) {
            String search = auto_search.getText().toString();
            auto_search.setText("");
            if (dbl.foundSelectProdByName(search)) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                ProductPesqCustomDialog ppcd = new ProductPesqCustomDialog(MainActivity.this, MainActivity.this, badge, search);
                ppcd.setCancelable(false);
                ppcd.setCanceledOnTouchOutside(false);
                ppcd.show();
            } else {
                Toast.makeText(MainActivity.this, "Nenhum produto encontrado com este nome", Toast.LENGTH_SHORT).show();

            }

        }
        if (view == return_family) {
            pb_family.setVisibility(View.VISIBLE);
            lv_prod.setVisibility(View.INVISIBLE);
            lv_prod.setAdapter(null);
            tfam.setText("Famílias");
            return_family.setVisibility(View.INVISIBLE);
            lv_family.setVisibility(View.VISIBLE);
            pb_family.setVisibility(View.INVISIBLE);
        }
        if (view == bt_atalhos) {
            view_atalhos.setVisibility(View.VISIBLE);
            view_pad.setVisibility(View.INVISIBLE);
            view_family.setVisibility(View.INVISIBLE);
            rl_pad.setVisibility(View.INVISIBLE);
            rl_family.setVisibility(View.INVISIBLE);
            lv_family.setVisibility(View.VISIBLE);
            rl_atalhos.setVisibility(View.VISIBLE);
            lv_prod.setVisibility(View.INVISIBLE);
            tfam.setText("Famílias");
            return_family.setVisibility(View.INVISIBLE);
        }
        if (view == bt_pad) {
            view_pad.setVisibility(View.VISIBLE);
            rl_pad.setVisibility(View.VISIBLE);
            view_family.setVisibility(View.INVISIBLE);
            view_atalhos.setVisibility(View.INVISIBLE);
            rl_family.setVisibility(View.INVISIBLE);
            rl_atalhos.setVisibility(View.INVISIBLE);
            lv_family.setVisibility(View.VISIBLE);
            lv_prod.setVisibility(View.INVISIBLE);
            tfam.setText("Famílias");
            return_family.setVisibility(View.INVISIBLE);
        }
        if (view == bt_family) {
            view_family.setVisibility(View.VISIBLE);
            view_pad.setVisibility(View.INVISIBLE);
            view_atalhos.setVisibility(View.INVISIBLE);
            rl_pad.setVisibility(View.INVISIBLE);
            rl_family.setVisibility(View.VISIBLE);
            rl_atalhos.setVisibility(View.INVISIBLE);
        }
        if (view == bt_0) {
            StringCodigo = prod_cod.getText().toString();
            StringCodigo = StringCodigo + "0";
            prod_cod.setText(StringCodigo);
        }
        if (view == bt_1) {
            StringCodigo = prod_cod.getText().toString();
            StringCodigo = StringCodigo + "1";
            prod_cod.setText(StringCodigo);
        }
        if (view == bt_2) {
            StringCodigo = prod_cod.getText().toString();
            StringCodigo = StringCodigo + "2";
            prod_cod.setText(StringCodigo);
        }
        if (view == bt_3) {
            StringCodigo = prod_cod.getText().toString();
            StringCodigo = StringCodigo + "3";
            prod_cod.setText(StringCodigo);
        }
        if (view == bt_4) {
            StringCodigo = prod_cod.getText().toString();
            StringCodigo = StringCodigo + "4";
            prod_cod.setText(StringCodigo);
        }
        if (view == bt_5) {
            StringCodigo = prod_cod.getText().toString();
            StringCodigo = StringCodigo + "5";
            prod_cod.setText(StringCodigo);
        }
        if (view == bt_6) {
            StringCodigo = prod_cod.getText().toString();
            StringCodigo = StringCodigo + "6";
            prod_cod.setText(StringCodigo);
        }
        if (view == bt_7) {
            StringCodigo = prod_cod.getText().toString();
            StringCodigo = StringCodigo + "7";
            prod_cod.setText(StringCodigo);
        }
        if (view == bt_8) {
            StringCodigo = prod_cod.getText().toString();
            StringCodigo = StringCodigo + "8";
            prod_cod.setText(StringCodigo);
        }
        if (view == bt_9) {
            StringCodigo = prod_cod.getText().toString();
            StringCodigo = StringCodigo + "9";
            prod_cod.setText(StringCodigo);
        }
        if (view == bt_back) {
            StringCodigo = prod_cod.getText().toString();
            if (!StringCodigo.isEmpty())
                StringCodigo = StringCodigo.substring(0, StringCodigo.length() - 1);
            prod_cod.setText(StringCodigo);
        }
        if (view == carrinho) {
            if (dbl.haveProdInCarrinho()) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                Intent i = new Intent();
                i.setClass(MainActivity.this, CarinhoActivity.class);
                i.putExtra("comanda", scomanda);
                if (config.getPergunta_mesa() == 1) {
                    i.putExtra("mesa", snmesa);
                }
                startActivity(i);
            } else {
                Toast.makeText(MainActivity.this, "Nenhum item adicionado ao carrinho", Toast.LENGTH_SHORT).show();
            }
        }
        if (view == scanner_click) {
            int MyVersion = Build.VERSION.SDK_INT;
            if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("Permissão de acesso")
                            .setMessage("Para usar a camera do dispositivo, é necessário permissão de acesso. Aperte em Permitir para continuar.")
                            .setPositiveButton("Permitir", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, MaterialBarcodeScanner.RC_HANDLE_CAMERA_PERM);
                                }
                            })
                            .setNegativeButton("Agora não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainActivity.this, "Não é possivel escanerar produtos sem permissão de camera", Toast.LENGTH_LONG).show();
                                }
                            }).show();
                } else {
                    startScan();
                }
            }else{
                startScan();
            }
        }
        if (view == bt_ok) {
            if (prod_cod.getText().toString().length() > 0) {
                String cod_digitado = String.format("%14s", prod_cod.getText().toString()).replace(' ', '0');
                Log.i("SEARCH", cod_digitado);
                ProductModel p = dbl.getProdByAssociadoAndCode(cod_digitado);
                if (p.getName().equalsIgnoreCase("")) {
                    Toast.makeText(MainActivity.this, "Produto não encontrado", Toast.LENGTH_SHORT).show();
                    prod_cod.setText("");
                    StringCodigo = "";
                } else {
                    prod_cod.setText("");
                    StringCodigo = "";
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    ProductDetailCustomDialog pdcd = new ProductDetailCustomDialog(MainActivity.this, MainActivity.this, p, badge);
                    pdcd.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    pdcd.setCanceledOnTouchOutside(false);
                    pdcd.setCancelable(false);
                    pdcd.show();
                }
            } else {
                if (dbl.haveProdInCarrinho()) {
                    Intent i = new Intent();
                    i.setClass(MainActivity.this, CarinhoActivity.class);
                    i.putExtra("comanda", scomanda);
                    if (config.getPergunta_mesa() == 1) {
                        i.putExtra("mesa", snmesa);
                    }
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Nenhum item adicionado ao carrinho", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void startScan() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(MainActivity.this)
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withCenterTracker()
                .withTrackerColor(Color.WHITE)
                .withText("Procurando código de barras...")
                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        barcodeResult = barcode;
                        prod_cod.setText(barcode.rawValue);
                        String cod_digitado = String.format("%14s", prod_cod.getText().toString()).replace(' ', '0');
                        Log.i("SEARCH", cod_digitado);
                        ProductModel p = dbl.getProdByAssociadoAndCode(cod_digitado);
                        if (p.getName().equalsIgnoreCase("")) {
                            Toast.makeText(MainActivity.this, "Produto não encontrado", Toast.LENGTH_SHORT).show();
                            prod_cod.setText("");
                            StringCodigo = "";
                        } else {
                            prod_cod.setText("");
                            StringCodigo = "";
                            if(p.getUnit().equalsIgnoreCase("UN") && p.getFl_imp() == 0){
                                String tobs = "";
                                dbl.insertProdCarrinho(p.getId(),p.getName(),1,"",tobs);
                                int b = Integer.parseInt(badge.getText().toString());
                                b++;
                                badge.setText(b+"");
                                Toast.makeText(MainActivity.this, "Produto adicionado ao carrinho", Toast.LENGTH_SHORT).show();
                                prod_cod.setText("");
                            }else {
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                                ProductDetailCustomDialog pdcd = new ProductDetailCustomDialog(MainActivity.this, MainActivity.this, p, badge);
                                pdcd.setCanceledOnTouchOutside(false);
                                pdcd.setCancelable(false);
                                pdcd.show();
                            }
                        }
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MaterialBarcodeScanner.RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScan();
            return;
        }
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(android.R.string.ok, listener)
                .show();
    }
}
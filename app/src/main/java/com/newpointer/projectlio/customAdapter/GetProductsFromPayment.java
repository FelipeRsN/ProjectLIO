package com.newpointer.projectlio.customAdapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.newpointer.projectlio.activity.PagamentoActivity;
import com.newpointer.projectlio.connection.DBLiteConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created by Felipe Silveira on 10/22/2017.
 */

public class GetProductsFromPayment extends AsyncTask<String,Object,Integer> {
    private String mesa;
    private DBLiteConnection dbl;
    private Context context;
    private Double totalTaxa = 0.0;
    private double total = 0.0;
    private boolean haveConta = false;
    private boolean isError = false;
    private String message;

    public GetProductsFromPayment(String mesa, Context context) {
        this.mesa = mesa;
        dbl = DBLiteConnection.Companion.getInstance(context);
        this.context = context;
    }

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
                ((PagamentoActivity)context).setTotal(total + totalTaxa);
            }else{
                ((PagamentoActivity)context).noPaymentAvailable();
            }
        }else{
            ((PagamentoActivity)context).errorOnGetProduct();
        }
    }
}
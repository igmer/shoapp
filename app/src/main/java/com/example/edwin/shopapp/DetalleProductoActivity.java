package com.example.edwin.shopapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DetalleProductoActivity extends AppCompatActivity {
    String codigo,url;
    ImageView imDetalle;
    Button btnAdd;
    static ArrayList pedido=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);
        Bundle bundle = getIntent().getExtras();
        codigo = bundle.getString("codigo");
        url = bundle.getString("url");
        imDetalle =(ImageView)findViewById(R.id.imDetalleProducto);
        Picasso.get().load(url).into(imDetalle);
        btnAdd = (Button)findViewById(R.id.btnConfirmar);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),codigo,Toast.LENGTH_LONG).show();
                pedido.add(codigo);
            }
        });

    }
    public Integer addPedido(String codigoProd){
        Connection connect; connect = ConexionSQL.ConnectionHelper();
        String mensaje_error="";
        Integer existe=0;
        try {
            Statement st = connect.createStatement();
            String comando="INSERT INTO PRODUCTO";
            ResultSet rs = st.executeQuery(comando);
            while (rs.next()) {
                existe++;
            }
            connect.close();
        } catch (Exception e) {
            Log.i("Error**",e.getMessage());
            existe=0;
        }
        return existe;
    }
}

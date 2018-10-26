package com.example.edwin.shopapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DetalleProductoActivity extends AppCompatActivity {
    String codigo,url;
    ImageView imDetalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);
        Bundle bundle = getIntent().getExtras();
        codigo = bundle.getString("codigo");
        url = bundle.getString("url");
        imDetalle =(ImageView)findViewById(R.id.imDetalleProducto);

        Picasso.get().load(url).into(imDetalle);

    }
}

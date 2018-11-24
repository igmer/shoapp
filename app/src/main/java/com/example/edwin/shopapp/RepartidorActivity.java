package com.example.edwin.shopapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class RepartidorActivity extends AppCompatActivity {
    ListViewAdapterRepartidor adapter;
    ArrayList<String> pedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartidor);
        Utilidades u = new Utilidades();
        pedidos = u.getListaRepartidor();
        adapter = new ListViewAdapterRepartidor(getApplicationContext(), pedidos);
        ListView lista = (ListView)findViewById(R.id.lvPedidosRepartidor);
        lista.setAdapter(adapter);

    }
}

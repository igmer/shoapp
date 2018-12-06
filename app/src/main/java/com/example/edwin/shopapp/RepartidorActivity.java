package com.example.edwin.shopapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RepartidorActivity extends AppCompatActivity {
    ListViewAdapterRepartidor adapter;
    ArrayList<String> pedidos;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartidor);
        pref = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        String usuario  = pref.getString("usuarioBD","");
        Utilidades u = new Utilidades();
        pedidos = u.getListaRepartidor(usuario);
        adapter = new ListViewAdapterRepartidor(getApplicationContext(), pedidos);
        ListView lista = (ListView)findViewById(R.id.lvPedidosRepartidor);
        lista.setAdapter(adapter);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getApplicationContext(),pedidos.get(i), Toast.LENGTH_LONG).show();
                String fila = pedidos.get(i);
                String[] parts = fila.split("!/");

                Intent intent = new Intent(getApplicationContext(),RepartidorMaps.class);
                intent.putExtra("codigo",parts[0]);
                intent.putExtra("cliente",parts[2]+" "+parts[3]);
                startActivity(intent);
                finish();

            }
        });
    }
}

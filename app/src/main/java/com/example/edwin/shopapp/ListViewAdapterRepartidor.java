package com.example.edwin.shopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListViewAdapterRepartidor extends BaseAdapter{
    Context context;
    ArrayList<String> lista;

    public ListViewAdapterRepartidor(Context context, ArrayList<String> lista) {
        this.context = context;
        this.lista = lista;
    }


    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        // Declare Variables
        TextView tvProducto;
        TextView tvCliente;
        TextView tvDireccion;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.fial_repartidor, viewGroup, false);


        try {

            // Locate the TextViews in listview_item.xml
            tvProducto = (TextView) itemView.findViewById(R.id.repProducto);
            tvCliente = (TextView) itemView.findViewById(R.id.repCliente);
            tvDireccion = (TextView) itemView.findViewById(R.id.repDireccion);


            String fila = lista.get(position); // aqui tengo cada fila del arrayList
            String[] parts = fila.split("!/"); //partimos la cadena por el - con el que concatemanos al crear el arrayList

            tvCliente.setText(parts[1]);
            tvProducto.setText(parts[0]);
            tvDireccion.setText(parts[2]+" "+parts[3]);

        }catch (Exception e){
            e.printStackTrace();
        }
        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html


        return itemView;
    }
}

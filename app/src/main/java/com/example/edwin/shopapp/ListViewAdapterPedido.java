package com.example.edwin.shopapp;

/**
 * Created by Edwin on 9/20/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListViewAdapterPedido extends BaseAdapter {
    // Declare Variables
    Context context;

    ArrayList<String> lista;

    LayoutInflater inflater;

    public ListViewAdapterPedido(Context context, ArrayList<String> lista) {
        this.context = context;
      this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        // Declare Variables
        TextView tvNombre;
        TextView tvDescripcion;
        TextView tvModelo;
        TextView tvPrecio;
        ImageView img;

        //http://developer.android.com/intl/es/reference/android/view/LayoutInflater.html
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.filapedido, parent, false);

        // Locate the TextViews in listview_item.xml
        tvNombre = (TextView) itemView.findViewById(R.id.tvNombrePed);
        tvDescripcion = (TextView) itemView.findViewById(R.id.tvDescripcionPed);
       // tvModelo = (TextView) itemView.findViewById(R.id.tvModeloPed);
        tvPrecio = (TextView) itemView.findViewById(R.id.tvPrecioPed);
        img = (ImageView) itemView.findViewById(R.id.imgcartaPed);

        String fila = lista.get(position); // aqui tengo cada fila del arrayList
        String[] parts = fila.split("!/"); //partimos la cadena por el - con el que concatemanos al crear el arrayList

        tvNombre.setText(parts[2]);
        tvDescripcion.setText(parts[4]);
        //tvModelo.setText("");
        tvPrecio.setText(parts[3]);
        Picasso.get().load("http://www.geoshop.somee.com/UpImg/"+parts[1]).into(img);

        return itemView;
    }
}
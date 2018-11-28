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

public class ListViewAdapter extends BaseAdapter {
    // Declare Variables
    Context context;

    ArrayList<String> lista;

    LayoutInflater inflater;

    public ListViewAdapter(Context context,ArrayList<String> lista) {
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

        View itemView = inflater.inflate(R.layout.filaproducto, parent, false);
        try{
            tvNombre = (TextView) itemView.findViewById(R.id.tvNombre);
            tvDescripcion = (TextView) itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = (TextView) itemView.findViewById(R.id.tvPrecio);
            img = (ImageView) itemView.findViewById(R.id.imgcarta);

            String fila = lista.get(position); // aqui tengo cada fila del arrayList
            String[] parts = fila.split("!-"); //partimos la cadena por el - con el que concatemanos al crear el arrayList

            tvNombre.setText(parts[1]);
            tvDescripcion.setText(parts[3]);
            //tvModelo.setText(parts[0]); // esta es la Url pero la usaremos luego para cargar la imagen
            tvPrecio.setText(parts[2]);
            Picasso.get().load("http://www.geoshop.somee.com/UpImg/"+parts[0]).into(img);

        }catch (Exception e){e.printStackTrace();}

        return itemView;
    }
}
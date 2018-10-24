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

public class ListViewAdapter extends BaseAdapter {
    // Declare Variables
    Context context;

    String[] nombre;
    String[] descripcion;
    String[] modelo;
    String[] precio;
    int[] imagenes;

    LayoutInflater inflater;

    public ListViewAdapter(Context context, String[] nombre_, int[] imagenes_,  String[] descripcion_, String[] modelo_, String[] precio_) {
        this.context = context;
        this.nombre = nombre_;
        this.descripcion = descripcion_;
        this.modelo=modelo_;
        this.precio =precio_;
        this.imagenes = imagenes_;
    }

    @Override
    public int getCount() {
        return nombre.length;
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

        // Locate the TextViews in listview_item.xml
        tvNombre = (TextView) itemView.findViewById(R.id.tvNombre);
        tvDescripcion = (TextView) itemView.findViewById(R.id.tvDescripcion);
        tvModelo = (TextView) itemView.findViewById(R.id.tvModelo);
        tvPrecio = (TextView) itemView.findViewById(R.id.tvPrecio);
        img = (ImageView) itemView.findViewById(R.id.imgcarta);

        // Capture position and set to the TextViews
        tvNombre.setText(nombre[position]);
        tvDescripcion.setText(descripcion[position]);
        tvModelo.setText(modelo[position]);
        tvPrecio.setText(precio[position]);
        img.setImageResource(imagenes[position]);

        return itemView;
    }
}
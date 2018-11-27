package com.example.edwin.shopapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.edwin.shopapp.SQLite.SQLiteLocal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ListaComprasFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ArrayList<String> categorias;
    ListViewAdapterPedido adapter;
    ArrayAdapter adaptador_categoria;
    private SharedPreferences pref;
    Utilidades u;
    ListView lista;



    private OnFragmentInteractionListener mListener;

    public ListaComprasFragment() {

    }


    public static ListaComprasFragment newInstance(String param1, String param2) {
        ListaComprasFragment fragment = new ListaComprasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_lista_compras, container, false);
        u = new Utilidades();
        pref = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        categorias = u.getListaPedido(getContext());
        lista = v.findViewById(R.id.lvCarritoCompras);
        FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categorias.size()>0){
                    Intent intent = new Intent(getActivity(),ConfrimarEntregaActivity.class);
                    startActivity(intent);
                    //confirm();
                }else {
                    Toast.makeText(getContext(),"No tiene productos agregados a su carrito",Toast.LENGTH_LONG).show();
                }
            }
        });
        if (categorias.size()>0){
            adapter = new ListViewAdapterPedido(getActivity(), categorias);
            lista.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(getActivity(),String.valueOf(categorias.get(i)),Toast.LENGTH_LONG).show();
                try {
                    String[] fila = categorias.get(i).split("!/");
                    String codigoPedido = fila[0];
                    int exito=deleteLocalProducto(Integer.parseInt(codigoPedido));
                    adapter.notifyDataSetChanged();
                    categorias = u.getListaPedido(getContext());
                    adapter = new ListViewAdapterPedido(getActivity(), categorias);
                    lista.setAdapter(adapter);

                }catch (Exception e){
                    e.printStackTrace();
                }


                return true;
            }
        });

        return v;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
    /*public void confirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(Html.fromHtml("<font color='#FF0000'><b>Confirma la compra?</b></font>"))
                .setNegativeButton(Html.fromHtml("Cancelar"), null)
                .setPositiveButton(Html.fromHtml("Sí, Comprar"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        agregarProducto addProd = new agregarProducto();
                        addProd.execute("");


                    }
                })
                .setCancelable(false);
        //.create().show();
        AlertDialog a = builder.create();
        a.show();
        Button btnPositivo = a.getButton(DialogInterface.BUTTON_POSITIVE);
        btnPositivo.setTextColor(Color.RED);
        Button btnNegativo = a.getButton(DialogInterface.BUTTON_NEGATIVE);
        btnNegativo.setTextColor(Color.GREEN);
    }*/


    public int deleteLocalProducto(int codigoPedido){
        int exito;
        try {
            SQLiteLocal helper = new SQLiteLocal(getActivity().getApplicationContext());
            SQLiteDatabase db = helper.getWritableDatabase();
            String deletepedido="DELETE FROM DetallePedido WHERE codigo ="+codigoPedido;
            db.execSQL(deletepedido);
            exito = 1;
            db.close();
        }catch (Exception e){
            e.printStackTrace();
            exito =0;
        }

        return exito;
    }
}

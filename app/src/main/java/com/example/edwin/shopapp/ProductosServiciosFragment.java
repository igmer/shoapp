package com.example.edwin.shopapp;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductosServiciosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductosServiciosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductosServiciosFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btnSalir, btnInfo;
    Spinner spnCategorias;
    ArrayAdapter adaptador_categoria;
    String [] nombre;
    String [] descripcion;
    String [] modelo;
    String [] precio;
    ArrayList<String> categorias;
    ListViewAdapter adapter;

    private OnFragmentInteractionListener mListener;

    public ProductosServiciosFragment() {
        // Required empty public constructor
    }

    public static ProductosServiciosFragment newInstance(String param1, String param2) {
        ProductosServiciosFragment fragment = new ProductosServiciosFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_productos_servicios, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        Utilidades u = new Utilidades();

        //nombre=getResources().getStringArray(R.array.NomProducto);
       // modelo=getResources().getStringArray(R.array.ModeloProducto);
      //  descripcion=getResources().getStringArray(R.array.DescProducto);
      //  precio=getResources().getStringArray(R.array.PrecioProducto);
        categorias = u.getListaProducto();

        spnCategorias =(Spinner) getActivity().findViewById(R.id.ListaCategoria);
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,categoriaProducto());
        spnCategorias.setAdapter(adapterCategories);

         ListView lista = (ListView) getActivity().findViewById(R.id._Lista);
         adapter = new ListViewAdapter(getActivity(), categorias);
       // adaptador_categoria = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,categorias);


        lista.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String fila =categorias.get(position);
                String[] parts = fila.split("!!");
                String[] partsUrl = fila.split("!-");
                String codigo = parts[1]; //el codigo lo separamos por dos !!
                String url = partsUrl[0];

                Intent i = new Intent(getContext(),DetalleProductoActivity.class);
                i.putExtra("codigo",codigo);
                i.putExtra("url",url);
                startActivity(i);

            }
        });


    }

    // TODO: Rename method, update argument and hook method into UI event
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

    public ArrayList<String> categoriaProducto(){
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Todas");
        categories.add("Tarjeta de Video");
        categories.add("Cases");
        categories.add("Discos Duros");
        categories.add("Procesadores");
        categories.add("Memorias RAM");
        categories.add("Ventiladores");
        return categories;
    }

}

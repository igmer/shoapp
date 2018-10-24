package com.example.edwin.shopapp;

import android.app.ListFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;


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
    String [] categorias;

    ListViewAdapter adapter;
    int[] imagen={R.drawable.uno, R.drawable.dos, R.drawable.tres, R.drawable.cuatro, R.drawable.cinco, R.drawable.seis};
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

        nombre=getResources().getStringArray(R.array.NomProducto);
        modelo=getResources().getStringArray(R.array.ModeloProducto);
        descripcion=getResources().getStringArray(R.array.DescProducto);
        precio=getResources().getStringArray(R.array.PrecioProducto);
        categorias =getResources().getStringArray(R.array.Categorias_Productos);

        spnCategorias =(Spinner) getActivity().findViewById(R.id.ListaCategoria);

        final ListView lista = (ListView) getActivity().findViewById(R.id._Lista);
        adapter = new ListViewAdapter(getActivity(), nombre, imagen,descripcion,modelo,precio);
        adaptador_categoria = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,categorias);
        spnCategorias.setAdapter(adaptador_categoria);
        lista.setAdapter(adapter);

     /*   listView = (ListView)getView().findViewById(R.id.listview);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_expandable_list_item_1,elementos);
        listView.setAdapter(adapter); */

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

}

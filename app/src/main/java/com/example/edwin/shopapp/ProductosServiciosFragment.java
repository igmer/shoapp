package com.example.edwin.shopapp;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
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
    Utilidades u;

    Button btnSalir, btnInfo;
    Spinner spnCategorias;
    ArrayAdapter adaptador_categoria;
    String [] nombre;
    String [] descripcion;
    String [] modelo;
    String [] precio;
    ArrayList<String> categorias;
    ListViewAdapter adapter;
    ListView lista;
    ProgressBar pbWaiting;

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
        u = new Utilidades();

        //nombre=getResources().getStringArray(R.array.NomProducto);
       // modelo=getResources().getStringArray(R.array.ModeloProducto);
      //  descripcion=getResources().getStringArray(R.array.DescProducto);
      //  precio=getResources().getStringArray(R.array.PrecioProducto);
       // categorias = u.getListaProducto("todas");

        spnCategorias =(Spinner) getActivity().findViewById(R.id.ListaCategoria);
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,categoriaProducto());
        spnCategorias.setAdapter(adapterCategories);

         lista = (ListView) getActivity().findViewById(R.id._Lista);
         pbWaiting = (ProgressBar) getView().findViewById(R.id.pbWaiting);
        // adapter = new ListViewAdapter(getActivity(), categorias);
        spnCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              //  Toast.makeText(getContext(),String.valueOf(i),Toast.LENGTH_SHORT).show();
                String cat = getCodigoCategoria(i);
                Principal p = new Principal();
                p.execute(cat);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


       // lista.setAdapter(adapter);
        //adapter.notifyDataSetChanged();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String fila =categorias.get(position);
                String[] parts = fila.split("!!");
                String[] partsUrl = fila.split("!-");
                String codigo = parts[1]; //el codigo lo separamos por dos !!
                String url = partsUrl[0];
                String nombre = partsUrl[1];
                String precio = partsUrl[2];
                String descripcion = partsUrl[3];
                Intent i = new Intent(getContext(),DetalleProductoActivity.class);
                i.putExtra("codigo",codigo);
                i.putExtra("url",url);
                i.putExtra("nombre",nombre);
                i.putExtra("precio",precio);
                i.putExtra("descripcion",descripcion);
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
        categories.add("Tarjeta SD");
        categories.add("Memoria USB");
        categories.add("Tarjeta de video");
        categories.add("Cases");
        categories.add("Disco Duro");
        categories.add("Prodcesadores");
        categories.add("Memoria RAM");
        categories.add("Ventidalores");
        return categories;
    }
    public String getCodigoCategoria(int position){
        String categoria = "";
        if (position==1){
            categoria= "007";
        }else if (position==2){
            categoria= "008";
        }else if (position==3){
            categoria= "AAA";
        }else if (position==4){
            categoria= "CAS";
        }else if (position==5){
            categoria= "HDD";
        }else if (position==6){
            categoria= "PRO";
        }else if (position==7){
            categoria= "RAM";
        }else if (position==8){
            categoria= "VEN";
        }else{
            categoria= "todas";
        }

        return categoria;
    }
    class Principal extends AsyncTask<String, Void, ListViewAdapter>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbWaiting.setVisibility(View.VISIBLE);
            if (categorias!= null){
                adapter.notifyDataSetChanged();
                categorias.clear();
            }
        }

        @Override
        protected ListViewAdapter doInBackground(String... strings) {
            try {
                if (categorias != null){
                    categorias.clear();
                }
                String cat = strings[0];
                categorias = u.getListaProducto(cat);
                adapter = new ListViewAdapter(getActivity(), categorias);
                adapter.notifyDataSetChanged();

            }catch (Exception e){
                e.printStackTrace();
            }


            return adapter;
        }

        @Override
        protected void onPostExecute(ListViewAdapter adapterf) {
            super.onPostExecute(adapterf);
            adapterf.notifyDataSetChanged();
            lista.setAdapter(adapterf);
            adapterf.notifyDataSetChanged();
            pbWaiting.setVisibility(View.INVISIBLE);

        }
    }

}

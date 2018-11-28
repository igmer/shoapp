package com.example.edwin.shopapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class UbicacionPedidoFragment extends Fragment  implements OnMapReadyCallback {

    //MapView mMapView;
   private GoogleMap mMap;
    private SharedPreferences pref;



    private OnFragmentInteractionListener mListener;

    public UbicacionPedidoFragment() {
        // Required empty public constructor
    }

    public static UbicacionPedidoFragment newInstance(String param1, String param2) {
        UbicacionPedidoFragment fragment = new UbicacionPedidoFragment();
        Bundle args = new Bundle();

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
        View view = inflater.inflate(R.layout.fragment_ubicacion_pedido, container, false);
        pref = this.getActivity().getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        final SupportMapFragment myMAPF = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        myMAPF.getMapAsync(this);

        return view;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng= new LatLng(13.700073, -89.200170);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
        if (mMap != null) {
            Marker hamburg = mMap.addMarker(new MarkerOptions().position(latLng));
            hamburg.setTitle("Bodega Principal");
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(getActivity(),String.valueOf(latLng),Toast.LENGTH_SHORT).show();
            }
        });
        getMisCompras compras = new getMisCompras();
        compras.execute();

    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    class getMisCompras extends AsyncTask<Void,Void,ArrayList<String>>{

        @Override
        protected ArrayList<String>  doInBackground(Void... voids) {
            String usuario  = pref.getString("usuarioBD","");//obtnemos el usuario
            ArrayList<String> pedidos = new ArrayList<>();

            try {
                Connection connect; connect = ConexionSQL.ConnectionHelper();
                Statement st = connect.createStatement();
                String comando="SELECT idEstado, latitud, longitud, latidudDestino, longituDestino,codigo FROM Pedidos WHERE idcliente='"+usuario+"'";
                ResultSet rs = st.executeQuery(comando);
                while (rs.next()) { // el pimer elemento que ira el la lista sera la Url de la imagen
                    pedidos.add(rs.getString(1)+"!/"
                               +rs.getString(2)+"!/"
                               +rs.getString(3)+"!/"
                               +rs.getString(4)+"!/"
                               +rs.getString(5)+"!/"
                               +rs.getString(6)
                    );

                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            String mensaje_error="";

            return pedidos;
        }

        @Override
        protected void onPostExecute(ArrayList<String> pedidos) {
            //super.onPostExecute(strings);
            if (pedidos.size()>0){
                for (int i = 0; i <pedidos.size() ; i++) {
                    String fila = pedidos.get(i);
                    String[] parts = fila.split("!/");
                    Float latDestino = Float.parseFloat(parts[3]);
                    Float lonDestino = Float.parseFloat(parts[4]);
                    Float latOrigen = Float.parseFloat(parts[1]);
                    Float lonOrigen = Float.parseFloat(parts[2]);
                    if (parts[0].equals("NVO")){
                        mMap.addMarker(new MarkerOptions().title("En bodega cod-"+parts[5]).position(new LatLng(latOrigen,lonOrigen)).icon(BitmapDescriptorFactory.fromResource(R.drawable.enbodega)));
                    }
                    if(parts[0].equals("ENC")){
                        mMap.addMarker(new MarkerOptions().title("En camino cod-"+parts[5]).position(new LatLng(latOrigen,lonOrigen)).icon(BitmapDescriptorFactory.fromResource(R.drawable.encamino)));
                    }
                    if(parts[0].equals("ENT")){
                        mMap.addMarker(new MarkerOptions().title("Entregado cod-"+parts[5]).position(new LatLng(latDestino,lonDestino)).icon(BitmapDescriptorFactory.fromResource(R.drawable.entregado)));
                    }
                }
            }
        }
    }

}

package com.example.edwin.shopapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.edwin.shopapp.SQLite.SQLiteLocal;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RepartidorMaps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ProgressBar pbRepartidorMaps;
    static final  String TAG="**console**";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    TextView tvrepartidoMaps,tvruta;
    String codigoPedido,cliente;
    Button btnEntregado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repartidor_maps);
        getLocationPermission();
        pbRepartidorMaps = (ProgressBar)findViewById(R.id.pbRepartidorMaps);
        tvrepartidoMaps = (TextView) findViewById(R.id.tvrepartidoMaps);
        tvruta = (TextView)findViewById(R.id.tvruta);
        btnEntregado = (Button)findViewById(R.id.btnEntregado);
        Bundle bundle = getIntent().getExtras();
        codigoPedido = bundle.getString("codigo");
        cliente = bundle.getString("cliente");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        verRuta ruta = new verRuta();
        ruta.execute(codigoPedido);

        btnEntregado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmaEntrega();

            }
        });


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getDeviceLocation();


    }
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(RepartidorMaps.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }
    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(RepartidorMaps.this);
    }
    class verRuta extends AsyncTask<String,String,String> {
        String cadenaViene = "";

        @Override
        protected String doInBackground(String... strings) {
            Connection connect; connect = ConexionSQL.ConnectionHelper();
           String codigopedido = strings[0];
            try {
                Statement st = connect.createStatement();

                String comando="select latitud, longitud, latidudDestino, longituDestino from pedidos where codigo = "+codigopedido+"";
                ResultSet rs = st.executeQuery(comando);
                while (rs.next()) {
                    cadenaViene = rs.getString(1)+"!/"+rs.getString(2)+"!/"+rs.getString(3)+"!/"+rs.getString(4);
                }
                connect.close();

            } catch (Exception e) {
                e.printStackTrace();
                // Toast.makeText(getActivity(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();

            }
            return cadenaViene;

        }

        @Override
        protected void onPostExecute(String datos) {
            if (!datos.equals("null")){
                pbRepartidorMaps.setVisibility(View.INVISIBLE);
                tvrepartidoMaps.setVisibility(View.INVISIBLE);
                tvruta.setVisibility(View.VISIBLE);
                tvruta.setText("Ruta para llegar a: "+cliente);
                String[] parts = datos.split("!/");
                float latitud= Float.parseFloat(parts[0]);
                float longitud= Float.parseFloat(parts[1]);
                float latidudDestino= Float.parseFloat(parts[2]);
                float longituDestino= Float.parseFloat(parts[3]);
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitud,longitud)).title("origen"));
                moveCamera(new LatLng(latitud,longitud),12);

                mMap.addMarker(new MarkerOptions().position(new LatLng(latidudDestino,longituDestino)).title("Destino"));
                moveCamera(new LatLng(latitud,longitud),12);
               getRoute(parts[0]+","+parts[1],parts[2]+","+parts[3]);


            }else {
                Toast.makeText(getApplicationContext(),"Error al obtener los datos",Toast.LENGTH_LONG).show();
            }

        }
    }
    public  void getRoute(String origen, String destino){
        List<LatLng> path = new ArrayList();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBrPt88vvoPDDn_imh-RzCXl5Ha2F2LYig")
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, origen, destino);
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);
        }
    }


    public void confirmaEntrega(){
        try {
            Connection connect; connect = ConexionSQL.ConnectionHelper();
            Statement st = connect.createStatement();
            String updatePedido="UPDATE pedidos SET idEstado = 'ENT' WHERE codigo = "+codigoPedido+";";
                        st.execute(updatePedido);

            connect.close();
            Intent i = new Intent(getApplicationContext(), RepartidorActivity.class);
            startActivity(i);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();

        }
    }
}

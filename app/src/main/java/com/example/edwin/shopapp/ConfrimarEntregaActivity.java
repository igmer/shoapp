package com.example.edwin.shopapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.LocaleList;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.edwin.shopapp.SQLite.SQLiteLocal;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ConfrimarEntregaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Location location;
    LocationManager locationManager;
    private CameraPosition cameraZoom;
    int MY_PERMISSION_LOCATION = 10;
    static final  String TAG="**console**";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    Marker marketShop;
    Button btnConfirmarEntrega;
    private SharedPreferences pref;
    Utilidades u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confrimar_entrega);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocationPermission();
        btnConfirmarEntrega =(Button)findViewById(R.id.btnConfirmarEntrega);
        locationManager = (LocationManager) ConfrimarEntregaActivity.this.getSystemService(Context.LOCATION_SERVICE);
        pref = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("eror", "error");
            return;
        }

        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location == null) {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
        btnConfirmarEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (marketShop!=null){
                        agregarProducto add = new agregarProducto();
                        add.execute(String.valueOf(marketShop.getPosition().latitude),String.valueOf(marketShop.getPosition().longitude));

                    }else{
                        Toast.makeText(getApplicationContext(),"Por favor selecciones una ubucación",Toast.LENGTH_LONG).show();
                    }

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
        });
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setCountry("SV")
                .build();
        autocompleteFragment.setFilter(typeFilter);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                try{
                    mMap.clear();
                    moveCamera(place.getLatLng(),15);
                    Log.i(TAG,place.getLatLng().toString());
                    marketShop= mMap.addMarker(new MarkerOptions().position(place.getLatLng())
                            .title(place.getName().toString()));


                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);

        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.clear();
                marketShop = mMap.addMarker(new MarkerOptions()
                .position(latLng).title("punto de entrega"));


            }
        });





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
                            Toast.makeText(ConfrimarEntregaActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
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

        mapFragment.getMapAsync(ConfrimarEntregaActivity.this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
    public ArrayList<String> getPedidoLocal(){
        ArrayList<String> pedido  = new ArrayList<>();
        try {
            SQLiteLocal helper = new SQLiteLocal(getApplicationContext());
            SQLiteDatabase db = helper.getWritableDatabase();

            String getPedido="SELECT idProducto,precio FROM DetallePedido WHERE estado = 'ADD'";
            db.rawQuery(getPedido,null);
            Cursor rs = db.rawQuery(getPedido, null);
            if (rs.moveToFirst()) {
                do {// el pimer elemento que ira el la lista sera la Url de la imagen
                    pedido.add(rs.getString(0) + "!/" + rs.getString(1));
                } while (rs.moveToNext());
                db.close();
            }

            //  String borrarpedido="UPDATE DetallePedido SET estado = 'CONFIRMADO' WHERE estado = 'ADD'";
            //  db.execSQL(borrarpedido);

        }catch (Exception e){
            e.printStackTrace();
        }
        return pedido;

    }
    class agregarProducto extends AsyncTask<String,String,Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            Connection connect; connect = ConexionSQL.ConnectionHelper();
            String mensaje_error="";
            String latitudTxt = strings[0];
            String longitudTxt = strings[1];
            float latitud = Float.parseFloat(latitudTxt);
            float longitud = Float.parseFloat(longitudTxt);
            String usuario  = pref.getString("usuarioBD","");//obtnemos el usuario
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fecha = dateFormat.format(currentTime);
            Integer ingresado=0;
            try {
                Statement st = connect.createStatement();
                int maxCodido= 0;
                String comando="SELECT max(codigo) FROM Pedidos";
                ResultSet rs = st.executeQuery(comando);
                while (rs.next()) {
                    maxCodido = rs.getInt(1);
                }
                int maxCodigoFinal = maxCodido +1;
                //String lotitud = String.valueOf(marketShop.getPosition().latitude);
               String pedidoInsert="INSERT INTO pedidos(idEstado,fechaIngreso,idCliente,latitud," +
                       "longitud,latidudDestino,longituDestino)" +
                       "values('NVO','"+fecha+"','"+usuario+"','13.700059'," +
                        "'-89.200195',"+latitudTxt+","+longitudTxt+")";
              // Log.i("**",pedidoInsert);
                st.execute(pedidoInsert);

                for (int i = 0; i <getPedidoLocal().size() ; i++) {
                    ArrayList<String> lista = getPedidoLocal();
                    String fila =lista.get(i);
                    String[] part = fila.split("!/");
                    int idProducto = Integer.parseInt(part[0]);
                    Double precio = Double.parseDouble(part[1]);
                    String pedidoDetalle="INSERT INTO DetallePedido" +
                            "(idPedido, idProducto, precio)" +
                            "VALUES("+maxCodigoFinal+", "+idProducto+", "+precio+");";
                    st.execute(pedidoDetalle);

                }

                connect.close();
                ingresado = 1;
            } catch (Exception e) {
                e.printStackTrace();
                // Toast.makeText(getActivity(),"Error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                ingresado=0;
            }
            return ingresado;

        }

        @Override
        protected void onPostExecute(Integer respuesta) {
            //super.onPostExecute(integer);
            if (respuesta==1){
                SQLiteLocal helper = new SQLiteLocal(getApplicationContext());
                SQLiteDatabase db = helper.getWritableDatabase();
                String borrarpedido="UPDATE DetallePedido SET estado = 'CONFIRMADO' WHERE estado = 'ADD'";
                db.execSQL(borrarpedido);
                Toast.makeText(getApplicationContext(),"Su compra de ha reaizado con exito",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),MenusTabs.class);
                startActivity(i);
                finish();

            }else{
                Toast.makeText(getApplicationContext(),"Hubo un error al agregar el producto",Toast.LENGTH_LONG).show();
            }
        }
    }



}

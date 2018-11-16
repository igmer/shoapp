package com.example.edwin.shopapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class Principal extends AppCompatActivity {

    Button btnIngresar, btnSalir;
    //EditText edtEmail, edtClave;
   static TextInputEditText edtEmail, edtClave;
    CardView cv_loggin;
    CardView cv_registro;
    static Switch swRemember;
    ProgressBar pbLogin;
    static SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        edtEmail =(TextInputEditText) findViewById(R.id.titEmail);
        edtClave=(TextInputEditText) findViewById(R.id.titPass);
        CardView card_view = (CardView) findViewById(R.id.cardView);
        CardView cv_registrar = (CardView) findViewById(R.id.cv_registro);
        pbLogin = (ProgressBar)findViewById(R.id.pbLogin);
        swRemember = (Switch)findViewById(R.id.swRemember);
        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);


        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtEmail.getText().toString().trim().length()==0){
                    edtEmail.setError("Dato Requerido para Ingresar");
                }else if(edtClave.getText().toString().trim().length()==0){
                    edtClave.setError("Dato Requerido para Ingresar");
                }else{
                    checkinServer check = new checkinServer();
                    check.execute("");
                    /*if(validaIngresousuario(edtEmail.getText().toString().trim(),edtClave.getText().toString().trim())>=1){
                        saveOnPreferences(edtEmail.getText().toString().trim(),edtClave.getText().toString().trim());
                        Intent i = new Intent(getApplicationContext(),MenusTabs.class);
                        startActivity(i);
                        finish();
                        */


                }


            }
        });

        cv_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewUser = new Intent(getApplication(), RegistroUsuarios.class);
                startActivity(NewUser);
            }
        });



    }
    public class checkinServer extends AsyncTask<String,String,Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            String elUsuario= edtEmail.getText().toString().trim();
            String laClave =  edtClave.getText().toString().trim();
            Connection connect; connect = ConexionSQL.ConnectionHelper();
            String mensaje_error="";
            Integer existe=0;
            try {
                Statement st = connect.createStatement();
                String comando="SELECT usuario, password FROM usuarios where usuario='" +  elUsuario + "'";
                comando+=" and password='" + laClave+"'";
                ResultSet rs = st.executeQuery(comando);
                while (rs.next()) {
                    existe++;
                }
                connect.close();
            } catch (Exception e) {
                Log.i("Error**",e.getMessage());
                existe=0;
               // return false;
            }
            //return existe;
            //   }

            return existe;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pbLogin.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Integer existe) {
            //super.onPostExecute(existe);
            if (existe>0){
                saveOnPreferences(edtEmail.getText().toString().trim(),edtClave.getText().toString().trim());
                Intent i = new Intent(getApplicationContext(),MenusTabs.class);
                startActivity(i);
                finish();
            }else{
                edtClave.setError("Credenciales no validas");
                edtEmail.setError("Credenciales no validas");
                pbLogin.setVisibility(View.INVISIBLE);
            }
        }


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }






    /** metodo para guardar en Shared preferences las credenciales de usuario**/
    private static void saveOnPreferences(String user, String pass) {
        if (swRemember.isChecked()) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user", user);
            editor.putString("pass", pass);
            editor.putString("usuarioBD", user);
            editor.apply();
        }else{
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("usuarioBD", user); // este lo usaremos para el usuario que se enviare ellos pedidos
            editor.apply();
        }
    }

}

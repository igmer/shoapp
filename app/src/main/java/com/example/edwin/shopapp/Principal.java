package com.example.edwin.shopapp;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.widget.EditText;

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
    TextInputEditText edtEmail, edtClave;
    CardView cv_loggin;
    CardView cv_registro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        edtEmail =(TextInputEditText) findViewById(R.id.titEmail);
        edtClave=(TextInputEditText) findViewById(R.id.titPass);
        CardView card_view = (CardView) findViewById(R.id.cardView);
        CardView cv_registrar = (CardView) findViewById(R.id.cv_registro);


        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtEmail.getText().toString().trim().length()==0){
                    edtEmail.setError("Dato Requerido para Ingresar");
                }else if(edtClave.getText().toString().trim().length()==0){
                    edtClave.setError("Dato Requerido para Ingresar");
                }else{
                    validaIngresousuario(edtEmail.getText().toString().trim(),edtClave.getText().toString().trim());
                  ///  checkCredential(edtEmail.getText().toString().trim(),edtClave.getText().toString().trim());
                   // Intent TabsMenus = new Intent(getApplication(), MenusTabs.class);
                   //rty startActivity(TabsMenus);
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




    public Integer validaIngresousuario( String elUsuario,  String laClave){
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
            mensaje_error=e.getMessage().toString();
            existe=0;
        }

        return existe;
    }

}

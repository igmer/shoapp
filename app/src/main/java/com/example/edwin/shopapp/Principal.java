package com.example.edwin.shopapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    String rol = "";
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        mAuth = FirebaseAuth.getInstance();

       // mDatabase = FirebaseDatabase.getInstance().getReference();
       // mDatabase.child("user").child("1").setValue(user);

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
                    try {
                        String email = edtEmail.getText().toString().trim();
                        String password = edtClave.getText().toString().trim();
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(Principal.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d("****", "signInWithEmail:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            Intent i = new Intent(getApplicationContext(),MenusTabs.class);
                                            startActivity(i);
                                           // updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w("****", "signInWithEmail:failure", task.getException());
                                            Toast.makeText(Principal.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                            //updateUI(null);
                                        }

                                        // ...
                                    }
                                });

                    }catch (Exception e){
                        e.printStackTrace();
                    }



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
                String comando="SELECT usuario, password,idRol FROM usuarios where usuario='" +  elUsuario + "'";
                comando+=" and password='" + laClave+"'";
                ResultSet rs = st.executeQuery(comando);
                while (rs.next()) {
                   rol= rs.getString(3);
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
                if (rol.equals("REP")){
                    Intent i = new Intent(getApplicationContext(),RepartidorActivity.class);
                    startActivity(i);
                    finish();

                }else{
                    Intent i = new Intent(getApplicationContext(),MenusTabs.class);
                    startActivity(i);
                    finish();

                }


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

    public class User {

        public String username;
        public String email;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String username, String email) {
            this.username = username;
            this.email = email;
        }

    }

}

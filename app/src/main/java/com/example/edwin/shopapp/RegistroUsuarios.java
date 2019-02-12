package com.example.edwin.shopapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.sql.Connection;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class RegistroUsuarios extends AppCompatActivity {
    EditText txtNombre,txtApellido,txtTel,txtDireccion,txtCorreo,txtPassword1,txtPassword2;
    Button  btnAceptar_nuevo_usuario;
    private FirebaseAuth mAuth;
    public String TAG ="**firebase***";
   // private Connection connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuarios);
        mAuth = FirebaseAuth.getInstance();
        txtNombre = (EditText)findViewById(R.id.txtNombre);
        txtApellido = (EditText)findViewById(R.id.txtApellido);
        txtTel = (EditText)findViewById(R.id.txtTel);
        txtDireccion = (EditText)findViewById(R.id.txtDireccion);
        txtCorreo = (EditText)findViewById(R.id.txtCorreo);
        txtPassword1 = (EditText)findViewById(R.id.txtPassword1);
       // connect = ConexionSQL.ConnectionHelper();
        txtPassword2 = (EditText)findViewById(R.id.txtPassword2);
        btnAceptar_nuevo_usuario = (Button)findViewById(R.id.btnAceptar_nuevo_usuario);
        btnAceptar_nuevo_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDataValid()){
                   int exito= registrarUsuario();

                }

            }
        });
    }



    public boolean isDataValid(){

        if(TextUtils.isEmpty(txtCorreo.getText())){
            txtCorreo.setError("Correo Electrónico, requerido");
            txtCorreo.requestFocus();
            return false;
        }
        else
        {
            Pattern pattern =  Patterns.EMAIL_ADDRESS;

            if(!pattern.matcher(txtCorreo.getText().toString()).matches()){
                txtCorreo.setError("Correo Electrónico inválido, favor verifique");
                txtCorreo.requestFocus();
                return false;
            }
        }

        if(TextUtils.isEmpty(txtPassword2.getText())){
            txtPassword2.setError("Confirmación de Contraseña requerida");
            txtPassword2.requestFocus();
            return false;
        }
        if(!txtPassword1.getText().toString().equals(txtPassword2.getText().toString())){
            txtPassword2.setError("Las contraseñas no coinciden, favor verique");
            txtPassword2.requestFocus();
            return false;
        }

        return true;

    }
    private int registrarUsuario() {
        String email = txtCorreo.getText().toString().trim();
        String password = txtPassword1.getText().toString().trim();
        try {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                               // updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                               // updateUI(null);
                            }

                            // ...
                        }
                    });
            return  1;
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            return 0;
        }

    }


}

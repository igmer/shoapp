package com.example.edwin.shopapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
   // private Connection connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuarios);
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
                   if (exito==1){
                       Toast.makeText(getApplicationContext(),"Usuario Registrado con exito",Toast.LENGTH_LONG).show();
                       Intent i = new Intent(getApplicationContext(),Principal.class);
                       startActivity(i);
                   }
                }

            }
        });
    }



    public boolean isDataValid(){

        if(TextUtils.isEmpty(txtNombre.getText())){
            txtNombre.setError("Nombre requerido");
            txtNombre.requestFocus();
            return false;
        }
        if(TextUtils.isEmpty(txtApellido.getText())){
            txtApellido.setError("Apellidofgy requerido");
            txtApellido.requestFocus();
            return false;
        }
        

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

      /*

        Usuarios objUsuValCorreo= new Usuarios();
        objUsuValCorreo.setCorreoElectronico(txtCorreo.getText().toString());

        UsuariosBL objUsuVal = new UsuariosBL(getApplicationContext(), objUsuValCorreo );

        LinkedList listDatosUsuario;

        listDatosUsuario =objUsuVal.getByID();
        if(listDatosUsuario.size() > 0){
            txtCorreo.setError("El Correo Electrónico "+ txtCorreo.getText().toString() +" ya ha sido registrado, favor verifique");
            txtCorreo.requestFocus();
            return false;
        }

        objUsuValCorreo= new Usuarios();
        objUsuValCorreo.setUsuario(edtUsuario.getText().toString());

        objUsuVal = new UsuariosBL(getApplicationContext(), objUsuValCorreo );
        listDatosUsuario =objUsuVal.getByUSUARIO();
        if(listDatosUsuario.size() > 0){
            edtUsuario.setError("El Usuario "+ edtUsuario.getText().toString() +" ya ha sido registrado, favor verifique");
            edtUsuario.requestFocus();
            return false;
        }
*/
        if(TextUtils.isEmpty(txtDireccion.getText())){
            txtDireccion.setError("Dirección de residencia requerida");
            txtDireccion.requestFocus();
            return false;
        }

        if(TextUtils.isEmpty(txtTel.getText())){
            txtTel.setError("Teléfono requerido");
            txtTel.requestFocus();
            return false;

        }



        if(TextUtils.isEmpty(txtPassword1.getText())){
            txtPassword1.setError("Contraseña requerida");
            txtPassword1.requestFocus();
            return false;
        }
        else{
            if(txtPassword1.getText().toString().length() < 5){
                txtPassword1.setError("Contraseña debe poseer al menos 5 caracteres");
                txtPassword1.requestFocus();
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
        try {
            Connection  connect = ConexionSQL.ConnectionHelper();
            Statement st = connect.createStatement();
            String NombreTxt = txtNombre.getText().toString().trim();
            String ApellidoTxt = txtApellido.getText().toString().trim();
            String TelTxt = txtTel.getText().toString().trim();
            String DireccionTxt = txtDireccion.getText().toString().trim();
            String CorreoTxt = txtCorreo.getText().toString().trim();
            String Password1Txt = txtPassword1.getText().toString().trim();
            String sql = "INSERT INTO usuarios VALUES('" + CorreoTxt + "','" + Password1Txt + "','" + NombreTxt + "'," +
                    "'" + ApellidoTxt + "','" + DireccionTxt + "','" + TelTxt + "','" + CorreoTxt + "','CLI')";
            st.execute(sql);
            connect.close();
            return  1;
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            return 0;
        }

    }


}

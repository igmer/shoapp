package com.example.edwin.shopapp;

import android.content.SharedPreferences;
import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

public class Utilidades {


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
            Log.i("Error**",e.getMessage());
            existe=0;
        }
        return existe;
    }
    public ArrayList<String> getListaProducto( ){
        Connection connect; connect = ConexionSQL.ConnectionHelper();
        ArrayList<String> productos = new ArrayList<>();

        try {
            Statement st = connect.createStatement();
            String comando="SELECT nombre,precio,descripcion,urlImage,codigo FROM Productos";
            ResultSet rs = st.executeQuery(comando);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) { // el pimer elemento que ira el la lista sera la Url de la imagen
                    productos.add(rs.getString(4)+"-"+rs.getString(1)+"-"+rs.getString(2)
                                    +"-"+rs.getString(3)+"!!"+rs.getString(5));


            }
            connect.close();
        } catch (Exception e) {
            Log.i("Error**",e.getMessage());

        }
        return productos;
    }
    //este metodo nos retornara de las shared preferences el usaurio y la contraseña
    public  static String getUserPrefs(SharedPreferences prefs){
        return prefs.getString("user","");
    }
    public  static String getPassPrefs(SharedPreferences prefs){
        return prefs.getString("pass","");
    }
}

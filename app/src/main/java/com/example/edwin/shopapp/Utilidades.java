package com.example.edwin.shopapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.edwin.shopapp.SQLite.SQLiteLocal;

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
    public ArrayList<String> getListaProducto( String cat){
        Connection connect; connect = ConexionSQL.ConnectionHelper();
        ArrayList<String> productos = new ArrayList<>();
        String comando;

        try {
            Statement st = connect.createStatement();
            if (cat.equals("todas")){
                 comando="SELECT nombre,precio,descripcion,urlImage,codigo FROM Productos";
            }else{
                 comando="SELECT nombre,precio,descripcion,urlImage,codigo FROM Productos WHERE idCategoria = '"+cat+"' ";
            }

            ResultSet rs = st.executeQuery(comando);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) { // el pimer elemento que ira el la lista sera la Url de la imagen
                    productos.add(rs.getString(4)+"!-"+rs.getString(1)+"!-"+rs.getString(2)
                                    +"!-"+rs.getString(3)+"!!"+rs.getString(5));


            }
            connect.close();
        } catch (Exception e) {
            Log.i("Error**",e.getMessage());

        }
        return productos;
    }
    public ArrayList<String> getListaRepartidor( String idRepartidor){
        Connection connect; connect = ConexionSQL.ConnectionHelper();
        ArrayList<String> productos = new ArrayList<>();

        try {
            Statement st = connect.createStatement();
            String comando="SELECT p.codigo, p.fechaIngreso,u.nombre,u.apellido " +
                    "FROM pedidos p "+
                    "INNER JOIN usuarios u on p.idCliente= u.usuario"+
                    " WHERE p.idRepartidor = '"+idRepartidor+"' AND p.idestado = 'NVO' ";
            ResultSet rs = st.executeQuery(comando);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) { // el pimer elemento que ira el la lista sera la Url de la imagen
                productos.add(rs.getString(1)+"!/"+rs.getString(2)+"!/"+rs.getString(3)+"!/"+rs.getString(4));


            }
            connect.close();
        } catch (Exception e) {
            Log.i("Error**",e.getMessage());

        }
        return productos;
    }
    public ArrayList<String> getListaPedido(Context context){
        Connection connect; connect = ConexionSQL.ConnectionHelper();
        ArrayList<String> productos = new ArrayList<>();
        try {
            SQLiteLocal helper = new SQLiteLocal(context);
            SQLiteDatabase db = helper.getReadableDatabase();
            String comando="SELECT codigo,url,nombre,precio,descripcion FROM DetallePedido WHERE estado = 'ADD'";
            Cursor rs = db.rawQuery(comando, null);
            if (rs.moveToFirst()) {
                do {// el pimer elemento que ira el la lista sera la Url de la imagen
                    productos.add(rs.getString(0) + "!/" + rs.getString(1)+"!/"+rs.getString(2)+"!/"+rs.getString(3)+"!/"+rs.getString(4));
                } while (rs.moveToNext());
                db.close();
            }
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
}

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
                    productos.add(rs.getString(4)+"!-"+rs.getString(1)+"!-"+rs.getString(2)
                                    +"!-"+rs.getString(3)+"!!"+rs.getString(5));


            }
            connect.close();
        } catch (Exception e) {
            Log.i("Error**",e.getMessage());

        }
        return productos;
    }
    public ArrayList<String> getListaRepartidor( ){
        Connection connect; connect = ConexionSQL.ConnectionHelper();
        ArrayList<String> productos = new ArrayList<>();

        try {
            Statement st = connect.createStatement();
            String comando="SELECT u.nombre, pe.idrepartidor,pe.destino,dp.idProducto " +
                    "FROM pedidos pe " +
                    "INNER JOIN DetallePedido dp on pe.codigo = dp.idPedido " +
                    "INNER JOIN usuarios u on u.usuario = pe.idCliente " +
                    "WHERE pe.idRepartidor = 'repartidor1' AND pe.idestado = 'NVO' ";
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
}

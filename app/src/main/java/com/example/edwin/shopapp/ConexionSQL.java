package com.example.edwin.shopapp;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQL {
    public static Connection ConnectionHelper() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            //ConnectionURL = "jdbc:jtds:sqlserver://clinicacetu.database.windows.net;port=1433;databaseName=ClinicaCETU;user=elemus@clinicacetu;password=Clinicadb$";
            ConnectionURL = "jdbc:jtds:sqlserver://dbGeoShop.mssql.somee.com;databaseName=dbGeoShop;user=acid0ikario_SQLLogin_1;password=hkfw6opi81";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("ERROR1", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("ERROR2", e.getMessage());
        } catch (Exception e) {
            Log.e("ERROR3", e.getMessage());
        }
        return connection;
    }
}

package com.example.edwin.shopapp.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class SQLiteLocal  extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "shopapp";
    public static final int DATABASE_VERSION = 1;
    public SQLiteLocal( Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    String sqlPedidos = "CREATE TABLE DetallePedido (" +
            "codigo INTEGER PRIMARY KEY AUTOINCREMENT," +
            "idPedido INTEGER," +
            "idProducto INTEGER NOT NULL," +
            "precio decimal ," +
            "url text ," +
            "nombre text ," +
            "descripcion text ," +
            "estado TEXT not null)";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlPedidos);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

package com.example.notas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import androidx.annotation.Nullable;

public class BBDD extends SQLiteOpenHelper {

    String almacennotas = "CREATE TABLE Notas(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "fecha TEXT DEFAULT (strftime('%d %b','now')), " + "titulo TEXT, " + "nota TEXT)";
    String almacendiario = "CREATE TABLE Diario(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "fecha TEXT DEFAULT (strftime('%d %b','now')), " + "titulo TEXT, " + "nota TEXT)";
    public BBDD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(almacennotas);
        db.execSQL(almacendiario);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS Notas");
        db.execSQL("DROP TABLE IF EXISTS Diario");
        db.execSQL(almacennotas);
        db.execSQL(almacendiario);

    }
}
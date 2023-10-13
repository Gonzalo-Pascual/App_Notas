package com.example.notas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class EditarEliminarNota extends AppCompatActivity {
    SQLiteDatabase db;
    private BBDD almacen;
    private EditText titulo, texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.colordelmenu);
        setContentView(R.layout.activity_editar_eliminar_nota);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Editar Nota</font>"));
        }

        titulo = findViewById(R.id.titulo);
        texto = findViewById(R.id.texto);

    }

    protected void onResume(){
        super.onResume();
        almacen = new BBDD(this, "Datos", null, 1);
        db = almacen.getWritableDatabase();

        Intent intent = getIntent();
        if (intent != null) {
            String notaId = intent.getStringExtra("nota_id");
            if (notaId != null) {
                obtenerDatosDeNota(notaId);
            }
        }
    }

    protected void onPause(){
        super.onPause();
        db.close();
        almacen.close();
    }

    private void obtenerDatosDeNota(String notaId){
        Cursor cursor = db.rawQuery("SELECT titulo, nota FROM Notas WHERE _id = ?", new String[]{notaId});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String tituloNota = cursor.getString(cursor.getColumnIndex("titulo"));
            @SuppressLint("Range") String textoNota = cursor.getString(cursor.getColumnIndex("nota"));
            titulo.setText(tituloNota);
            texto.setText(textoNota);
        }
        cursor.close();
    }

    public void volver(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas guardar antes de volver?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                guardar();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(EditarEliminarNota.this, MainActivity.class);
                startActivity(i);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas guardar antes de volver?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                guardar();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(EditarEliminarNota.this, MainActivity.class);
                startActivity(i);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menueditar, menu);
        return true;
    }

    public void guardarNota(MenuItem item) {
        guardar();
    }

    public void guardar(){
        Intent intent = getIntent();
        if (intent != null) {
            String notaId = intent.getStringExtra("nota_id");
            if (notaId != null) {
                String nuevoTitulo = titulo.getText().toString();
                String nuevoTexto = texto.getText().toString();

                SQLiteDatabase db = almacen.getWritableDatabase();
                ContentValues valores = new ContentValues();
                valores.put("titulo", nuevoTitulo);
                valores.put("nota", nuevoTexto);

                int filasActualizadas = db.update("Notas", valores, "_id = ?", new String[]{notaId});
                db.close();

                if (filasActualizadas > 0) {
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Error al actualizar la nota", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void eliminarNota(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas eliminar la nota?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                eliminarNotaConfirmada();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void eliminarNotaConfirmada() {
        Intent intent = getIntent();
        if (intent != null) {
            String notaId = intent.getStringExtra("nota_id");
            if (notaId != null) {
                SQLiteDatabase db = almacen.getWritableDatabase();
                int filasEliminadas = db.delete("Notas", "_id = ?", new String[]{notaId});
                db.close();

                if (filasEliminadas > 0) {
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(this, "Error al eliminar la nota", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
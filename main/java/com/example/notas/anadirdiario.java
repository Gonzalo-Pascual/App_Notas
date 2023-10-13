package com.example.notas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class anadirdiario extends AppCompatActivity {
    SQLiteDatabase db;
    private BBDD almacen;
    private EditText titulo,texto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadirnota);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Añadir entrada</font>"));
        }
        titulo = findViewById(R.id.titulo);
        texto = findViewById(R.id.texto);
    }

    protected void onResume(){
        super.onResume();
        almacen = new BBDD(this, "Datos", null, 1);
        db = almacen.getWritableDatabase();
    }

    protected void onPause(){
        super.onPause();

        db.close();
        almacen.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.colormenu, menu);
        return true;
    }

    public void volver(MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas guardar antes de volver?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                añadirmetodo();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(anadirdiario.this, DiarioMain.class);
                startActivity(i);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void añadir(MenuItem item){
        añadirmetodo();
    }

    public void añadirmetodo(){
        if(db != null)
        {
            if(comprobarDatos())
            {
                try
                {
                    db.execSQL("insert into Diario( titulo, nota) values ( '" + titulo.getText().toString() + "', '" + texto.getText().toString() + "');");
                    titulo.setText("");
                    texto.setText("");
                    Intent i = new Intent(this, DiarioMain.class);
                    startActivity(i);

                }
                catch(SQLiteConstraintException e) {
                    Toast.makeText(this, "No se ha podido añadir", Toast.LENGTH_LONG).show();
                }
            }
        }
        else
        {
            Toast.makeText(this, "No se pudo acceder a la base de datos", Toast.LENGTH_LONG).show();
        }
    }

    public boolean comprobarDatos()
    {
        if(titulo.getText().toString().isEmpty())
        {
            Toast.makeText(this, "Introduce el Titulo", Toast.LENGTH_LONG).show();
            return false;
        }

        if(texto.getText().toString().isEmpty()) {
            Toast.makeText(this, "Introduce la nota", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas guardar antes de volver?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                añadirmetodo();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(anadirdiario.this, DiarioMain.class);
                startActivity(i);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
package com.example.notas;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DiarioMain extends AppCompatActivity {

    SQLiteDatabase db;
    private BBDD almacen;
    private Button botonAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        botonAgregar = findViewById(R.id.botonAgregar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Diario</font>"));
        }
        almacen = new BBDD(this, "Datos", null, 1);
        db = almacen.getWritableDatabase();

        LinearLayout container = findViewById(R.id.container);

        SQLiteDatabase db = almacen.getReadableDatabase();

        String selectQuery = "SELECT _id, fecha, titulo, nota FROM Diario  ORDER BY _id DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        LinearLayout cardLayout = null;
        if (cursor.moveToFirst()){
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
                @SuppressLint("Range") String fecha = cursor.getString(cursor.getColumnIndex("fecha"));
                @SuppressLint("Range") String titulo = cursor.getString(cursor.getColumnIndex("titulo"));
                @SuppressLint("Range") String texto = cursor.getString(cursor.getColumnIndex("nota"));

                SimpleDateFormat originalFormat = new SimpleDateFormat("dd.MM", Locale.getDefault());
                SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());

                try {
                    Date date = originalFormat.parse(fecha);
                    fecha = targetFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                LayoutInflater inflater = LayoutInflater.from(this);
                cardLayout = (LinearLayout) inflater.inflate(R.layout.cartasdiario, null);
                TextView fechaTextView = cardLayout.findViewById(R.id.fecha);
                fechaTextView.setText(fecha);

                TextView tituloTextView = cardLayout.findViewById(R.id.titulo);
                tituloTextView.setText(titulo);

                TextView textoTextView = cardLayout.findViewById(R.id.nota);
                textoTextView.setText(texto);

                container.addView(cardLayout);

                cardLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(DiarioMain.this, EditarEliminarDiario.class);
                        intent.putExtra("nota_id", String.valueOf(id));
                        startActivity(intent);
                    }
                });
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
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
        getMenuInflater().inflate(R.menu.menunotas, menu);
        return true;
    }

    public void añadir(View view){
        Intent i = new Intent(this, anadirdiario.class);
        startActivity(i);
    }

    public void notas(MenuItem item){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}
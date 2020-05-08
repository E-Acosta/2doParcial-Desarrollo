package com.everacosta.segundoParcial;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import android.util.Log;

import com.everacosta.segundoParcial.Modelos.persona;
import com.everacosta.segundoParcial.Modelos.personaDBHelper;
import com.everacosta.segundoParcial.Modelos.personaContract;

import java.util.ArrayList;

public class listarActivity extends AppCompatActivity {
    personaDBHelper con;
    SQLiteDatabase db;
    RecyclerView recyclerView;
    recyclerViewAdapter adapter;
    private static final String TAG = "listarActivity";
    private ArrayList<persona> personas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);
        Log.d(TAG, "Created");
        consultarSql();
        initRecyclerView();
        registerForContextMenu(recyclerView);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 122:
                String id = String.valueOf(personas.get(adapter.getPosition()).getId());
                Intent intent = new Intent(listarActivity.this, editActivity.class);
                intent.putExtra("ID", id);
                this.finish();
                startActivity(intent);

                break;
            case 123:
                int idEliminar = personas.get(adapter.getPosition()).getId();
                Log.e("ELIMINAREL:",personas.get(adapter.getPosition()).getNombre()+"CON ID"+personas.get(adapter.getPosition()).getId());
                deleteSql(idEliminar);
                //Toast.makeText(this, "Eliminar", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteSql(int ID) {
        con = new personaDBHelper(this, "bd_personas", null, 1);
        SQLiteDatabase db = con.getWritableDatabase();
        long idResultado = db.delete(personaContract.TABLE_NAME, "id = ?", new String[]{String.valueOf(ID)});
        Toast.makeText(this, "Persona Eliminada:" + idResultado, Toast.LENGTH_SHORT).show();
        db.close();
        this.finish();
        startActivity(new Intent(this, opcionesActivity.class));
    }

    private void consultarSql() {
        con = new personaDBHelper(this, "bd_personas", null, 1);
        Log.d("INFOOOOOOOOOOOOOO", "consultarSql: iniciado");
        try {
            SQLiteDatabase db = con.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + personaContract.TABLE_NAME, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
//                    int id = cursor.getInt(cursor.getColumnIndex(personaContract.ID));
                    int id = cursor.getInt(0);
//                    String nombre = cursor.getString(cursor.getColumnIndex(personaContract.NOMBRE));
                    String nombre = cursor.getString(1);
                    String cedula = cursor.getString(cursor.getColumnIndex(personaContract.CEDULA));
                    String Estrato = cursor.getString(cursor.getColumnIndex(personaContract.ESTRATO));
                    String Salario = cursor.getString(cursor.getColumnIndex(personaContract.SALARIO));
                    String NivelE = cursor.getString(cursor.getColumnIndex(personaContract.NIVELE));
                    cursor.moveToNext();
                    //String nombre, String cedula, String salario, String estrato, String nivelEdu
                    personas.add(new persona(id, nombre,cedula,Salario,Estrato,NivelE));
                    Log.e("PERSONAAAAAA", new persona(id, nombre,cedula,Salario,Estrato,NivelE).toString());
                }
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.d("INFOOOOOOOOOOOOOO", "ERROR SQL");
            Log.d("INFOOOOOOOOOOOOOO", e.toString());
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView");
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new recyclerViewAdapter(personas, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //Toast.makeText(listarActivity.this, "Text Changed", Toast.LENGTH_SHORT).show();
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

}

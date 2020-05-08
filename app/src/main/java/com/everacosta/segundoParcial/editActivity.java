package com.everacosta.segundoParcial;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.everacosta.segundoParcial.Modelos.persona;
import com.everacosta.segundoParcial.Modelos.personaContract;
import com.everacosta.segundoParcial.Modelos.personaDBHelper;

import java.util.ArrayList;

public class editActivity extends AppCompatActivity {

    personaDBHelper con;
    private ArrayList<persona> personas = new ArrayList<>();
    EditText etNombre, etCedula, etSalrio;
    Button updateButton;
    Spinner estratoSpinner,nivelESpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        final int ID = Integer.valueOf(getIntent().getStringExtra("ID"));
        setContentView(R.layout.activity_add_contact);
        estratoSpinner = findViewById(R.id.gruopEstrato);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.estrato, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        estratoSpinner.setAdapter(adapter);
        nivelESpinner = findViewById(R.id.groupNivelE);
        adapter = ArrayAdapter.createFromResource(this, R.array.nivel_educate, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nivelESpinner.setAdapter(adapter);
        etNombre = findViewById(R.id.etNombre);
        etCedula = findViewById(R.id.etCedula);
        etSalrio = findViewById(R.id.etSalario);
        updateButton = findViewById(R.id.ingresarButton);
        consultarSql(ID);
        writeInformation();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSql(ID);
            }
        });
    }

    private void consultarSql(int ID) {
        con = new personaDBHelper(this, "bd_personas", null, 1);
        Log.d("INFOOOOOOOOOOOOOOaaa", "consultarSql: iniciado");
        try {
            SQLiteDatabase db = con.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + personaContract.TABLE_NAME+" WHERE id =" + ID, null);
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    String id = cursor.getString(0);
//                    String nombre = cursor.getString(cursor.getColumnIndex(personaContract.NOMBRE));
                    String nombre = cursor.getString(1);
                    String cedula = cursor.getString(cursor.getColumnIndex(personaContract.CEDULA));
                    String Estrato = cursor.getString(cursor.getColumnIndex(personaContract.ESTRATO));
                    String Salario = cursor.getString(cursor.getColumnIndex(personaContract.SALARIO));
                    String NivelE = cursor.getString(cursor.getColumnIndex(personaContract.NIVELE));
                    cursor.moveToNext();
                    personas.add(new persona(Integer.parseInt(id), nombre,cedula,Salario,Estrato,NivelE));
                    //String nombre, String cedula, String salario, String estrato, String nivelEdu
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

    private void updateSql(int ID) {
        if (TextUtils.isEmpty(etNombre.getText().toString())) {
            etNombre.setError("No hay nombre");
            return;
        }
        if (TextUtils.isEmpty(etCedula.getText().toString())) {
            etCedula.setError("No hay Sistolico");
            return;
        }
        if (TextUtils.isEmpty(etSalrio.getText().toString())) {
            etSalrio.setError("No hay Distolico");
            return;
        }
        con = new personaDBHelper(this, "bd_personas", null, 1);
        SQLiteDatabase db = con.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(personaContract.NOMBRE, etNombre.getText().toString());
        values.put(personaContract.CEDULA, etCedula.getText().toString());
        values.put(personaContract.SALARIO, etSalrio.getText().toString());
        values.put(personaContract.NIVELE, nivelESpinner.getSelectedItem().toString());
        long idResultado = db.update(personaContract.TABLE_NAME, values, "id = ?", new String[]{String.valueOf(ID)});
        Toast.makeText(this, "Registro Actualizado:"+idResultado , Toast.LENGTH_SHORT).show();
        db.close();
        this.finish();
        startActivity(new Intent(editActivity.this,opcionesActivity.class));
    }

    public void writeInformation() {
        try {
            etNombre.setText(personas.get(0).getNombre());
            etCedula.setText(personas.get(0).getCedula());
            etSalrio.setText(personas.get(0).getSalario());
            etCedula.setFocusable(false);
            etCedula.setEnabled(false);
            etCedula.setCursorVisible(false);
            etCedula.setKeyListener(null);
            etCedula.setBackgroundColor(Color.TRANSPARENT);
        }catch (Exception e){
            Log.e("PUTAMADRE","ERROR EN ESTA MONDA");
        }
    }
}

package com.everacosta.segundoParcial;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
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

import com.everacosta.segundoParcial.Modelos.personaContract;
import com.everacosta.segundoParcial.Modelos.personaDBHelper;

public class addPersonaActivity extends AppCompatActivity {
    EditText etNombre, etCedula, etSalrio;
    Button ingresarButton, birthdayButton;
    Spinner estratoSpinner,nivelESpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        ingresarButton = findViewById(R.id.ingresarButton);
        ingresarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noEmpty();

            }
        });
    }

    private void registrarUsuario() {
        personaDBHelper conexion = new personaDBHelper(this, "bd_personas", null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(personaContract.NOMBRE, etNombre.getText().toString());
        values.put(personaContract.CEDULA, etCedula.getText().toString());
        values.put(personaContract.SALARIO, etSalrio.getText().toString());
        values.put(personaContract.NIVELE, nivelESpinner.getSelectedItem().toString());
        Log.i("INFOOOOO:","EL NIVEL DEL EDUCACION: " + nivelESpinner.getSelectedItem().toString());
        values.put(personaContract.ESTRATO, estratoSpinner.getSelectedItem().toString());
        long idResultado = db.insert(personaContract.TABLE_NAME, null, values);
        Toast.makeText(this, "Id registro: " + idResultado, Toast.LENGTH_SHORT).show();
        db.close();
        this.finish();
    }

    public void noEmpty() {
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
        registrarUsuario();
    }
}

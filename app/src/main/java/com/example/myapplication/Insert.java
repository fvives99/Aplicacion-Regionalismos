package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Insert extends AppCompatActivity {


    FirebaseAuth mAuth;

    EditText escribirpalabra;
    EditText escribirSignificado;
    Spinner spinnerpaises;
    Button ingresarpalabra;
    Button listarpalabras;
    Button btnLogout2;
    Button perfil;

    DatabaseReference regionalismosDbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        escribirpalabra = findViewById(R.id.escribirpalabra);
        escribirSignificado =findViewById(R.id.escribirsignificado);
        spinnerpaises = findViewById(R.id.spinnerpaises);
        ingresarpalabra = findViewById(R.id.ingresarpalabra);
        listarpalabras= findViewById(R.id.listarpalabras);
        btnLogout2 = findViewById(R.id.btnLogout2);
        regionalismosDbRef = FirebaseDatabase.getInstance().getReference().child("Paises");
        mAuth = FirebaseAuth.getInstance();

        ingresarpalabra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertPais();
            }
        });

        listarpalabras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Insert.this, RetrieveDataActivity.class));
            }
        });
        btnLogout2.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(Insert.this, Activity_login.class));
        });

    }



    private void insertPais(){

        String palabra = escribirpalabra.getText().toString();
        String significado = escribirSignificado.getText().toString();
        String pais = spinnerpaises.getSelectedItem().toString();

        Palabras palabras = new Palabras(palabra,significado,pais);

        regionalismosDbRef.push().setValue(palabras);
        Toast.makeText(Insert.this, "Palaba Ingresada",Toast.LENGTH_SHORT).show();

    }
}
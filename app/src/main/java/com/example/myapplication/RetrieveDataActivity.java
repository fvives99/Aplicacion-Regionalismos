package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.service.autofill.RegexValidator;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RetrieveDataActivity extends AppCompatActivity {


    private static RetrieveDataActivity instance;

    List<Palabras> buscaPalabras;
    List<String> palabrasString;
    ListView myListview;
    List<Palabras> palabrasList;
    AutoCompleteTextView mySearchView;
    DatabaseReference regionalismosDbRef;
    Button buttonmapa;

    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_data);

        myListview = findViewById(R.id.myListView);

        palabrasList = new ArrayList<>();

        buscaPalabras = new ArrayList<>();
        regionalismosDbRef = FirebaseDatabase.getInstance().getReference("Paises");
        mySearchView = findViewById(R.id.buscarpalabra);

        buttonmapa = findViewById(R.id.buttonmapa);

        myListview.setAdapter(arrayAdapter);

        // arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,palabrasString);


        ValueEventListener event = new ValueEventListener(){


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                populateSearch(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        regionalismosDbRef.addListenerForSingleValueEvent(event);

        myListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });

        buttonmapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RetrieveDataActivity.this, MapsActivity.class));
            }
        });

        regionalismosDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                palabrasList.clear();

                for (DataSnapshot palabrasData : dataSnapshot.getChildren()) {
                    Palabras palabra = palabrasData.getValue(Palabras.class);
                    String key = palabrasData.getKey();
                    palabra.setId(key);
                    palabrasList.add(palabra);
                }
                ListAdapter adapter = new ListAdapter(RetrieveDataActivity.this, palabrasList);
                myListview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        myListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Palabras palabras = palabrasList.get(position);
                showUpdateDialog(palabras.getId(), palabras.getPalabra(), palabras.getPais());
                return false;
            }
        });
    instance=this;
    }

    public static RetrieveDataActivity getInstance(){
        return instance;
    }

    private void populateSearch(DataSnapshot snapshot) {
        ArrayList<String> palabras = new ArrayList<>();
        if(snapshot.exists()){
            for(DataSnapshot ds: snapshot.getChildren()){
                String name=ds.child("palabra").getValue(String.class);
                palabras.add(name);
            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,palabras);
            mySearchView.setAdapter(adapter);
            mySearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String name = mySearchView.getText().toString();
                    buscarPalabra(name, "palabra");
                }
            });
        }else{
            Log.d("paises","no existe");
        }
    }

    public void buscarPalabra(String name, String tipo) {

        Query query=regionalismosDbRef.orderByChild(tipo).equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Palabras palabras = new Palabras(ds.child("palabra").getValue(String.class),ds.child("significado").getValue(String.class),ds.child("pais").getValue(String.class));
                        String key = ds.getKey();
                        palabras.setId(key);
                        buscaPalabras.add(palabras);
                    }
                    ListAdapter adapter = new ListAdapter(RetrieveDataActivity.this, buscaPalabras);
                    palabrasList.clear();
                    palabrasList=buscaPalabras;
                    myListview.setAdapter(adapter);
                }else{
                    Log.d("palabra","no existe");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void showUpdateDialog(String id, String palabra, String pais) {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View mDialogView = inflater.inflate(R.layout.activity_listdata, null);

        mDialog.setView(mDialogView);

        EditText txpalabra2 = mDialogView.findViewById(R.id.txpalabra2);
        EditText txsignificado2 = mDialogView.findViewById(R.id.txsignificado2);
        Button buttonupdate = mDialogView.findViewById(R.id.buttonupdate);
        Button buttondelete = mDialogView.findViewById(R.id.buttondelete);

        mDialog.setTitle("Actualizando: " + palabra);
        mDialog.show();
        buttonupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newpalabra = txpalabra2.getText().toString();
                String newsignificado = txsignificado2.getText().toString();

                updateData(id, newpalabra, newsignificado, pais);

                Toast.makeText(RetrieveDataActivity.this, "actualizado", Toast.LENGTH_SHORT).show();


            }
        });

        buttondelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteRecord(id);

                Toast.makeText(RetrieveDataActivity.this, "Eliminado", Toast.LENGTH_SHORT).show();
            }
        });
    }
        private void showToast(String message){
            Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
        }

        private void deleteRecord(String id){
            regionalismosDbRef = FirebaseDatabase.getInstance().getReference("Paises").child(id);
            Task<Void> mTask = regionalismosDbRef.removeValue();
            mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    showToast("Eliminado");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showToast("Error eliminando");
                }
            });


        }


    private void updateData(String id, String palabra, String significado, String pais) {
        regionalismosDbRef = FirebaseDatabase.getInstance().getReference("Paises").child(id);
        Palabras palabras = new Palabras(palabra, significado, pais);
        regionalismosDbRef.setValue(palabras);


    }



}
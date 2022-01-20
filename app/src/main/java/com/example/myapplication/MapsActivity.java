package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.myapplication.databinding.ActivityMapsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Marker marker;
    private ActivityMapsBinding binding;

    DatabaseReference regionalismosDbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        regionalismosDbRef = FirebaseDatabase.getInstance().getReference("Paises");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap=googleMap;

        LatLng belice = new LatLng(17,-88);
        LatLng guatemala = new LatLng(15,-90);
        LatLng elsalvador = new LatLng(13,-88);
        LatLng honduras = new LatLng(14,-86);
        LatLng nicaragua = new LatLng(12,-84);
        LatLng costarica = new LatLng(10,-84);
        LatLng panama = new LatLng(8,-80);



        marker = mMap.addMarker(new MarkerOptions()
            .position(belice)
            .title("Belice")
        );

        marker = mMap.addMarker(new MarkerOptions()
                .position(guatemala)
                .title("Guatemala")
        );

        marker = mMap.addMarker(new MarkerOptions()
                .position(elsalvador)
                .title("El Salvador")
        );

        marker = mMap.addMarker(new MarkerOptions()
                .position(honduras)
                .title("Honduras")
        );

        marker = mMap.addMarker(new MarkerOptions()
                .position(nicaragua)
                .title("Nicaragua")
        );

        marker = mMap.addMarker(new MarkerOptions()
                .position(costarica)
                .title("Costa Rica")
        );

        marker = mMap.addMarker(new MarkerOptions()
                .position(panama)
                .title("Panama")
        );

        mMap.setOnMarkerClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(belice));

    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        RetrieveDataActivity.getInstance().buscarPalabra(marker.getTitle(),"pais");
        //startActivity(new Intent(MapsActivity.this, RetrieveDataActivity.class));
        Toast.makeText(MapsActivity.this, "Filtro Aplicado", Toast.LENGTH_LONG).show();

        return false;
    }


    /*public void buscarPais(String name) {
        System.out.printf("_---------------------------------------ddddddddddddddddddddddddd--------");

        System.out.println(name);

        Query query=regionalismosDbRef.orderByChild("pais").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    for(DataSnapshot ds : snapshot.getChildren()){
                        Palabras palabras = new Palabras(ds.child("palabra").getValue(String.class),ds.child("significado").getValue(String.class),ds.child("pais").getValue(String.class));
                        String key = ds.getKey();
                        palabras.setId(key);
                        buscarPais.add(palabras);
                    }
                    ListAdapter adapter = new ListAdapter(MapsActivity.this, buscarPais);
                    myListView.setAdapter(adapter);
                }else{
                    Log.d("palabra","no existe");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
*/

}
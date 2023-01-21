package com.example.todolistextended;

import static com.example.todolistextended.AddEditTaskActivity.REQUEST_LATITUDE;
import static com.example.todolistextended.AddEditTaskActivity.REQUEST_LONGITUDE;
import static com.example.todolistextended.MainActivity.EDIT_TODO_ACTIVITY_REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.todolistextended.databinding.ActivityMapsBinding;
import com.google.android.material.snackbar.Snackbar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Button saveButton;
    public static String RESULT_LATITUDE="ResultLat";
    public static String RESULT_LONGITUDE="ResultLong";
    public double setLangitude=0;
    public double setLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        saveButton=findViewById(R.id.submit_button);
        saveButton.setOnClickListener(OnSaveClickListener());
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
        mMap = googleMap;


        double lang = getIntent().getDoubleExtra(REQUEST_LATITUDE, 53.133);
        double longitude = getIntent().getDoubleExtra(REQUEST_LONGITUDE, 23.15);
        // Add a marker in Sydney and move the camera
        LatLng bialystok = new LatLng(lang, longitude);
        mMap.addMarker(new MarkerOptions().position(bialystok).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bialystok));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Current Location"));
                setLangitude=latLng.latitude;
                setLongitude=latLng.longitude;
                //return false;
            }
        });


    }
    private View.OnClickListener OnSaveClickListener() {
        return e -> {
            Intent replyIntent = new Intent();
            replyIntent.putExtra(RESULT_LATITUDE,setLangitude);
            replyIntent.putExtra(RESULT_LONGITUDE,setLongitude);
            setResult(RESULT_OK, replyIntent);
            finish();
        };
    }
}
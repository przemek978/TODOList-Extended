package com.example.todolistextended;

import static com.example.todolistextended.AddEditTaskActivity.REQUEST_LATITUDE;
import static com.example.todolistextended.AddEditTaskActivity.REQUEST_LONGITUDE;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.todolistextended.databinding.ActivityMapsBinding;

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double lang = getIntent().getDoubleExtra(REQUEST_LATITUDE, 53.133);
        double longitude = getIntent().getDoubleExtra(REQUEST_LONGITUDE, 23.15);
        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(lang, longitude);
        mMap.addMarker(new MarkerOptions().position(location).title(getString(R.string.loc_message)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.loc_message)));
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


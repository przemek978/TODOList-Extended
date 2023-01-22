package com.example.todolistextended;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todolistextended.DB.TaskViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.example.todolistextended.MainActivity.EDIT_TODO_ACTIVITY_REQUEST_CODE;
import static com.example.todolistextended.MainActivity.NEW_TODO_ACTIVITY_REQUEST_CODE;

public class AddEditTaskActivity extends AppCompatActivity implements SensorEventListener {
    public static final String EXTRA_EDIT_TODO_ID = "pb.edu.pl.EDIT_BOOK_TITLE";
    public final String DATE_PATTERN = "dd.MM.yyyy HH:mm";
    public final String EXTRA_REQUEST_CODE = "requestCode";
    public final int EXTRA_REQUEST_MAP=0;
    private Task task;
    private TextView nameField, descriptionField;
    private Button button;
    private CheckBox doneCheckBox;
    private EditText datefield;
    private Spinner categorySpinner;
    private TaskViewModel taskViewModel;
    private final Calendar calendar= Calendar.getInstance();
    private Date selectedDate;
    private int requestCode;
    private Button getLocationButton;
    private Location lastLocation;
    private TextView locationTextView;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView addressTextView;
    private SensorManager sensorManager;
    private Sensor sensor;
    private float lastValue;
    public static final int REQUEST_LOCATION_PERMISSION = 100;
    public static String REQUEST_LATITUDE="Latitude";
    public static String REQUEST_LONGITUDE="Longitude";
    public String TAG = "Location";
    private static final int SHAKE_THRESHOLD = 800;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit_task);
        this.requestCode = getIntent().getIntExtra(EXTRA_REQUEST_CODE, NEW_TODO_ACTIVITY_REQUEST_CODE);
        nameField = findViewById(R.id.task_name);
        descriptionField = findViewById(R.id.task_description);
        doneCheckBox = findViewById(R.id.task_done);
        datefield = findViewById(R.id.task_date);
        categorySpinner = findViewById(R.id.task_category);
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        ///Lokalizacja//////////////////////////////////////////////////////////////////////////////////
        getLocationButton = findViewById(R.id.get_location_button);
        getLocationButton.setOnClickListener((View v)->getLocation());
        locationTextView = findViewById(R.id.textview_location);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //addressTextView = findViewById(R.id.textview_address);
        Button getAddressButton = findViewById(R.id.open_maps_button);
        getAddressButton.setOnClickListener(v->openMap());
        /////////////////////////////////////////////////////////////
        updateLabel(calendar.getTime());
        //datefield.setText(calendar.getTime().toString());
        final Button button = findViewById(R.id.button_save);

        button.setOnClickListener(OnSaveClickListener());
        DatePickerDialog.OnDateSetListener date = (view12, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            TimePickerDialog timepicker;
            timepicker =new TimePickerDialog( this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                    calendar.set(Calendar.HOUR, hour);
                    calendar.set(Calendar.MINUTE,minute);
                    updateLabel();
                }
            },calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),true);
            timepicker.show();
        };

        datefield.setOnClickListener(view1 ->
                new DatePickerDialog(this, date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .show());
        int sensorType = getIntent().getIntExtra("EXTRA_SENSOR_TYPE_PARAMETER", Sensor.TYPE_ACCELEROMETER);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);
        if (this.requestCode == EDIT_TODO_ACTIVITY_REQUEST_CODE) {
            EditView();
        } else {
            AddView();
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (sensor!= null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];
        long curTime = System.currentTimeMillis();
        long  lastUpdate = 0;
        if((currentValue-lastValue>4 || currentValue-lastValue<-4)){
            if((curTime - lastUpdate) > 1000)
            {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                task.setDone(!task.isDone());
                doneCheckBox.setChecked(task.isDone());
                Log.d("Shake", "Skake detect");
            }
        }
        lastValue=currentValue;
//        float x,y,z,last_x=0,last_y=0,last_z=0;
//        long  lastUpdate = 0;
//        if (sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER) {
//            long curTime = System.currentTimeMillis();
//            // only allow one update every 100ms.
//            if ((curTime - lastUpdate) > 100) {
//                long diffTime = (curTime - lastUpdate);
//                lastUpdate = curTime;
//
//                x = sensorEvent.values[SensorManager.DATA_X];
//                y = sensorEvent.values[SensorManager.DATA_Y];
//                z = sensorEvent.values[SensorManager.DATA_Z];
//
//                float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;
//
//                if (speed > SHAKE_THRESHOLD) {
//                    task.setDone(!task.isDone());
//                    doneCheckBox.setChecked(task.isDone());
//                    Log.d("sensor", "shake detected w/ speed: " + speed);
//                    Toast.makeText(this, "shake detected w/ speed: " + speed, Toast.LENGTH_SHORT).show();
//                }
//                last_x = x;
//                last_y = y;
//                last_z = z;
//            }
//        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("AccuracyTag", "Work");
    }
    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            Log.d(TAG, "getLocation: permissions granted");
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location->{
            if(location!=null){
                lastLocation = location;
                task.setLocation(location.getLatitude(),location.getLongitude());
                locationTextView.setText(
                        getString(R.string.location_text, location.getLatitude(), location.getLongitude(), location.getTime()));
            }
            else{
                locationTextView.setText(R.string.no_location);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(this, R.string.location_permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    private String locationGecoding(Context context, Location location){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        String resultMessage = "";

        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        }
        catch (IOException ioException){
            resultMessage = context.getString(R.string.service_not_available);
            Log.e(TAG, resultMessage, ioException);
        }

        if(addresses == null || addresses.isEmpty()){
            if(resultMessage.isEmpty()){
                resultMessage = context.getString(R.string.no_address_found);
                Log.e(TAG, resultMessage);
            }
        }
        else{
            Address address = addresses.get(0);
            List<String> addressParts = new ArrayList<>();

            for(int i=0; i<=address.getMaxAddressLineIndex(); i++){
                addressParts.add(address.getAddressLine(i));
            }
            resultMessage = TextUtils.join("\n", addressParts);
        }
        return resultMessage;
    }
    private void openMap(){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(REQUEST_LATITUDE, task.getLatitude());
        intent.putExtra(REQUEST_LONGITUDE, task.getLongitude());
        startActivityForResult(intent,EXTRA_REQUEST_MAP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        task.setLocation(data.getDoubleExtra(MapsActivity.RESULT_LATITUDE,task.getLatitude()),data.getDoubleExtra(MapsActivity.RESULT_LONGITUDE,task.getLongitude()));
        locationTextView.setText(getString(R.string.location_text, task.getLatitude(), task.getLongitude(),calendar.getTime()));
    }

    private View.OnClickListener OnSaveClickListener() {
        return e -> {
            Intent replyIntent = new Intent();
            if (TextUtils.isEmpty(nameField.getText())) {
                Snackbar.make(findViewById(R.id.edit_Layout), getResources().getString(R.string.name_empty),
                                Snackbar.LENGTH_LONG)
                        .show();
            } else {
                String name = nameField.getText().toString();
                task.setName(name);
                task.setDone(doneCheckBox.isChecked());
                task.setDescription(descriptionField.getText().toString());
                if (datefield != null) {
                    task.setDate(selectedDate);
                }
                if (this.requestCode == EDIT_TODO_ACTIVITY_REQUEST_CODE) {
                    taskViewModel.update(task);
                }
                else {
                    taskViewModel.insert(task);
                }
                task = null;
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        };
    }
    private void updateLabel(){
        Locale locale= new Locale("pl","PL");
        this.selectedDate = calendar.getTime();
        SimpleDateFormat DateFormat = new SimpleDateFormat(DATE_PATTERN, locale);
        datefield.setText(DateFormat.format(selectedDate));
    }
    private void updateLabel(Date date) {
        Locale locale= new Locale("pl","PL");
        SimpleDateFormat DateFormat = new SimpleDateFormat(DATE_PATTERN, locale);
        datefield.setText(DateFormat.format(date));
    }
    private void AddView(){
        task= new Task();
        categorySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Category.values()));
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task.setCategory(Category.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        categorySpinner.setSelection(task.getCategory().ordinal());
    }
    private void EditView() {
        int selectedItemId = getIntent().getIntExtra(EXTRA_EDIT_TODO_ID, 0);
        taskViewModel.findById(selectedItemId).observe(this, task -> {
            this.task= task;
            nameField.setText(task.getName());
            descriptionField.setText(task.getDescription());
            doneCheckBox.setChecked(task.isDone());
            locationTextView.setText( getString(R.string.location_text,task.getLatitude(), task.getLongitude(),calendar.getTime()));
            updateLabel(task.getDate());
            categorySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,Category.values()));
            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    task.setCategory(Category.values()[position]);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            categorySpinner.setSelection(task.getCategory().ordinal());
            Date date = task.getDate();
            if (date != null) {
                calendar.setTime(date);
                selectedDate = date;
                updateLabel(selectedDate);
            }
        });
    }
}

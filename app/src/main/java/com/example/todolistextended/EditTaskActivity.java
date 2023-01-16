package com.example.todolistextended;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.text.Editable;
import android.text.TextWatcher;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.todolistextended.DB.TaskViewModel;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.todolistextended.MainActivity.EDIT_TODO_ACTIVITY_REQUEST_CODE;
import static com.example.todolistextended.MainActivity.NEW_TODO_ACTIVITY_REQUEST_CODE;

public class EditTaskActivity extends AppCompatActivity {
    public static final String EXTRA_EDIT_TODO_ID = "pb.edu.pl.EDIT_BOOK_TITLE";
    public static final String EXTRA_SEARCH_PLACE_QUERY = "pb.edu.pl.EDIT_BOOK_AUThOR";
    int MIN_SEARCH_INPUT_LENGTH = 3;
    public final String DATE_PATTERN = "dd-MM-yyyy";
    public final String EXTRA_REQUEST_CODE = "requestCode";
    private Task task;
    private TextView nameField;
    private Button button;
    private CheckBox doneCheckBox;
    private EditText datefield;
    private Spinner categorySpinner;
    private TaskViewModel taskViewModel;
    private final Calendar calendar= Calendar.getInstance();
    private int requestCode;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addedit_task);
        this.requestCode = getIntent().getIntExtra(EXTRA_REQUEST_CODE, NEW_TODO_ACTIVITY_REQUEST_CODE);
        nameField = findViewById(R.id.task_name);
        doneCheckBox = findViewById(R.id.task_done);
        datefield = findViewById(R.id.task_date);
        categorySpinner = findViewById(R.id.task_category);
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(OnSaveClickListener());
        DatePickerDialog.OnDateSetListener date = (view12, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            setupDateFieldValue(calendar.getTime());
            task.setDate(calendar.getTime());
        };
        datefield.setOnClickListener(view1 ->
                new DatePickerDialog(this, date, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .show());
        if (this.requestCode == EDIT_TODO_ACTIVITY_REQUEST_CODE) {
            setUpEditView();
        }

    }
    private View.OnClickListener OnSaveClickListener() {
        return e -> {
            Intent replyIntent = new Intent();
            finish();
        };
    }
    private void setupDateFieldValue(Date date){
        Locale locale= new Locale("pl","PL");
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy",locale);
        datefield.setText(dateFormat.format(date));
    }
    private void setUpEditView() {
        int selectedItemId = getIntent().getIntExtra(EXTRA_EDIT_TODO_ID, 0);
        taskViewModel.findById(selectedItemId).observe(this, task -> {
            this.task= task;
            nameField.setText(task.getName());
            doneCheckBox.setChecked(task.isDone());
            datefield.setText(task.getDate().toString());
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
//            Long toDoItemDate = toDoItem.getDate();
//            if (toDoItemDate != null) {
//                calendar.setTimeInMillis(toDoItemDate);
//                selectedDate = new Date(toDoItemDate);
//                updateLabel(selectedDate);
//            }
//            if (PlaceType.DEFINED.equals(currentTodo.getPlaceType())) {
//                final Button navButton = findViewById(R.id.button_nav);
//                navButton.setVisibility(View.VISIBLE);

//                navButton.setOnClickListener(v -> {
//                    Intent intent = new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("google.navigation:q=" + currentTodo.getPlaceAddress()));
//                    startActivity(intent);
//
//                });
//            }
        });
    }
}

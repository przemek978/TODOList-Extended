package com.example.todolistextended;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TaskFragment extends Fragment {

    private Task task;
    private TextView nameField;
    private Button dateButton;
    private CheckBox doneCheckBox;
    private EditText datefield;
    private Spinner categorySpinner;
    private final Calendar calendar= Calendar.getInstance();
    private final static String ARG_TASK_ID="ARG_TASK_ID";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //task = new Task();

        int taskId=(int) getArguments().getSerializable(ARG_TASK_ID);
        task=TaskStorage.getInstance().getTask(taskId);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.task_fragment, container, false);

        nameField = view.findViewById(R.id.task_name);
        doneCheckBox=view.findViewById(R.id.task_done);
        //dateButton=view.findViewById(R.id.task_date);
        datefield=view.findViewById(R.id.task_date);
        categorySpinner=view.findViewById(R.id.task_category);
        DatePickerDialog.OnDateSetListener date=(view12, year, month, day)-> {
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,day);
            setupDateFieldValue(calendar.getTime());
            task.setDate(calendar.getTime());
        };
        datefield.setOnClickListener(view1->
                new DatePickerDialog(getContext(),date,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .show() );
        setupDateFieldValue(task.getDate());
        if(container != null){
            if(nameField != null) {
                nameField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        task.setName(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                nameField.setText(task.getName());
            }

            if(dateButton != null){
                dateButton.setText(task.getDate().toString());
                dateButton.setEnabled(false);
            }

            if(doneCheckBox != null){
                doneCheckBox.setChecked(task.isDone());
                doneCheckBox.setOnCheckedChangeListener((buttonView, isChecked) ->{task.setDone(isChecked);});
            }

            categorySpinner.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item,Category.values()));
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
        return view;
    }
    private void setupDateFieldValue(Date date){
        Locale locale= new Locale("pl","PL");
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd.MM.yyyy",locale);
        datefield.setText(dateFormat.format(date));
    }
    public static TaskFragment newInstance(UUID taskId){
        Bundle bundle=new Bundle();
        bundle.putSerializable(ARG_TASK_ID,taskId);
        TaskFragment taskFragment = new TaskFragment();
        taskFragment.setArguments(bundle);
        return taskFragment;
    }
}
package com.example.todolistextended;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.SearchView;


import com.example.todolistextended.DB.TaskViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {

    public static final int NEW_TODO_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_TODO_ACTIVITY_REQUEST_CODE = 2;
    TaskAdapter adapter;
    private TaskViewModel taskViewModel;
    private LiveData<List<Task>> liveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        liveData = taskViewModel.findAll();
        loadAllTasks();

        FloatingActionButton addToDoItemButton = findViewById(R.id.add_button);
        addToDoItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivityForResult(intent, NEW_TODO_ACTIVITY_REQUEST_CODE);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.task_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void loadAllTasks() {
        liveData.removeObservers(this);
        liveData = taskViewModel.findAll();
        liveData.observe(this, adapter::setTasks);
    }

    private void loadTasksByState(boolean done) {
        liveData.removeObservers(this);
        liveData = taskViewModel.findByDone(done);
        liveData.observe(this, adapter::setTasks);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra("requestCode", requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_TODO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Snackbar.make(findViewById(R.id.constraintLayout), getString(R.string.task_added),
                    Snackbar.LENGTH_LONG).show();
        } else if (requestCode == EDIT_TODO_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Snackbar.make(findViewById(R.id.constraintLayout), getString(R.string.task_edited),
                    Snackbar.LENGTH_LONG).show();
        } else {}
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText.trim();
        if (text.length() <2) {
            taskViewModel.findAll().observe(this, adapter::setTasks);
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                loadAllTasks();
                return;
            case 1:
                loadTasksByState(false);
                return;
            case 2:
                loadTasksByState(true);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        loadAllTasks();
    }

    class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {

        private List<Task> tasks;
        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TaskHolder(getLayoutInflater(),parent);
        }

        @Override
        public void onBindViewHolder(TaskHolder holder, int position) {
            if (tasks != null)
            {
               Task task = tasks.get(position);
                holder.bind(task);

            }
            else
            {
                Log.d("MainActivity", "No tasks");
            }
        }

        public int getItemCount() {
            if (tasks != null)
            {
                return tasks.size();
            }
            else
            {
                return 0;
            }
        }

        public void setTasks(List<Task> tasks) {
            this.tasks = tasks;
            notifyDataSetChanged();
        }
    }
    class TaskHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView dateTextView;
        private CheckBox doneCheckBox;
        private ImageView iconImageView;
        private Task task;

        public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_task, parent, false));

            nameTextView=itemView.findViewById(R.id.task_item_name);
            dateTextView=itemView.findViewById(R.id.task_item_date);
            doneCheckBox=itemView.findViewById(R.id.task_item_done);
            iconImageView=itemView.findViewById(R.id.task_imageView);
            View taskItem = itemView.findViewById(R.id.task_item);
            itemView.setOnLongClickListener(v -> {
                taskViewModel.delete(task);
                Snackbar.make(findViewById(R.id.constraintLayout),
                                getString(R.string.task_delete),
                                Snackbar.LENGTH_LONG)
                        .show();
                return true;
            });
            taskItem.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                intent.putExtra(AddEditTaskActivity.EXTRA_EDIT_TODO_ID, task.getId());
                startActivityForResult(intent, MainActivity.EDIT_TODO_ACTIVITY_REQUEST_CODE);
            });
            doneCheckBox.setOnClickListener(v -> {
                task.setDone(!task.isDone());
                taskViewModel.update(task);
            });
        }

        public void bind(Task task){        //assign task to its dispalyed view
            this.task=task;
            nameTextView.setText(task.getName());
            dateTextView.setText(task.getDate().toString());
            doneCheckBox.setChecked(task.isDone());

            if(doneCheckBox.isChecked())
            {
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else
            {
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() &~ Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if(task.getCategory().equals(Category.HOME))
            {
                iconImageView.setImageResource(R.drawable.ic_house);
            }
            else
            {
                iconImageView.setImageResource(R.drawable.ic_studies);
            }
        }
    }
}
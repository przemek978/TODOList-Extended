package com.example.todolistextended;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public abstract class SingleFragmentActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        FragmentManager fragmentManager= getSupportFragmentManager();
//        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
//
//        if(fragment==null)
//        {
//            //fragment= new TaskFragment();
//            fragment=createFragment();
//            fragmentManager
//                    .beginTransaction()
//                    .add(R.id.fragment_container,fragment)
//                    .commit();
//        }
    }
    protected abstract Fragment createFragment();
}
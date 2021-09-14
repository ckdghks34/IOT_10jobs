package com.example.a10jobs;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.a10jobs.controlFragment.FragmentLivingRoom;
import com.example.a10jobs.controlFragment.FragmentRoom1;
import com.example.a10jobs.controlFragment.FragmentRoom2;
import com.example.a10jobs.controlFragment.FragmentRoom3;

public class ControlActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentLivingRoom fragmentLivingRoom;
    private FragmentRoom1 fragmentRoom1;
    private FragmentRoom2 fragmentRoom2;
    private FragmentRoom3 fragmentRoom3;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_room);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.room, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        fragmentManager = getSupportFragmentManager();

        fragmentLivingRoom = new FragmentLivingRoom();
        fragmentRoom1 = new FragmentRoom1();
        fragmentRoom2 = new FragmentRoom2();
        fragmentRoom3 = new FragmentRoom3();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                transaction = fragmentManager.beginTransaction();


                if (i==0) transaction.replace(R.id.controlLayout, fragmentLivingRoom);
                else if (i==1) transaction.replace(R.id.controlLayout, fragmentRoom1);
                else if (i==2) transaction.replace(R.id.controlLayout, fragmentRoom2);
                else transaction.replace(R.id.controlLayout, fragmentRoom3);

//                transaction.addToBackStack(null);
                transaction.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                transaction.replace(R.id.controlLayout, fragmentLivingRoom).commitAllowingStateLoss();
            }

        });
    }
}



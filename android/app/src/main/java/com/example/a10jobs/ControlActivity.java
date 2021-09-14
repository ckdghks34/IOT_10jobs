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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.a10jobs.controlFragment.FragmentLivingRoom;
import com.example.a10jobs.controlFragment.FragmentRoom1;

public class ControlActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentLivingRoom fragmentLivingRoom;
    private FragmentRoom1 fragmentRoom1;
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



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                transaction = fragmentManager.beginTransaction();

                if (i==0) transaction.replace(R.id.controlLayout, fragmentLivingRoom);
                else if (i==1) transaction.replace(R.id.controlLayout, fragmentRoom1);

                transaction.addToBackStack(null);
                transaction.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                transaction.replace(R.id.controlLayout, fragmentLivingRoom).commitAllowingStateLoss();
            }

        });

        //에어컨 전원
        ToggleButton airconToggle = (ToggleButton) findViewById(R.id.airconButton);
        airconToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView aircon = (TextView) findViewById(R.id.airconState);
                ImageView airconImg = (ImageView) findViewById(R.id.aircon);
                if (isChecked) {
                    aircon.setText("켜짐");
                    airconImg.setImageResource(R.drawable.airconon);
                } else {
                    aircon.setText("꺼짐");
                    airconImg.setImageResource(R.drawable.airconoff);
                }
            }
        });


        // TV 전원
        ToggleButton tvToggle = (ToggleButton) findViewById(R.id.tvButton);
        tvToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView aircon = (TextView) findViewById(R.id.tvState);
                ImageView airconImg = (ImageView) findViewById(R.id.tv);
                if (isChecked) {
                    aircon.setText("켜짐");
                    airconImg.setImageResource(R.drawable.tv);
                } else {
                    aircon.setText("꺼짐");
                    airconImg.setImageResource(R.drawable.tv);
                }
            }
        });

        // 커튼 열림 / 닫힘
        ToggleButton curtainToggle = (ToggleButton) findViewById(R.id.curtainButton);
        curtainToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView aircon = (TextView) findViewById(R.id.curtainState);
                ImageView airconImg = (ImageView) findViewById(R.id.curtain);
                if (isChecked) {
                    aircon.setText("열림");
                    airconImg.setImageResource(R.drawable.curtain);
                } else {
                    aircon.setText("닫힘");
                    airconImg.setImageResource(R.drawable.curtain);
                }
            }
        });

        // 공기청정기 전원
        ToggleButton airconditionerToggle = (ToggleButton) findViewById(R.id.airconditionerButton);
        airconditionerToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView aircon = (TextView) findViewById(R.id.airconditionerState);
                ImageView airconImg = (ImageView) findViewById(R.id.airconditioner);
                if (isChecked) {
                    aircon.setText("켜짐");
                    airconImg.setImageResource(R.drawable.airconditioner);
                } else {
                    aircon.setText("꺼짐");
                    airconImg.setImageResource(R.drawable.airconditioner);
                }
            }
        });

        // 불 전원
        ToggleButton lightToggle = (ToggleButton) findViewById(R.id.lightButton);
        lightToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView aircon = (TextView) findViewById(R.id.lightState);
                ImageView airconImg = (ImageView) findViewById(R.id.light);
                if (isChecked) {
                    aircon.setText("켜짐");
                    airconImg.setImageResource(R.drawable.light);
                } else {
                    aircon.setText("꺼짐");
                    airconImg.setImageResource(R.drawable.light);
                }
            }
        });
    }
}



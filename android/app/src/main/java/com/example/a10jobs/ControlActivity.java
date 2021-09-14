package com.example.a10jobs;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class ControlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_room);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.room, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ToggleButton toggle = (ToggleButton) findViewById(R.id.airconButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
    }
}

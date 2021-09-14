package com.example.a10jobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class FindClickedActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_clicked);

        Intent intent = getIntent();

        ImageView img = (ImageView)findViewById(R.id.findcliked_img);
        TextView title = (TextView)findViewById(R.id.findclicked_title);
        TextView date = (TextView)findViewById(R.id.findcliked_info);

        img.setImageResource(intent.getIntExtra("img", 0));
        title.setText(intent.getStringExtra("title"));
        date.setText(intent.getStringExtra("date"));
    }

}
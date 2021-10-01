package com.example.a10jobs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MapCreateActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_create);

        init();
    }

    private void init() {
        mSocket = IO.socket('http://j5d201.p.ssafy.io:12001/');
        
    }


}

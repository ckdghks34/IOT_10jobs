package com.example.a10jobs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MapActivity extends AppCompatActivity {
    Button btn_map;
    String url = getString(R.string.url);
    Socket socket;
    {
        try{
            socket = IO.socket(url);
        } catch(URISyntaxException e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        socket.connect();

        btn_map = (Button) findViewById(R.id.btn_make_map);
        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 소켓 통신(서버쪽으로 이벤트 발생시키기)
                socket.emit("makeMapToServer");
            }
        });
   }
}
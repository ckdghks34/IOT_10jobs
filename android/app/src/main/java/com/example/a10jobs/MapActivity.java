package com.example.a10jobs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class MapActivity extends AppCompatActivity {
    Button btn_map;
    String url = "http://j5d201.p.ssafy.io:12001";
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
                // data를 같이 보내야 하는지? (patrol_client처럼 cmd같은 숫자)
                socket.emit("makeMapToServer");
            }
        });
   }
}
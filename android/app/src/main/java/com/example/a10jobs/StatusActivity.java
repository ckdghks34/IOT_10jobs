package com.example.a10jobs;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class StatusActivity extends AppCompatActivity {
    LinearLayout power, battery, now;

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
        setContentView(R.layout.activity_status);
        socket.connect();

        power = (LinearLayout)findViewById(R.id.power);
        battery = (LinearLayout)findViewById(R.id.battery);
        now = (LinearLayout)findViewById(R.id.now);

        power.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 소켓 통신(서버 쪽에서 보낸 이벤트 듣기)
                socket.on("sendBotStatus", onStatus);
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner_status);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    // 리스너 -> 이벤트를 보냈을 때 이 리스너가 실행됨
    private Emitter.Listener onStatus = new Emitter.Listener(){
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // JSon 형태로 받을 수 있음
                    String data = (String)args[0];
                    Log.e("get", data);
                }
            });
        }
    };
}

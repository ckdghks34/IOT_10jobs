package com.example.a10jobs;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

public class StatusActivity extends AppCompatActivity {
    Activity activity = this;
    ImageButton battery, now;
    TextView battery_txt;

    String url = "http://j5d201.p.ssafy.io:12001";
    Socket socket;
    {
        try {
            socket = IO.socket(url);
            Log.v("socket", String.valueOf(socket));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

//        power = (ToggleButton)findViewById(R.id.power);
        battery = (ImageButton) findViewById(R.id.battery);
        now = (ImageButton) findViewById(R.id.now);
        battery_txt = (TextView) findViewById(R.id.battery_txt);

        socket.on("sendBotStatus", onStatus);
//        socket.on("sendBotStatus", new Emitter.Listener(){
//            @Override
//            public void call(final Object... args){
//                Log.v("on", "socket on");
//                runOnUiThread(new Runnable(){
//                    @Override
//                    public void run(){
//                        try{
//                            String data = (String)args[0];
//                            Log.v("data", data);
//                        } catch(Exception e){
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });
        socket.connect();

        battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 소켓 통신(서버 쪽에서 보낸 이벤트 듣기)
                Log.v("test", "click");
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner_status);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
    // 리스너 -> 이벤트를 보냈을 때 이 리스너가 실행됨
    private Emitter.Listener onStatus = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int data = (int)args[0];
                    Log.v("data", String.valueOf(data));
                    // 항상 0이라서 그냥 100에서 뺌
                    battery_txt.setText(String.valueOf(100 - data));
                }
            });
        }
    };
}

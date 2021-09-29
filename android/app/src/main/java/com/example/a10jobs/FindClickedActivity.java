package com.example.a10jobs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class FindClickedActivity extends AppCompatActivity {
    String url = getString(R.string.url);         // 서버 url 주기
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
        setContentView(R.layout.activity_find_clicked);
        socket.connect();
        Intent intent = getIntent();

        ImageView img = (ImageView)findViewById(R.id.findcliked_img);
        TextView title = (TextView)findViewById(R.id.findclicked_title);
        TextView date = (TextView)findViewById(R.id.findcliked_info);

        img.setImageResource(intent.getIntExtra("img", 0));
        title.setText(intent.getStringExtra("title"));
        date.setText(intent.getStringExtra("date"));

        switch(intent.getStringExtra("title")){
            case "지갑 찾기":
                socket.on("sendWalletStatus", onWallet);
                break;
            case "리모컨 찾기":
                socket.on("sendRemoteStatus", onRemote);
                break;
            case "열쇠 찾기":
                socket.on("sendKeyStatus", onKey);
                break;
            case "가방 찾기":
                socket.on("sendBagStatus", onBag);
                break;
        }
    }

    // 리스너 -> 이벤트를 보냈을 때 이 리스너가 실행됨
    private Emitter.Listener onWallet = new Emitter.Listener(){
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
    // 리스너 -> 이벤트를 보냈을 때 이 리스너가 실행됨
    private Emitter.Listener onRemote = new Emitter.Listener(){
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
    // 리스너 -> 이벤트를 보냈을 때 이 리스너가 실행됨
    private Emitter.Listener onKey = new Emitter.Listener(){
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
    // 리스너 -> 이벤트를 보냈을 때 이 리스너가 실행됨
    private Emitter.Listener onBag = new Emitter.Listener(){
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
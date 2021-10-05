package com.example.a10jobs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FindClickedActivity extends AppCompatActivity {
    Bitmap bitmap;
    LottieAnimationView animationView;
    ImageView img;
    TextView date;
    Button button;
    String url = "http://j5d201.p.ssafy.io:12001";         // 서버 url 주기
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

        img = (ImageView)findViewById(R.id.findcliked_img);
        img.setVisibility(View.GONE);
        TextView title = (TextView)findViewById(R.id.findclicked_title);
        date = (TextView)findViewById(R.id.findcliked_info);
        animationView = (LottieAnimationView) findViewById(R.id.findcliked_img_lottie);
        button = (Button)findViewById(R.id.finishbutton);

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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socket.emit("PatrolOffToServer");
                onBackPressed();
            }
        });
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
                    bitmap = StringToBitmap(data);
                    img.setImageBitmap(bitmap);
                    img.setVisibility(View.VISIBLE);
                    animationView.setVisibility(View.GONE);
                    date.setText(getTime());

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
                    bitmap = StringToBitmap(data);
                    img.setImageBitmap(bitmap);
                    animationView.setVisibility(View.GONE);
                    img.setVisibility(View.VISIBLE);
                    date.setText(getTime());
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
                    bitmap = StringToBitmap(data);
                    img.setImageBitmap(bitmap);
                    animationView.setVisibility(View.GONE);
                    img.setVisibility(View.VISIBLE);
                    date.setText(getTime());
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
                    bitmap = StringToBitmap(data);
                    img.setImageBitmap(bitmap);
                    animationView.setVisibility(View.GONE);
                    img.setVisibility(View.VISIBLE);
                    date.setText(getTime());
                }
            });
        }
    };


    public static Bitmap StringToBitmap(String encodedString){
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private String getTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = dateFormat.format(date);
        return getTime;
    }

}
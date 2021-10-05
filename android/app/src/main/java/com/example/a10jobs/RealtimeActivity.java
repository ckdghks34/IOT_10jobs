package com.example.a10jobs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

public class RealtimeActivity extends AppCompatActivity {
    ImageView realView;
    ImageButton btnUp, btnDown, btnLeft, btnRight;
    Bitmap bitmap_realTime;
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
        setContentView(R.layout.activity_realtime);
        socket.on("sendStreaming", onStream);
        socket.connect();

        realView = (ImageView)findViewById(R.id.realView);
        btnUp = (ImageButton) findViewById(R.id.arrowkeys_up);
        btnDown = (ImageButton) findViewById(R.id.arrowkeys_down);
        btnLeft = (ImageButton) findViewById(R.id.arrowkeys_left);
        btnRight = (ImageButton) findViewById(R.id.arrowkeys_right);

        btnUp.setOnTouchListener(onTouchListener);
        btnDown.setOnTouchListener(onTouchListener);
        btnLeft.setOnTouchListener(onTouchListener);
        btnRight.setOnTouchListener(onTouchListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.socket.disconnect();
        Log.d("socket connection ", "disconnect");
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.socket.connect();
        Log.d("socket connection ", "Reconnect");
    }

    // Touch 이벤트
    ImageButton.OnTouchListener onTouchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:   // 버튼이 눌렸을 때
                case MotionEvent.ACTION_MOVE:   // 버튼이 눌려져 있을 때
//                  view.setBackgroundColor(Color.parseColor("#757575"));
//                  왼-1/앞-2/뒤-3/오-4
                    if(view == btnUp){
                        socket.emit("gostraightToServer", 2);
                        Log.v("click", "up");
                    }
                    else if(view == btnDown){
                        socket.emit("gobackToServer", 3);
                        Log.v("click", "down");
                    }
                    else if(view == btnLeft){
                        socket.emit("turnleftToServer", 1);
                        Log.v("click", "left");
                    }
                    else if(view == btnRight){
                        socket.emit("turnrightToServer", 4);
                        Log.v("click", "right");
                    }
                    break;
                case MotionEvent.ACTION_UP:     // 버튼 뗄 때
                    view.setBackgroundColor(Color.TRANSPARENT);
                    break;
            }
            return false;
        }
    };

    // 리스너 -> 이벤트를 보냈을 때 이 리스너가 실행됨
    private Emitter.Listener onStream = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String)args[0];
                    Log.v("data", data);
                    bitmap_realTime = StringToBitmap(data);
                    realView.setImageBitmap(bitmap_realTime);
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
}

package com.example.a10jobs;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class MapCreateActivity extends AppCompatActivity {
    Button btn_start, btn_stop;
    ImageButton btnUp, btnDown, btnLeft, btnRight;
    Bitmap bitmap;
    ImageView mapImg;
    Switch switchAuto;

    String url = "http://j5d201.p.ssafy.io:12001";
    Socket msocket;
    {
        try{
            msocket = IO.socket(url);
        } catch(URISyntaxException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_create);
        mapImg = (ImageView) findViewById(R.id.map_img);
        msocket.connect();

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);

        btnUp = (ImageButton) findViewById(R.id.arrowkeys_up);
        btnDown = (ImageButton) findViewById(R.id.arrowkeys_down);
        btnLeft = (ImageButton) findViewById(R.id.arrowkeys_left);
        btnRight = (ImageButton) findViewById(R.id.arrowkeys_right);

        switchAuto = (Switch) findViewById(R.id.switch_auto);

        btnUp.setOnTouchListener(onTouchListener);
        btnDown.setOnTouchListener(onTouchListener);
        btnLeft.setOnTouchListener(onTouchListener);
        btnRight.setOnTouchListener(onTouchListener);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msocket.emit("start_createmap");
                btn_start.setVisibility(btn_start.GONE);
                btn_stop.setVisibility(btn_stop.VISIBLE);
                switchAuto.setVisibility(switchAuto.VISIBLE);
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MapCreateActivity.this);
                dlg.setTitle("맵 저장"); //제목
                dlg.setMessage("맵을 저장하시겠습니까? \n이후 새로 맵을 만들 수 있습니다."); // 메시지
                dlg.setIcon(R.drawable.robot); // 아이콘 설정
//                버튼 클릭시 동작
                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        // imgview의 drawable을 가져와서
                        Drawable tmpImg = mapImg.getDrawable();

                        // bitmap으로 변환시킨후
                        Bitmap bitmap = ((BitmapDrawable)tmpImg).getBitmap();

                        // 받아온 bitmap을 jpg로 변환해 캐시에 저장합니다.
                        saveBitmapToJpeg(bitmap);
                        
                        Toast.makeText(MapCreateActivity.this,"저장되었습니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        msocket.emit("stop_createmap");
                    }
                });
                dlg.setNegativeButton("취소",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("check", "취소버튼");
                    }
                });
                dlg.show();
//                msocket.emit("stop_createmap");
//                btn_stop.setVisibility(btn_start.GONE);
//                btn_start.setVisibility(btn_stop.VISIBLE);
            }
        });
        switchAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // 스위치 버튼이 체크되었는지 검사하여 텍스트뷰에 각 경우에 맞게 출력합니다.
                if (isChecked){
                    Log.d("check","옵션 활성화");
                    msocket.emit("mapAutoOnToServer");

                }else{
                    Log.d("check","옵션 비활성화");
                    msocket.emit("mapAutoOffToServer");
                }
            }
        });
        
        msocket.on("sendMapStreaming", getImg);
    }


    ImageButton.OnTouchListener onTouchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:   // 버튼이 눌렸을 때
                case MotionEvent.ACTION_MOVE:   // 버튼이 눌려져 있을 때
//                  view.setBackgroundColor(Color.parseColor("#757575"));
//                  왼-1/앞-2/뒤-3/오-4
                    if(view == btnUp){
                        msocket.emit("gostraightToServer", 2);
                        Log.v("click", "up");
                    }
                    else if(view == btnDown){
                        msocket.emit("gobackToServer", 3);
                        Log.v("click", "down");
                    }
                    else if(view == btnLeft){
                        msocket.emit("turnleftToServer", 1);
                        Log.v("click", "left");
                    }
                    else if(view == btnRight){
                        msocket.emit("turnrightToServer", 4);
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

    private Emitter.Listener getImg = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String)args[0];
                    bitmap = StringToBitmap(data);
                    mapImg.setImageBitmap(bitmap);
                }
            });
        }
    };

    private void saveBitmapToJpeg(Bitmap bitmap) {
        // 내부 저장소 캐시 경로를 받아온다
        File storage = getCacheDir();
        Log.d("test", "" + storage);

        // 저장할 파일 이름
        String fileName = "map.jpg";

        // storage에 파일 인스턴스를 생성한다
        File tempFile = new File(storage, fileName);
        try {
            // 자동으로 빈 파일을 생성합니다.
            tempFile.createNewFile();

            // 파일을 쓸 수 있는 스트림을 준비합니다.
            FileOutputStream out = new FileOutputStream(tempFile);

            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            // 스트림 사용후 닫아줍니다.
            out.close();
        
        } catch (FileNotFoundException e) {
            Log.e("MyTag","FileNotFoundException : " + e.getMessage());
        } catch (IOException e) {
            Log.e("MyTag","IOException : " + e.getMessage());
        }

    }

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




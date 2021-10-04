package com.example.a10jobs;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

public class MapActivity extends AppCompatActivity {
    ImageView mapImg;
    Button btn_newMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapImg = (ImageView) findViewById(R.id.map_img);
        btn_newMap = (Button) findViewById(R.id.btn_newmap);
        btn_newMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCache(getCacheDir());
                Intent intent = new Intent(getApplicationContext(), MapCreateActivity.class);
                startActivity(intent);
            }
        });
        btn_newMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MapActivity.this);
                dlg.setTitle("새로운 맵 생성"); //제목
                dlg.setMessage("기존의 맵 데이터는 삭제됩니다 \n새로운 맵을 생성하시겠습니까?"); // 메시지
                dlg.setIcon(R.drawable.robot); // 아이콘 설정
                dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCache(getCacheDir());
                        Intent intent = new Intent(getApplicationContext(), MapCreateActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                dlg.setNegativeButton("취소",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("check", "취소버튼");
                    }
                });
                dlg.show();
            }
        });
        //blackJin 이 들어간 파일들을 저장할 배열 입니다.
        ArrayList<String> map = new ArrayList<>();

        File file = new File(getCacheDir().toString());

        File[] files = file.listFiles();

        for(File tempFile : files) {

            Log.d("MyTag",tempFile.getName());

            //blackJin 이 들어가 있는 파일명을 찾습니다.
            if(tempFile.getName().contains("map")) {

                map.add(tempFile.getName());

            }

        }
        Log.e("MyTag","map size : " + map.size());
        if(map.size() > 0) {
            int randomPosition = new Random().nextInt(map.size());

            //blackJins 배열에 있는 파일 경로 중 하나를 랜덤으로 불러옵니다.
            String path = getCacheDir() + "/" + map.get(randomPosition);

            //파일경로로부터 비트맵을 생성합니다.
            Bitmap bitmap = BitmapFactory.decodeFile(path);

            mapImg.setImageBitmap(bitmap);
        }

    }

    //캐시삭제
    private boolean deleteCache(File dir) {

        try {
            //param File이 Null이 아니여야 하고 & 디렉토리인지 확인
            if (dir != null && dir.isDirectory()) {
                //디렉토리 내 파일 리스트 호출
                String[] children = dir.list();
                //파일 리스트를 반복문으로 호출
                for (String child : children) {
                    //파일 리스트중 디렉토리가 존재할 수 있기 때문에 재귀호출
                    boolean isSuccess = deleteCache(new File(dir, child));
                    if (!isSuccess) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            Log.w("TAG", "deleteCache Error!", e);
        }

        return dir.delete();
    }
}

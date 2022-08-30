package com.example.a10jobs;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class SplashActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceStare) {
        super.onCreate(savedInstanceStare);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                startActivity(intent);
                if (getBitmapFromCacheDir()) {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(),MapCreateActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                finish();
            }
        },3000);
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    private boolean getBitmapFromCacheDir() {

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
        if(map.size() > 0 ) {
            return true;
        }else {
            return false;
        }
    };
}

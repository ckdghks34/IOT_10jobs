package com.example.a10jobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeView(View view){
//        Intent intent = null;

        switch (view.getId()){
            // 맵만들기 버튼 클릭시 화면 전환
            case R.id.button3 :
                break;
            // 기기제어 버튼 클릭시 화면 전환
            case R.id.button4 :
                break;
            // 실시간 보기 버튼 클릭시 화면 전환
            case R.id.button5 :
                Intent intent = new Intent(getApplicationContext(),RealtimeActivity.class);
                startActivity(intent);
                break;
            // 방범모드 버튼 클릭시 화면 전환
            case R.id.button6 :
                break;
            // 물건찾기 버튼 클릭시 화면 전환
            case R.id.button7 :
                break;
            // 상태보기 버튼 클릭시 화면 전환
            case R.id.button9 :
                break;

        }
    }

}
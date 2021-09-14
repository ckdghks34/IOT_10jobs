package com.example.a10jobs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
<<<<<<< HEAD
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nex3z.notificationbadge.NotificationBadge;
=======
>>>>>>> c420c327b67718b3a09a23c214a05a21f4a30e96

public class MainActivity extends AppCompatActivity {
    Button btn_map, btn_control, btn_watch, btn_find, btn_status;
    ToggleButton btn_crime;
    NotificationBadge badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
        btn_map = (Button)findViewById(R.id.btn_map);
        btn_control = (Button)findViewById(R.id.btn_control);
        btn_watch = (Button)findViewById(R.id.btn_watch);
        btn_crime = (ToggleButton)findViewById(R.id.btn_crime);
        btn_find = (Button)findViewById(R.id.btn_find);
        btn_status = (Button)findViewById(R.id.btn_status);
        badge = findViewById(R.id.badge);
        badge.setNumber(2);

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TmpActivity.class);
                startActivity(intent);
            }
        });
        btn_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TmpActivity.class);
                startActivity(intent);
            }
        });
        btn_watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TmpActivity.class);
                startActivity(intent);
            }
        });
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TmpActivity.class);
                startActivity(intent);
            }
        });
        btn_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TmpActivity.class);
                startActivity(intent);
            }
        });
    }

=======
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

>>>>>>> c420c327b67718b3a09a23c214a05a21f4a30e96
}
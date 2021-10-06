package com.example.a10jobs;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.a10jobs.Fragment.MyAdapter;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.aviran.cookiebar2.CookieBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.relex.circleindicator.CircleIndicator3;


public class MainActivity extends AppCompatActivity {
    Activity activity = this;
    Button btn_map, btn_control, btn_watch, btn_find, btn_patrol_log;
    ImageButton battery_status;
    ToggleButton btn_crime;
    ViewPager2 mPager;
    Bitmap bitmap;
    FragmentStateAdapter pagerAdapter;
    int num_page = 4;
    CircleIndicator3 mIndicator;
    int data;
    long lastTime = 0;
    long deleyTime = 5000;

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
        setContentView(R.layout.activity_main);

        socket.on("sendBotStatus", onStatus);
        socket.on("humanDetect", getImg);

        socket.connect();

        mPager = findViewById(R.id.viewpager);
        pagerAdapter = new MyAdapter(this, num_page);
        mPager.setAdapter(pagerAdapter);
        mIndicator = findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.createIndicators(num_page,0);

        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.setCurrentItem(1000);
        mPager.setOffscreenPageLimit(3);

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position%num_page);
            }
        });

        final float pageMargin= getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);

        mPager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float myOffset = position * -(2 * pageOffset + pageMargin);
                if (mPager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(mPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.setTranslationX(-myOffset);
                    } else {
                        page.setTranslationX(myOffset);
                    }
                } else {
                    page.setTranslationY(myOffset);
                }
            }
        });

        btn_map = (Button) findViewById(R.id.btn_map);
        btn_control = (Button) findViewById(R.id.btn_control);
        btn_watch = (Button) findViewById(R.id.btn_watch);
        btn_crime = (ToggleButton) findViewById(R.id.btn_crime);
        btn_find = (Button) findViewById(R.id.btn_find);
        btn_patrol_log = (Button) findViewById(R.id.btn_patrol_log);
        battery_status = (ImageButton) findViewById(R.id.battery_status);

        btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });
        btn_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ControlActivity.class);
                startActivity(intent);
            }
        });
        btn_watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RealtimeActivity.class);
                startActivity(intent);
            }
        });
        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindActivity.class);
                startActivity(intent);
            }
        });
        btn_patrol_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PatrolActivity.class);        // 어디 Activity로 갈지만 바꿔주세요!!
                startActivity(intent);
            }
        });
        battery_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                socket.on("sendBotStatus", onStatus);
                CookieBar.build(activity)
                        .setTitle("터틀봇 배터리")
                        .setMessage(data + "%입니다")
                        .setIcon(R.drawable.ic_settings_white_48dp)
                        .setIconAnimation(R.animator.iconspin)
                        .setBackgroundColor(R.color.navy)
                        .setCookiePosition(CookieBar.TOP)  // Cookie will be displayed at the bottom
                        .setDuration(5000)
                        .show();                              // of the screen
            }
        });
        btn_crime.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if(isChecked){
                            socket.emit("PatrolOnToServer", 1);
                            btn_crime.setBackgroundColor(Color.GREEN);
                            Log.d("check", "방범모드 on");
                        }else{
                            socket.emit("PatrolOffToServer", 0);
                            Log.d("check", "방범모드 off");
                        }
                    }
                }
        );


    }

    @Override
    protected void onPause() {
        super.onPause();
        socket.off("sendBotStatus");
        socket.disconnect();
        Log.v("msg", "pause 소켓 통신 해제");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        socket.on("sendBotStatus", onStatus);
        socket.connect();
        Log.v("msg", "restart 배터리 소켓 재연결");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.off("sendBotStatus");
        socket.disconnect();
        Log.v("msg", "destroy 소켓 통신 해제");
    }

    // 리스너 -> 이벤트를 보냈을 때 이 리스너가 실행됨
    private Emitter.Listener onStatus = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    data = 100 - (int)args[0];
//                    Log.v("data", String.valueOf(100 - data));
                }
            });
        }
    };

    private Emitter.Listener getImg = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String data = (String)args[0];
                    if(lastTime == 0) {
                        lastTime = System.currentTimeMillis();
                        bitmap = StringToBitmap(data);
                        Log.d("save", "저장");
                        saveBitmapToJpeg(bitmap);
                    } else {
                        if(lastTime + deleyTime < System.currentTimeMillis()) {
                            lastTime = System.currentTimeMillis();
                            bitmap = StringToBitmap(data);
                            saveBitmapToJpeg(bitmap);
                            Log.d("save", "저장");
                        }
                    }



                }
            });
        }
    };
    private void saveBitmapToJpeg(Bitmap bitmap) {
        // 내부 저장소 캐시 경로를 받아온다
        File storage = getCacheDir();
        Log.d("test", "" + storage);

        // 현재시간 가져오기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm");
        String getTime = dateFormat.format(date);


        // 저장할 파일 이름
        String fileName = getTime + " human.jpg";

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

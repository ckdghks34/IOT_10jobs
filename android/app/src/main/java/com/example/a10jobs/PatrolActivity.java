package com.example.a10jobs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class PatrolActivity extends AppCompatActivity {
    ArrayList<PatrolItem> data;
    Button btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol);

        btn_delete = (Button) findViewById(R.id.btn_delete);
        ListView listView = (ListView) findViewById(R.id.patrol_listview);

        data = new ArrayList<PatrolItem>();

        ArrayList<String> humans = new ArrayList<>();

        File file = new File(getCacheDir().toString());

        File[] files = file.listFiles();

        for(File tempFile : files) {

            Log.d("MyTag",tempFile.getName());

            //human 이 들어가 있는 파일명을 찾습니다.
            if(tempFile.getName().contains("human")) {
                humans.add(tempFile.getName());
            }
        }

        PatrolItem log[] = new PatrolItem[humans.size()];


        for(int i=humans.size() - 1; i > -1; i--) {
            final int INDEX;
            INDEX = i;
            String path = getCacheDir() + "/" + humans.get(INDEX);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            String[] tmp = humans.get(INDEX).split(" ");
            log[i] = new PatrolItem(bitmap, tmp[0] + " " + tmp[1]);
            data.add(log[i]);
            Log.d("test", tmp[0] + " " + tmp[1]);
        }

//        Log.e("MyTag","humans size : " + humans.size());

        PatrolAdapter adapter = new PatrolAdapter(this, data);
        listView.setAdapter(adapter);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(File tempFile : files) {

                    Log.d("MyTag",tempFile.getName());

                    //human 이 들어가 있는 파일명을 찾습니다.
                    if(tempFile.getName().contains("human")) {
                        tempFile.delete();
                    }
                }
                // 새로고침
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getApplicationContext(), FindClickedActivity.class);
//                intent.putExtra("img",data.get(i).getImg());
//                intent.putExtra("date",data.get(i).getDate());
//                startActivity(intent);
//
//            }
//        });



    }

}

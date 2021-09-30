package com.example.a10jobs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class FindActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<FindItem> data = null;
    String url = getString(R.string.url);       // 서버 url 주기
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
        setContentView(R.layout.activity_find);
        socket.connect();

        ListView listView = (ListView) findViewById(R.id.find_listview);

        data = new ArrayList<>();

//        물건 입력
        FindItem wal = new FindItem(R.drawable.testimg, "지갑 찾기", "날짜 / 시간 표시");
        FindItem rc = new FindItem(R.drawable.testimg, "리모컨 찾기", "날짜 / 시간 표시");
        FindItem key = new FindItem(R.drawable.testimg, "열쇠 찾기", "날짜 / 시간 표시");
        FindItem bp = new FindItem(R.drawable.testimg, "가방 찾기", "날짜 / 시간 표시");

        data.add(wal);
        data.add(rc);
        data.add(key);
        data.add(bp);

        FindAdapter adapter = new FindAdapter(this, R.layout.find_item, data);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), FindClickedActivity.class);
                intent.putExtra("title",data.get(i).getTitle());
                intent.putExtra("img",data.get(i).getImg());
                intent.putExtra("date",data.get(i).getDate());
                startActivity(intent);
                
                // item별로 보내는 소켓이 다름
                switch(i){
                    case 0:
                        socket.emit("findWalletToServer");
                        break;
                    case 1:
                        socket.emit("findRemoteToServer");
                        break;
                    case 2:
                        socket.emit("findKeyToServer");
                        break;
                    case 3:
                        socket.emit("findBagToServer");
                        break;
                }
            }
        });



    }

    @Override
    public void onClick(View view) {

    }
}
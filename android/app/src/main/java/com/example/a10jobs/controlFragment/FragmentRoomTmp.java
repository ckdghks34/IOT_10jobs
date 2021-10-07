package com.example.a10jobs.controlFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.a10jobs.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentRoomTmp extends Fragment {
    int[] applianceStatus = new int[17];
    ImageView hallway_lightImg, kitchen_lightImg;           // 색깔 바뀌는 이미지뷰
    ImageView hallway_lightButton, kitchen_lightButton;     // 켜고 끄는 버튼 역할
    TextView hallway_light, kitchen_light;

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
    JSONObject data = new JSONObject();     // socket.emit할 때 필요한 JSON객체

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_room_tmp, container, false);
        socket.on("sendApplianceStatus", onAppliance);
        socket.connect();

        hallway_lightButton = (ImageView) v.findViewById(R.id.hallwayButton);
        kitchen_lightButton = (ImageView) v.findViewById(R.id.kitchenButton);

        hallway_light = (TextView) v.findViewById(R.id.hallwayState);
        hallway_lightImg = (ImageView) v.findViewById(R.id.hallway);

        kitchen_light = (TextView) v.findViewById(R.id.kitchenState);
        kitchen_lightImg = (ImageView) v.findViewById(R.id.kitchen);

        hallway_lightButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(applianceStatus[0] == 2){
                    Toast.makeText(getActivity(), "불을 켭니다!", Toast.LENGTH_SHORT).show();
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 0);
                        socket.emit("LightOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(applianceStatus[0] == 1){
                    Toast.makeText(getActivity(), "불을 끕니다!", Toast.LENGTH_SHORT).show();
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 0);
                        socket.emit("LightOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        kitchen_lightButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(applianceStatus[5] == 2){
                    Toast.makeText(getActivity(), "불을 켭니다!", Toast.LENGTH_SHORT).show();
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 5);
                        socket.emit("LightOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(applianceStatus[5] == 1){
                    Toast.makeText(getActivity(), "불을 끕니다!", Toast.LENGTH_SHORT).show();
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 5);
                        socket.emit("LightOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        socket.off("sendApplianceStatus");
        socket.disconnect();
        Log.v("msg", "기기제어 소켓 통신 해제");
    }

    // 리스너 -> 이벤트를 보냈을 때 이 리스너가 실행됨
    private Emitter.Listener onAppliance = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = (String) args[0];
//                   Log.v("data", data);
                        String[] tmp = data.split(",");
                        for (int i = 0; i < tmp.length; i++) {
//                        Log.v("data", tmp[i]);
                            String[] tmp2 = tmp[i].split(":");
                            String num = tmp2[1].trim();
                            num = num.replace("}", "");
                            num = num.replace("{", "");
                            applianceStatus[i] = Integer.parseInt(num);
//                            Log.v("data", i + " : " + String.valueOf(applianceStatus[i]));
                        }
                        if (applianceStatus[0] == 1) {
                            hallway_lightButton.setImageResource(R.drawable.power_on);
                            hallway_light.setText("켜짐");
                        } else if (applianceStatus[0] == 2) {
                            hallway_lightButton.setImageResource(R.drawable.power_off);
                            hallway_light.setText("꺼짐");
                            hallway_lightImg.setImageResource(R.drawable.hallway_off);
                        }

                        if (applianceStatus[5] == 1) {
                            kitchen_lightButton.setImageResource(R.drawable.power_on);
                            kitchen_light.setText("닫힘");
                            kitchen_lightImg.setImageResource(R.drawable.kitchen_on);
                        } else if (applianceStatus[5] == 2) {
                            kitchen_lightButton.setImageResource(R.drawable.power_off);
                            kitchen_light.setText("열림");
                            kitchen_lightImg.setImageResource(R.drawable.kitchen_off);
                        }
                    }
                });
            }
        }
    };
}

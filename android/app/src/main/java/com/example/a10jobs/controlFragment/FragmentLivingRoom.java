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

public class FragmentLivingRoom extends Fragment {
    ImageView airconButton, tvButton, curtainButton, aircleanerButton, lightButton;
    int[] applianceStatus = new int[17];
    String airconStatus = "";
    String tvStatus = "";
    String curtainStatus = "";
    String aircleanerStatus = "";
    String lightStatus = "";

    TextView aircon, tv, curtain, aircleaner, light;
    ImageView airconImg, tvImg, curtainImg, aircleanerImg, lightImg;


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
    JSONObject data = new JSONObject();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_living_room, container, false);

        airconButton = (ImageView) v.findViewById(R.id.airconButton);
        tvButton = (ImageView) v.findViewById(R.id.tvButton);
        curtainButton = (ImageView) v.findViewById(R.id.curtainButton);
        aircleanerButton = (ImageView) v.findViewById(R.id.aircleanerButton);
        lightButton = (ImageView) v.findViewById(R.id.lightButton);

        aircon = (TextView) v.findViewById(R.id.airconState);
        airconImg = (ImageView) v.findViewById(R.id.aircon);

        tv = (TextView) v.findViewById(R.id.tvState);
        tvImg = (ImageView) v.findViewById(R.id.tv);

        curtain = (TextView) v.findViewById(R.id.curtainState);
        curtainImg = (ImageView) v.findViewById(R.id.curtain);

        aircleaner = (TextView) v.findViewById(R.id.airpurifierState);
        aircleanerImg = (ImageView) v.findViewById(R.id.airpurifier);

        light = (TextView) v.findViewById(R.id.lightState);
        lightImg = (ImageView) v.findViewById(R.id.light);

//        if(savedInstanceState != null){   // 프래그먼트 전환할 때 데이터 유지하려고 했는데 그냥 add/hide/show로 해결
//            String txt1 = savedInstanceState.getString("data1");
//            String txt2 = savedInstanceState.getString("data2");
//            aircon.setText(txt1);
//            airconImg.setImageBitmap(StringToBitmap(txt2));
//        }
        
        socket.on("sendApplianceStatus", onAppliance);
//        socket.on("sendAirConditionerStatus", onAircon);
//        socket.on("sendAirCleanerStatus", onAirCleaner);
//        socket.on("sendTvStatus", onTv);
//        socket.on("sendLightStatus", onLight);
//        socket.on("sendCurtainStatus", onCurtain);
        socket.connect();

        airconButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(applianceStatus[10] == 2){
                    Toast.makeText(getActivity(), "에어컨을 작동시킵니다!", Toast.LENGTH_SHORT).show();
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 10);
                        socket.emit("AirConditionerOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(applianceStatus[10] == 1){
                    Toast.makeText(getActivity(), "에어컨 작동을 멈춥니다!", Toast.LENGTH_SHORT).show();
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 10);
                        socket.emit("AirConditionerOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        tvButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(applianceStatus[12] == 2){
                    Toast.makeText(getActivity(), "TV를 작동시킵니다!", Toast.LENGTH_SHORT).show();
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 12);
                        socket.emit("TvOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(applianceStatus[12] == 1){
                    Toast.makeText(getActivity(), "TV 작동을 멈춥니다!", Toast.LENGTH_SHORT).show();
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 12);
                        socket.emit("TvOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        curtainButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(applianceStatus[16] == 2){
                    Toast.makeText(getActivity(), "커튼을 닫습니다!", Toast.LENGTH_SHORT).show();
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 16);
                        socket.emit("CurtainOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(applianceStatus[16] == 1){
                    Toast.makeText(getActivity(), "커튼을 엽니다!", Toast.LENGTH_SHORT).show();
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 16);
                        socket.emit("CurtainOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        aircleanerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(applianceStatus[11] == 2){
                    Toast.makeText(getActivity(), "공기청정기를 작동시킵니다!", Toast.LENGTH_SHORT).show();
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 11);
                        socket.emit("AirCleanerOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(applianceStatus[11] == 1){
                    Toast.makeText(getActivity(), "공기청정기 작동을 멈춥니다!", Toast.LENGTH_SHORT).show();
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 11);
                        socket.emit("AirCleanerOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        lightButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(applianceStatus[6] == 2){
                    Toast.makeText(getActivity(), "불을 켭니다!", Toast.LENGTH_SHORT).show();
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 6);
                        socket.emit("LightOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(applianceStatus[6] == 1){
                    Toast.makeText(getActivity(), "불을 끕니다!", Toast.LENGTH_SHORT).show();
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 6);
                        socket.emit("LightOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

//        airconButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (!isChecked) {
//                    Toast.makeText(getActivity(), "에어컨을 작동시킵니다!", Toast.LENGTH_SHORT).show();
//                    try {
//                        data.put("ctr_cmd", 1);
//                        data.put("ctr_num", 10);
//                        socket.emit("AirConditionerOnToServer", data);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
////                    aircon.setText("켜짐");
////                    airconImg.setImageResource(R.drawable.airconon);
//                } else {
//                    Toast.makeText(getActivity(), "에어컨 작동을 멈춥니다!", Toast.LENGTH_SHORT).show();
//                    try {
//                        data.put("ctr_cmd", 2);
//                        data.put("ctr_num", 10);
//                        socket.emit("AirConditionerOffToServer", data);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
////                    aircon.setText("꺼짐");
////                    airconImg.setImageResource(R.drawable.airconoff);
//                }
//            }
//        });

        // TV 전원
//        tvButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (!isChecked) {
//                    Toast.makeText(getActivity(), "TV를 작동시킵니다!", Toast.LENGTH_SHORT).show();
//                    try {
//                        data.put("ctr_cmd", 1);
//                        data.put("ctr_num", 12);
//                        socket.emit("TvOnToServer", data);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
////                    tv.setText("켜짐");
////                    tvImg.setImageResource(R.drawable.television_on);
//                } else {
//                    Toast.makeText(getActivity(), "TV 작동을 멈춥니다!", Toast.LENGTH_SHORT).show();
//                    try {
//                        data.put("ctr_cmd", 2);
//                        data.put("ctr_num", 12);
//                        socket.emit("TvOffToServer", data);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
////                    tv.setText("꺼짐");
////                    tvImg.setImageResource(R.drawable.television_off);
//                }
//            }
//        });

        // 커튼 열림 / 닫힘
//        curtainButton = (ToggleButton) v.findViewById(R.id.curtainButton);
//        curtainButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    Toast.makeText(getActivity(), "커튼을 닫습니다!", Toast.LENGTH_SHORT).show();
//                    try {
//                        data.put("ctr_cmd", 1);
//                        data.put("ctr_num", 16);
//                        socket.emit("CurtainOnToServer", data);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
////                    curtain.setText("닫힘");
////                    curtainImg.setImageResource(R.drawable.curtain_on);
//                } else {
//                    Toast.makeText(getActivity(), "커튼을 엽니다!", Toast.LENGTH_SHORT).show();
//                    try {
//                        data.put("ctr_cmd", 2);
//                        data.put("ctr_num", 16);
//                        socket.emit("CurtainOffToServer", data);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
////                    curtain.setText("열힘");
////                    curtainImg.setImageResource(R.drawable.curtain_off);
//                }
//            }
//        });

        // 공기청정기 전원
//        airpurifierButton = (ToggleButton) v.findViewById(R.id.airpurifierButton);
//        airpurifierButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    Toast.makeText(getActivity(), "공기청정기를 작동시킵니다!", Toast.LENGTH_SHORT).show();
//                    try {
//                        data.put("ctr_cmd", 1);
//                        data.put("ctr_num", 11);
//                        socket.emit("AirCleanerOnToServer", data);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
////                    aircleaner.setText("켜짐");
////                    aircleanerImg.setImageResource(R.drawable.airpurifier_on);
//                } else {
//                    Toast.makeText(getActivity(), "공기청정기작동을 멈춥니다!", Toast.LENGTH_SHORT).show();
//                    try {
//                        data.put("ctr_cmd", 2);
//                        data.put("ctr_num", 11);
//                        socket.emit("AirCleanerOffToServer", data);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
////                    aircleaner.setText("꺼짐");
////                    aircleanerImg.setImageResource(R.drawable.airpurifier_off);
//                }
//            }
//        });

        // 불 전원
//        lightButton = (ToggleButton) v.findViewById(R.id.lightButton);
//        lightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    Toast.makeText(getActivity(), "전등을 켭니다!", Toast.LENGTH_SHORT).show();
//                    try {
//                        data.put("ctr_cmd", 1);
//                        data.put("ctr_num", 6);
//                        socket.emit("LightOnToServer", data);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
////                    light.setText("켜짐");
////                    lightImg.setImageResource(R.drawable.lamps_on);
//                } else {
//                    Toast.makeText(getActivity(), "전등을 끕니다!", Toast.LENGTH_SHORT).show();
//                    try {
//                        data.put("ctr_cmd", 2);
//                        data.put("ctr_num", 6);
//                        socket.emit("LightOffToServer", data);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
////                    light.setText("켜짐");
////                    lightImg.setImageResource(R.drawable.lamps_off);
//                }
//            }
//        });

        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        socket.off("sendApplianceStatus");
        socket.disconnect();
        Log.v("msg", "기기제어 소켓 통신 해제");
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        socket.off("sendApplianceStatus");
//        socket.disconnect();
//        Log.v("msg", "기기제어 소켓 통신 해제");
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        TextView textCounter = getView().findViewById(R.id.airconState);
        ImageView image = getView().findViewById(R.id.aircon);
        String data1 = textCounter.getText().toString();
        String data2 = image.toString();
//        String data2 = image.get
//        int counter = Integer.parseInt(textCounter.getText().toString());
        outState.putString("data1", data1);
        outState.putString("data2", data2);
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
//                            Log.v("기기상태", i + " : " + String.valueOf(applianceStatus[i]));
                        }
                        if (applianceStatus[10] == 1) {
                            airconButton.setImageResource(R.drawable.power_on);
                            aircon.setText("켜짐");
                            airconImg.setImageResource(R.drawable.airconon);
                        } else if (applianceStatus[10] == 2) {
                            airconButton.setImageResource(R.drawable.power_off);
                            aircon.setText("꺼짐");
                            airconImg.setImageResource(R.drawable.airconoff);
                        }

                        if (applianceStatus[12] == 1) {
                            tvButton.setImageResource(R.drawable.power_on);
                            tv.setText("켜짐");
                            tvImg.setImageResource(R.drawable.television_on);
                        } else if (applianceStatus[12] == 2) {
                            tvButton.setImageResource(R.drawable.power_off);
                            tv.setText("꺼짐");
                            tvImg.setImageResource(R.drawable.television_off);
                        }

                        if (applianceStatus[11] == 1) {
                            tvButton.setImageResource(R.drawable.power_on);
                            aircleaner.setText("켜짐");
                            aircleanerImg.setImageResource(R.drawable.airpurifier_on);
                        } else if (applianceStatus[11] == 2) {
                            aircleaner.setText("꺼짐");
                            aircleanerImg.setImageResource(R.drawable.airpurifier_off);
                            tvButton.setImageResource(R.drawable.power_off);
                        }

                        if (applianceStatus[6] == 1) {
                            lightButton.setImageResource(R.drawable.power_on);
                            light.setText("켜짐");
                            lightImg.setImageResource(R.drawable.lamps_on);
                        } else if (applianceStatus[6] == 2) {
                            light.setText("꺼짐");
                            lightImg.setImageResource(R.drawable.lamps_off);
                            lightButton.setImageResource(R.drawable.power_off);
                        }

                        if (applianceStatus[16] == 1) {
                            curtainButton.setImageResource(R.drawable.power_on);
                            curtain.setText("닫힘");
                            curtainImg.setImageResource(R.drawable.curtain_on);
                        } else if (applianceStatus[16] == 2) {
                            curtain.setText("열림");
                            curtainImg.setImageResource(R.drawable.curtain_off);
                            curtainButton.setImageResource(R.drawable.power_off);
                        }
                    }
                });
            }
        }
    };

//    private Emitter.Listener onAircon = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    airconStatus = (String)args[0];
//                    Log.v("aircon", airconStatus);
//                }
//            });
//        }
//    };
//
//    private Emitter.Listener onAirCleaner = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    aircleanerStatus = (String)args[0];
//                    Log.v("aircleaner", aircleanerStatus);
//                }
//            });
//        }
//    };
//    private Emitter.Listener onTv = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    tvStatus = (String)args[0];
//                    Log.v("tv", tvStatus);
//                }
//            });
//        }
//    };
//    private Emitter.Listener onLight = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    lightStatus = (String)args[0];
//                    Log.v("light", lightStatus);
//                }
//            });
//        }
//    };
//    private Emitter.Listener onCurtain = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    curtainStatus = (String)args[0];
//                    Log.v("curtain", curtainStatus);
//                }
//            });
//        }
//    };
//
//    public static Bitmap StringToBitmap(String encodedString){
//        try {
//            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//            return bitmap;
//        } catch (Exception e) {
//            e.getMessage();
//            return null;
//        }
//    };
}


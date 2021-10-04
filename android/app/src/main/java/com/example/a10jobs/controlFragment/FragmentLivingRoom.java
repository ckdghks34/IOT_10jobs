package com.example.a10jobs.controlFragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.example.a10jobs.R;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentLivingRoom extends Fragment {
    ToggleButton airconButton;
    ToggleButton tvButton;
    ToggleButton curtainButton;
    ToggleButton airpurifierButton;
    ToggleButton lightButton;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_living_room, container, false);
        aircon = (TextView) v.findViewById(R.id.airconState);
        airconImg = (ImageView) v.findViewById(R.id.aircon);

        if(savedInstanceState != null){
            String txt1 = savedInstanceState.getString("data1");
            String txt2 = savedInstanceState.getString("data2");
            aircon.setText(txt1);
            airconImg.setImageBitmap(StringToBitmap(txt2));
        }
        socket.on("AirConditionerStatus", onAircon);
        socket.on("AirCleanerStatus", onAirCleaner);
        socket.on("TvStatus", onTv);
        socket.on("LightStatus", onLight);
        socket.on("CurtainStatus", onCurtain);
        socket.connect();

        if(airconStatus == "On") {
            aircon.setText("켜짐");
            airconImg.setImageResource(R.drawable.airconon);
        } else if(airconStatus == "Off") {
            aircon.setText("꺼짐");
            airconImg.setImageResource(R.drawable.airconoff);
        }

        if(tvStatus == "On") {
            tv.setText("켜짐");
            tvImg.setImageResource(R.drawable.television_on);
        } else if(tvStatus == "Off") {
            tv.setText("꺼짐");
            tvImg.setImageResource(R.drawable.television_off);
        }

        if(aircleanerStatus == "On") {
            aircleaner.setText("켜짐");
            aircleanerImg.setImageResource(R.drawable.airpurifier_on);
        } else if(airconStatus == "Off") {
            aircleaner.setText("꺼짐");
            aircleanerImg.setImageResource(R.drawable.airpurifier_off);
        }

        if(lightStatus == "On") {
            light.setText("켜짐");
            lightImg.setImageResource(R.drawable.lamps_on);
        } else if(lightStatus == "Off") {
            light.setText("꺼짐");
            lightImg.setImageResource(R.drawable.lamps_on);
        }

        if(curtainStatus == "On") {
            curtain.setText("닫힘");
            curtainImg.setImageResource(R.drawable.curtain_on);
        } else if(curtainStatus == "Off") {
            curtain.setText("열림");
            curtainImg.setImageResource(R.drawable.curtain_off);
        }

        // 에어컨 전원
        airconButton = (ToggleButton) v.findViewById(R.id.airconButton);
        airconButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getActivity(), "에어컨을 작동시킵니다!", Toast.LENGTH_SHORT).show();
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 10);
                        socket.emit("AirConditionerOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    aircon.setText("켜짐");
//                    airconImg.setImageResource(R.drawable.airconon);
                } else {
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 10);
                        socket.emit("AirConditionerOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    aircon.setText("꺼짐");
//                    airconImg.setImageResource(R.drawable.airconoff);
                }
            }
        });
        // TV 전원
        tvButton = (ToggleButton) v.findViewById(R.id.tvButton);
        tvButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 12);
                        socket.emit("TvOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    aircon.setText("켜짐");
//                    airconImg.setImageResource(R.drawable.television_on);
                } else {
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 12);
                        socket.emit("TvOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    aircon.setText("꺼짐");
//                    airconImg.setImageResource(R.drawable.television_off);
                }
            }
        });

        // 커튼 열림 / 닫힘
        curtainButton = (ToggleButton) v.findViewById(R.id.curtainButton);
        curtainButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 16);
                        socket.emit("CurtainOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    aircon.setText("열림");
//                    airconImg.setImageResource(R.drawable.curtain_on);
                } else {
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 16);
                        socket.emit("CurtainOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    aircon.setText("닫힘");
//                    airconImg.setImageResource(R.drawable.curtain_off);
                }
            }
        });

        // 공기청정기 전원
        airpurifierButton = (ToggleButton) v.findViewById(R.id.airpurifierButton);
        airpurifierButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 11);
                        socket.emit("AirCleanerOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    aircon.setText("켜짐");
//                    airconImg.setImageResource(R.drawable.airpurifier_on);
                } else {
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 16);
                        socket.emit("AirCleanerOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    aircon.setText("꺼짐");
//                    airconImg.setImageResource(R.drawable.airpurifier_off);
                }
            }
        });

        // 불 전원
        lightButton = (ToggleButton) v.findViewById(R.id.lightButton);
        lightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 6);
                        socket.emit("LightOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    aircon.setText("켜짐");
//                    airconImg.setImageResource(R.drawable.lamps_on);
                } else {
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 6);
                        socket.emit("LightOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    aircon.setText("꺼짐");
//                    airconImg.setImageResource(R.drawable.lamps_off);
                }
            }
        });

        return v;
    }

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
    private Emitter.Listener onAircon = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    airconStatus = (String)args[0];
                    Log.v("aircon", airconStatus);
                }
            });
        }
    };

    private Emitter.Listener onAirCleaner = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    aircleanerStatus = (String)args[0];
                    Log.v("aircleaner", aircleanerStatus);
                }
            });
        }
    };
    private Emitter.Listener onTv = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvStatus = (String)args[0];
                    Log.v("tv", tvStatus);
                }
            });
        }
    };
    private Emitter.Listener onLight = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lightStatus = (String)args[0];
                    Log.v("light", lightStatus);
                }
            });
        }
    };
    private Emitter.Listener onCurtain = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    curtainStatus = (String)args[0];
                    Log.v("curtain", curtainStatus);
                }
            });
        }
    };

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

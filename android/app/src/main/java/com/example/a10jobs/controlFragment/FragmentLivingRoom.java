package com.example.a10jobs.controlFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
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
        socket.on("AirConditionerStatus", onAircon);
        socket.on("AirCleanerStatus", onAirCleaner);
        socket.on("TvStatus", onTv);
        socket.on("LightStatus", onLight);
        socket.on("CurtainStatus", onCurtain);
        socket.connect();

        // 에어컨 전원
        airconButton = (ToggleButton) v.findViewById(R.id.airconButton);
        airconButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView aircon = (TextView) v.findViewById(R.id.airconState);
                ImageView airconImg = (ImageView) v.findViewById(R.id.aircon);
                if (isChecked) {
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 10);
                        socket.emit("AirConditionerOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    aircon.setText("켜짐");
                    airconImg.setImageResource(R.drawable.airconon);
                } else {
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 10);
                        socket.emit("AirConditionerOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    aircon.setText("꺼짐");
                    airconImg.setImageResource(R.drawable.airconoff);
                }
            }
        });
        // TV 전원
        tvButton = (ToggleButton) v.findViewById(R.id.tvButton);
        tvButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView aircon = (TextView) v.findViewById(R.id.tvState);
                ImageView airconImg = (ImageView) v.findViewById(R.id.tv);
                if (isChecked) {
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 12);
                        socket.emit("TvOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    aircon.setText("켜짐");
                    airconImg.setImageResource(R.drawable.television_on);
                } else {
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 12);
                        socket.emit("TvOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    aircon.setText("꺼짐");
                    airconImg.setImageResource(R.drawable.television_off);
                }
            }
        });

        // 커튼 열림 / 닫힘
        curtainButton = (ToggleButton) v.findViewById(R.id.curtainButton);
        curtainButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView aircon = (TextView) v.findViewById(R.id.curtainState);
                ImageView airconImg = (ImageView) v.findViewById(R.id.curtain);
                if (isChecked) {
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 16);
                        socket.emit("CurtainOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    aircon.setText("열림");
                    airconImg.setImageResource(R.drawable.curtain_on);
                } else {
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 16);
                        socket.emit("CurtainOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    aircon.setText("닫힘");
                    airconImg.setImageResource(R.drawable.curtain_off);
                }
            }
        });

        // 공기청정기 전원
        airpurifierButton = (ToggleButton) v.findViewById(R.id.airpurifierButton);
        airpurifierButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView aircon = (TextView) v.findViewById(R.id.airpurifierState);
                ImageView airconImg = (ImageView) v.findViewById(R.id.airpurifier);
                if (isChecked) {
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 11);
                        socket.emit("AirCleanerOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    aircon.setText("켜짐");
                    airconImg.setImageResource(R.drawable.airpurifier_on);
                } else {
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 16);
                        socket.emit("AirCleanerOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    aircon.setText("꺼짐");
                    airconImg.setImageResource(R.drawable.airpurifier_off);
                }
            }
        });

        // 불 전원
        lightButton = (ToggleButton) v.findViewById(R.id.lightButton);
        lightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView aircon = (TextView) v.findViewById(R.id.lightState);
                ImageView airconImg = (ImageView) v.findViewById(R.id.light);
                if (isChecked) {
                    try {
                        data.put("ctr_cmd", 1);
                        data.put("ctr_num", 6);
                        socket.emit("LightOnToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    aircon.setText("켜짐");
                    airconImg.setImageResource(R.drawable.lamps_on);
                } else {
                    try {
                        data.put("ctr_cmd", 2);
                        data.put("ctr_num", 6);
                        socket.emit("LightOffToServer", data);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    aircon.setText("꺼짐");
                    airconImg.setImageResource(R.drawable.lamps_off);
                }
            }
        });

        return v;
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
}

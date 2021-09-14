package com.example.a10jobs.controlFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.example.a10jobs.R;

public class FragmentLivingRoom extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_living_room, container, false);

        // 에어컨 전원
        ToggleButton airconToggle = (ToggleButton) v.findViewById(R.id.airconButton);
        airconToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView aircon = (TextView) v.findViewById(R.id.airconState);
                ImageView airconImg = (ImageView) v.findViewById(R.id.aircon);
                if (isChecked) {
                    aircon.setText("켜짐");
                    airconImg.setImageResource(R.drawable.airconon);
                } else {
                    aircon.setText("꺼짐");
                    airconImg.setImageResource(R.drawable.airconoff);
                }
            }
        });
        // TV 전원
        ToggleButton tvToggle = (ToggleButton) v.findViewById(R.id.tvButton);
        tvToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView aircon = (TextView) v.findViewById(R.id.tvState);
                ImageView airconImg = (ImageView) v.findViewById(R.id.tv);
                if (isChecked) {
                    aircon.setText("켜짐");
                    airconImg.setImageResource(R.drawable.tv);
                } else {
                    aircon.setText("꺼짐");
                    airconImg.setImageResource(R.drawable.tv);
                }
            }
        });

        // 커튼 열림 / 닫힘
        ToggleButton curtainToggle = (ToggleButton) v.findViewById(R.id.curtainButton);
        curtainToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView aircon = (TextView) v.findViewById(R.id.curtainState);
                ImageView airconImg = (ImageView) v.findViewById(R.id.curtain);
                if (isChecked) {
                    aircon.setText("열림");
                    airconImg.setImageResource(R.drawable.curtain);
                } else {
                    aircon.setText("닫힘");
                    airconImg.setImageResource(R.drawable.curtain);
                }
            }
        });

        // 공기청정기 전원
        ToggleButton airconditionerToggle = (ToggleButton) v.findViewById(R.id.airpurifierButton);
        airconditionerToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView aircon = (TextView) v.findViewById(R.id.airpurifierState);
                ImageView airconImg = (ImageView) v.findViewById(R.id.airpurifier);
                if (isChecked) {
                    aircon.setText("켜짐");
                    airconImg.setImageResource(R.drawable.airpurifier);
                } else {
                    aircon.setText("꺼짐");
                    airconImg.setImageResource(R.drawable.airpurifier);
                }
            }
        });

        // 불 전원
        ToggleButton lightToggle = (ToggleButton) v.findViewById(R.id.lightButton);
        lightToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView aircon = (TextView) v.findViewById(R.id.lightState);
                ImageView airconImg = (ImageView) v.findViewById(R.id.light);
                if (isChecked) {
                    aircon.setText("켜짐");
                    airconImg.setImageResource(R.drawable.light);
                } else {
                    aircon.setText("꺼짐");
                    airconImg.setImageResource(R.drawable.light);
                }
            }
        });


        return v;



    }

}

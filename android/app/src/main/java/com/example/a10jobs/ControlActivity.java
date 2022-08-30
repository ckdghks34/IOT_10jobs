package com.example.a10jobs;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.a10jobs.controlFragment.FragmentLivingRoom;
import com.example.a10jobs.controlFragment.FragmentRoom1;
import com.example.a10jobs.controlFragment.FragmentRoom2;
import com.example.a10jobs.controlFragment.FragmentRoom3;
import com.example.a10jobs.controlFragment.FragmentRoom4;
import com.example.a10jobs.controlFragment.FragmentRoomTmp;

public class ControlActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentLivingRoom fragmentLivingRoom;
    private FragmentRoom1 fragmentRoom1;
    private FragmentRoom2 fragmentRoom2;
    private FragmentRoom3 fragmentRoom3;
    private FragmentRoom4 fragmentRoom4;
    private FragmentRoomTmp fragmentRoomTmp;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_room);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.room, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        fragmentManager = getSupportFragmentManager();

        fragmentLivingRoom = new FragmentLivingRoom();
        fragmentRoom1 = new FragmentRoom1();
        fragmentRoom2 = new FragmentRoom2();
        fragmentRoom3 = new FragmentRoom3();
        fragmentRoom4 = new FragmentRoom4();
        fragmentRoomTmp = new FragmentRoomTmp();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                transaction = fragmentManager.beginTransaction();

                if (i==0) {
                    transaction.remove(fragmentRoom1);
                    transaction.remove(fragmentRoom2);
                    transaction.remove(fragmentRoom3);
                    transaction.remove(fragmentRoom4);
                    transaction.remove(fragmentRoomTmp);
                    transaction.add(R.id.controlLayout, fragmentLivingRoom);

                    transaction.show(fragmentLivingRoom);
                    transaction.hide(fragmentRoom1);
                    transaction.hide(fragmentRoom2);
                    transaction.hide(fragmentRoom3);
                    transaction.hide(fragmentRoom4);
                    transaction.hide(fragmentRoomTmp);
                }
                else if (i==1) {
                    transaction.remove(fragmentLivingRoom);
                    transaction.remove(fragmentRoom2);
                    transaction.remove(fragmentRoom3);
                    transaction.remove(fragmentRoom4);
                    transaction.remove(fragmentRoomTmp);
                    transaction.add(R.id.controlLayout, fragmentRoom1);

                    transaction.hide(fragmentLivingRoom);
                    transaction.show(fragmentRoom1);
                    transaction.hide(fragmentRoom2);
                    transaction.hide(fragmentRoom3);
                    transaction.hide(fragmentRoom4);
                    transaction.hide(fragmentRoomTmp);
                }
                else if (i==2) {
                    transaction.remove(fragmentRoom1);
                    transaction.remove(fragmentLivingRoom);
                    transaction.remove(fragmentRoom3);
                    transaction.remove(fragmentRoom4);
                    transaction.remove(fragmentRoomTmp);
                    transaction.add(R.id.controlLayout, fragmentRoom2);

                    transaction.hide(fragmentLivingRoom);
                    transaction.hide(fragmentRoom1);
                    transaction.show(fragmentRoom2);
                    transaction.hide(fragmentRoom3);
                    transaction.hide(fragmentRoom4);
                    transaction.hide(fragmentRoomTmp);
                }
                else if(i == 3){
                    transaction.remove(fragmentRoom1);
                    transaction.remove(fragmentRoom2);
                    transaction.remove(fragmentLivingRoom);
                    transaction.remove(fragmentRoom4);
                    transaction.remove(fragmentRoomTmp);
                    transaction.add(R.id.controlLayout, fragmentRoom3);

                    transaction.hide(fragmentLivingRoom);
                    transaction.hide(fragmentRoom1);
                    transaction.hide(fragmentRoom2);
                    transaction.show(fragmentRoom3);
                    transaction.hide(fragmentRoom4);
                    transaction.hide(fragmentRoomTmp);

                }
                else if(i == 4){
                    transaction.remove(fragmentRoom1);
                    transaction.remove(fragmentRoom2);
                    transaction.remove(fragmentLivingRoom);
                    transaction.remove(fragmentRoom3);
                    transaction.remove(fragmentRoomTmp);
                    transaction.add(R.id.controlLayout, fragmentRoom4);

                    transaction.hide(fragmentLivingRoom);
                    transaction.hide(fragmentRoom1);
                    transaction.hide(fragmentRoom2);
                    transaction.hide(fragmentRoom3);
                    transaction.show(fragmentRoom4);
                    transaction.hide(fragmentRoomTmp);
                }
                else if(i == 5){
                    transaction.remove(fragmentRoom1);
                    transaction.remove(fragmentRoom2);
                    transaction.remove(fragmentLivingRoom);
                    transaction.remove(fragmentRoom3);
                    transaction.remove(fragmentRoom4);
                    transaction.add(R.id.controlLayout, fragmentRoomTmp);

                    transaction.hide(fragmentLivingRoom);
                    transaction.hide(fragmentRoom1);
                    transaction.hide(fragmentRoom2);
                    transaction.hide(fragmentRoom3);
                    transaction.hide(fragmentRoom4);
                    transaction.show(fragmentRoomTmp);
                }

//                transaction.addToBackStack(null);
                transaction.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                transaction.remove(fragmentRoom1);
                transaction.remove(fragmentRoom2);
                transaction.remove(fragmentRoom3);
                transaction.remove(fragmentRoom4);
                transaction.remove(fragmentRoomTmp);
                transaction.add(R.id.controlLayout, fragmentLivingRoom);

                transaction.show(fragmentLivingRoom);
                transaction.hide(fragmentRoom1);
                transaction.hide(fragmentRoom2);
                transaction.hide(fragmentRoom3);
//                transaction.replace(R.id.controlLayout, fragmentLivingRoom).commitAllowingStateLoss();

                transaction.commit();
            }

        });
    }
}



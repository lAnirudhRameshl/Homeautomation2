package com.example.android.homeautomationfinal;

import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HallActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String TAG;
    private Switch light;
    private Switch fan;
    private SeekBar fanSpeed;
    private Chronometer timerLight, timerFan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MESSAGE","Starting");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hall_activity);
        TAG = "Message";

        mDatabase = FirebaseDatabase.getInstance().getReference();

        light = (Switch) findViewById(R.id.hall_light);
        fan = (Switch) findViewById(R.id.hall_fan);
        fanSpeed = (SeekBar) findViewById(R.id.hall_fan_speed);
        timerLight = new Chronometer(this);
        timerFan = new Chronometer(this);

        //fanSpeed.setEnabled(false);

        /*mDatabase.child("HALL").child("LIGHT").setValue(false);
        mDatabase.child("HALL").child("FAN").setValue(false);
        mDatabase.child("HALL").child("SPEED").setValue(0);*/

        /*light.setChecked(true);
        fan.setChecked(true);
        fanSpeed.setProgress(0);*/

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if((boolean)dataSnapshot.child("HALL").child("LIGHT").getValue() == true){
                    timerLight.setBase((long)dataSnapshot.child("HALL").child("EXIT TIME LIGHT").getValue());
                }

                if ((boolean)dataSnapshot.child("HALL").child("FAN").getValue() == true){
                    timerFan.setBase((long)dataSnapshot.child("HALL").child("EXIT TIME FAN").getValue());
                }

                light.setChecked((boolean)dataSnapshot.child("HALL").child("LIGHT").getValue());
                fan.setChecked((boolean)dataSnapshot.child("HALL").child("FAN").getValue());
                fanSpeed.setEnabled((boolean)dataSnapshot.child("HALL").child("FAN").getValue());
                long speed = (long)dataSnapshot.child("HALL").child("SPEED").getValue();
                fanSpeed.setProgress((int)speed);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    Log.d(TAG,"Hall light is off, turning it on");
                    mDatabase.child("HALL").child("LIGHT").setValue(true);
                    timerLight.setBase(System.currentTimeMillis());
                    timerLight.start();
                }

                else{
                    Log.d(TAG,"Hall light is on, turning it off");
                    mDatabase.child("HALL").child("LIGHT").setValue(false);
                    timerLight.stop();
                    long timeOn = System.currentTimeMillis() - timerLight.getBase();
                    Log.d(TAG,"The timer was on for approx " + timeOn/1000 + "seconds");
                    String toastMessage= "Light was on for " + timeOn/1000 + " seconds";
                    Toast toast = Toast.makeText(getApplicationContext(),toastMessage,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        fan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    fanSpeed.setEnabled(true);
                    Log.d(TAG,"Hall fan is off, turning it on");
                    mDatabase.child("HALL").child("FAN").setValue(true);
                    timerFan.setBase(System.currentTimeMillis());
                    timerFan.start();
                }

                else{
                    fanSpeed.setEnabled(false);
                    Log.d(TAG,"Hall fan is on, turning it off");
                    mDatabase.child("HALL").child("FAN").setValue(false);
                    timerFan.stop();
                    long timeOn = System.currentTimeMillis() - timerFan.getBase();
                    Log.d(TAG,"Fan was on for approx " + timeOn/1000 + " seconds");
                    String toastMessage = "The fan was on for " + timeOn/1000 + " seconds";
                    Toast toast = Toast.makeText(getApplicationContext(),toastMessage,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        fanSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mDatabase.child("HALL").child("SPEED").setValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onStop() {
        mDatabase.child("HALL").child("EXIT TIME FAN").setValue(timerFan.getBase());
        mDatabase.child("HALL").child("EXIT TIME LIGHT").setValue(timerLight.getBase());
        super.onStop();
    }
}

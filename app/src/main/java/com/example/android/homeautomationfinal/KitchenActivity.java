package com.example.android.homeautomationfinal;

import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class KitchenActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String TAG;
    private Switch light;
    private Switch fan;
    private SeekBar fanSpeed;
    private Chronometer timerLight, timerFan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kitchen_activity);
        TAG = "Message";

        mDatabase = FirebaseDatabase.getInstance().getReference();

        light = (Switch) findViewById(R.id.kitchen_light);
        fan = (Switch) findViewById(R.id.kitchen_exhaust_fan);
        fanSpeed = (SeekBar) findViewById(R.id.kitchen_exhaust_fan_speed);
        timerLight = new Chronometer(this);
        timerFan = new Chronometer(this);

        fanSpeed.setEnabled(false);

        mDatabase.child("KITCHEN").child("LIGHT").setValue(false);
        mDatabase.child("KITCHEN").child("FAN").setValue(false);
        mDatabase.child("KITCHEN").child("SPEED").setValue(0);

        light.setChecked(false);
        fan.setChecked(false);
        fanSpeed.setProgress(0);

        light.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    Log.d(TAG,"Kitchen light is off, turning it on");
                    mDatabase.child("KITCHEN").child("LIGHT").setValue(true);
                    timerLight.setBase(System.currentTimeMillis());
                    timerLight.start();
                }

                else{
                    Log.d(TAG,"Kitchen light is on, turning it off");
                    mDatabase.child("KITCHEN").child("LIGHT").setValue(false);
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
                    Log.d(TAG,"Kitchen fan is off, turning it on");
                    mDatabase.child("KITCHEN").child("FAN").setValue(true);
                    timerFan.setBase(System.currentTimeMillis());
                    timerFan.start();
                }

                else{
                    fanSpeed.setEnabled(false);
                    Log.d(TAG,"Kitchen is on, turning it off");
                    mDatabase.child("KITCHEN").child("FAN").setValue(false);
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
                mDatabase.child("KITCHEN").child("SPEED").setValue(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}

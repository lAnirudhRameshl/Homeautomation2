package com.example.android.homeautomationfinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView hall = (TextView) findViewById(R.id.hall);
        TextView masterBedroom = (TextView) findViewById(R.id.master_bedroom);
        TextView smallBedroom = (TextView) findViewById(R.id.small_bedroom);
        TextView kitchen = (TextView) findViewById(R.id.kitchen);

        hall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,HallActivity.class);
                startActivity(intent);
            }
        });

        masterBedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MasterBedroomActivity.class);
                startActivity(intent);
            }
        });

        smallBedroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SmallBedroomActivity.class);
                startActivity(intent);
            }
        });

        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,KitchenActivity.class);
                startActivity(intent);
            }
        });
    }
}

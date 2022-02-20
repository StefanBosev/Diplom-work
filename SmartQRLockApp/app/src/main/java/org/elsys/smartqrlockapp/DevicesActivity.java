package org.elsys.smartqrlockapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DevicesActivity extends AppCompatActivity {

    // Main body of the activity
    CoordinatorLayout activityBody;
    // List of devices
    LinearLayout devicesList;

    // Layout specifics for each device card
    private static LinearLayout.LayoutParams devicesLayout = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    // List of all devices
    private static List<String> devices = new ArrayList<>();

    public static void addDevice(String device) {
        DevicesActivity.devices.add(device);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);

        // Adding more specifics to the layout of the devices
        devicesLayout.setMargins(25, 5, 25, 10);
        devicesLayout.gravity = Gravity.CENTER_HORIZONTAL;

        // Setting a value and background color for main body of the activity
        activityBody = (CoordinatorLayout) findViewById(R.id.devicesActivityBody);
        activityBody.setBackgroundColor(Color.WHITE);

        // Setting a value for the view list of the device cards
        devicesList = (LinearLayout) findViewById(R.id.devicesList);

        // Visualising all available devices on every initialization of activity
        for (String device : DevicesActivity.devices) {
            System.out.println(device);
            addNewDeviceCard(device);
        }

        FloatingActionButton fab = findViewById(R.id.addNewDevice);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchNewDeviceActivity(view);
            }
        });
    }


    public void launchNewDeviceActivity(View v) {
        Intent i = new Intent(this, AddNewDeviceActivity.class);
        startActivity(i);
    }

    public void addNewDeviceCard(String deviceName) {
        CardView newDevice = new CardView(getApplicationContext());

        newDevice.setLayoutParams(DevicesActivity.devicesLayout);
        newDevice.setPadding(25, 25, 25, 25);
        newDevice.setCardBackgroundColor(0xFF84C984);
        newDevice.setMaxCardElevation(60);
        newDevice.setRadius(25);
        newDevice.setMinimumHeight(60);
        newDevice.setMinimumWidth(activityBody.getWidth() - 50);
        newDevice.setClickable(true);
        newDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Open details about the device", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TextView text = new TextView(getApplicationContext());
        text.setLayoutParams(DevicesActivity.devicesLayout);
        text.setText(deviceName);
        text.setTextColor(Color.WHITE);
        text.setPadding(25, 25, 25, 25);
        text.setGravity(Gravity.CENTER);

        newDevice.addView(text);

        devicesList.addView(newDevice);
    }
}
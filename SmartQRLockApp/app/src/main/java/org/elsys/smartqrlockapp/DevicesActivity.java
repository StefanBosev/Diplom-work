package org.elsys.smartqrlockapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
        File directory = new File(this.getFilesDir() + File.separator + "demo-json-dir");
        File[] allDevices = directory.listFiles();

//        if (allDevices != null) {
//            for (File file : allDevices) {
//                file.delete();
//            }
//        }

        JSONObject cardDetails = null;

        for (File device : allDevices) {
            StringBuilder data = new StringBuilder();

            try {
                BufferedReader bufReader = new BufferedReader(new FileReader(device));
                String line;

                while ((line = bufReader.readLine()) != null) {
                    data.append(line);
                    data.append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                cardDetails = new JSONObject(data.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (cardDetails != null) {
                visualiseDeviceCard(cardDetails);
            }

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



    public void visualiseDeviceCard(JSONObject cardInfo) {
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
                Intent editDeviceIntent = new Intent(v.getContext(), EditDeviceActivity.class);
                Bundle deviceData = new Bundle();

                deviceData.putString("name", cardInfo.toString());

                editDeviceIntent.putExtras(deviceData);
                startActivity(editDeviceIntent);
            }
        });

        TextView text = new TextView(getApplicationContext());
        text.setLayoutParams(DevicesActivity.devicesLayout);

        String deviceName = "";
        try {
            deviceName = cardInfo.getString("name");
        } catch (JSONException e) {
            deviceName = "Error extracting name from json";
            e.printStackTrace();
        }

        text.setText(deviceName);
        text.setTextColor(Color.WHITE);
        text.setPadding(25, 25, 25, 25);
        text.setGravity(Gravity.CENTER);

        newDevice.addView(text);

        devicesList.addView(newDevice);
    }
}
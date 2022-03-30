package org.elsys.smartqrlockapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import org.elsys.smartqrlockapp.factories.MainCardFactory;
import org.elsys.smartqrlockapp.values.Colors;
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
        activityBody.setBackgroundColor(Colors.APP_BACKGROUND);

        // Setting a value for the view list of the device cards
        devicesList = (LinearLayout) findViewById(R.id.devicesList);

        // Visualising all available devices on every initialization of activity
        File directory = new File(this.getFilesDir() + File.separator + "demo-json-dir");
        File[] allDevices = directory.listFiles();


//        Just for easier file management. DELETE BEFORE UPLOADING TO PROD
//        if (allDevices != null) {
//            for (File file : allDevices) {
//                file.delete();
//            }
//        }

        JSONObject cardDetails = null;

        for (File device : allDevices) {
            cardDetails = readFile(device);

            if (cardDetails != null) {
                visualiseDeviceCard(cardDetails, device.getPath());
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

    public void visualiseDeviceCard(JSONObject cardInfo, String filePath) {
        MainCardFactory mainCardFactory = MainCardFactory.getInstance();

        CardView newDevice = mainCardFactory.getCard(cardInfo, getApplicationContext(), activityBody);

        newDevice.setLayoutParams(DevicesActivity.devicesLayout);
        newDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editDeviceIntent = new Intent(v.getContext(), EditDeviceActivity.class);
                Bundle deviceData = new Bundle();

                deviceData.putString("data", cardInfo.toString());
                deviceData.putString("file-path", filePath);

                editDeviceIntent.putExtras(deviceData);
                startActivity(editDeviceIntent);
            }
        });

        newDevice.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DevicesActivity.this);

                builder.setCancelable(true);
                builder.setTitle("Delete file?");
                builder.setMessage("Are you sure you want to delete this file?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(filePath);
                        file.delete();
                        devicesList.removeView(newDevice);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();


                return true;
            }
        });

        devicesList.addView(newDevice);
    }

    private JSONObject readFile(File device) {
        StringBuilder data = new StringBuilder();
        JSONObject cardDetails = null;

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

        return cardDetails;
    }
}
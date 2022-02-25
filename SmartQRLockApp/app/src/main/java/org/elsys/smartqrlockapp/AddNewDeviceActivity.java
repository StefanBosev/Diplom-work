package org.elsys.smartqrlockapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.Buffer;

public class AddNewDeviceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_device);
        Button but = (Button) findViewById(R.id.button);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDevicesList();
                returnToMainPage();
            }
        });
    }

    public void returnToMainPage() {

        Intent intent = new Intent(this, DevicesActivity.class);

        startActivity(intent);
    }

    public void addToDevicesList() {
        EditText deviceName = (EditText) findViewById(R.id.setDeviceName);

        JSONObject deviceData = new JSONObject();

        JSONObject accessList = new JSONObject();

        try {
            accessList.put("gosho", "asdfg");
            accessList.put("misho", "qwertyui");
            accessList.put("stoyo", "1234mn56m7,8.,m21");

            deviceData.put("name", deviceName.getText().toString());
            deviceData.put("place", "kitchen");
            deviceData.put("access-list", accessList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        File directory = new File(this.getFilesDir() + File.separator + "demo-json-dir");

        if (!directory.exists()) {
            directory.mkdir();
        }

        File newDevice = new File(directory, deviceName.getText().toString().toLowerCase().replaceAll(" ", "-") + ".json");
        Log.d("DEBUG", newDevice.getPath());

        boolean res = true;
        if (!newDevice.exists()) {
            try {
                res = newDevice.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(newDevice);
            OutputStreamWriter outStreamWriter = new OutputStreamWriter(fos);

            outStreamWriter.write(deviceData.toString());
            outStreamWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
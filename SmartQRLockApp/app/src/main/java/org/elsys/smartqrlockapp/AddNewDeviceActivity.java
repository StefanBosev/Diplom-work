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

import org.elsys.smartqrlockapp.factories.FileManager;
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

    private final String devicesDir = "demo-json-dir";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_device);
        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToDevicesList();
                returnToMainPage();
            }
        });

        Button addAccess = (Button) findViewById(R.id.addAccessEntry);
        addAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewAccessEntry();
            }
        });
    }

    public void returnToMainPage() {

        Intent intent = new Intent(this, DevicesActivity.class);

        startActivity(intent);
    }

    private void addToDevicesList() {
        EditText deviceName = (EditText) findViewById(R.id.setDeviceName);
        EditText devicePlacement = (EditText) findViewById(R.id.setDevicePlacement);

        JSONObject deviceData = new JSONObject();
        JSONObject accessList = new JSONObject();

        try {

            accessList.put("gosho", "asdfg");
            accessList.put("misho", "qwertyui");
            accessList.put("stoyo", "1234mn56m7,8.,m21");

            deviceData.put("name", deviceName.getText().toString());
            deviceData.put("place", devicePlacement.getText().toString());
            deviceData.put("access-list", accessList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        File directory = new File(this.getFilesDir() + File.separator + devicesDir);

        if (!directory.exists()) {
            directory.mkdir();
        }

        StringBuilder filename = new StringBuilder();
        filename.append(deviceName.getText().toString().toLowerCase().replaceAll(" ", "-"));

        FileManager fileManager = FileManager.getInstance();

        fileManager.createDeviceFile(directory, filename,  deviceData);
    }

    private void addNewAccessEntry() {
        EditText newName = new EditText(getApplicationContext());
        EditText newPass = new EditText(getApplicationContext());

        newName.setHint("Device name");
        newPass.setHint("Password");

        LinearLayout newEntry = new LinearLayout(getApplicationContext());

        newEntry.addView(newName);
        newEntry.addView(newPass);

        LinearLayout list = findViewById(R.id.addToListView);
        list.addView(newEntry);

        Log.d("DEBUG", "added new device");

    }
}


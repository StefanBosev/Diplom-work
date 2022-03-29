package org.elsys.smartqrlockapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
        LinearLayout accessListContainer = findViewById(R.id.addToListView);

        JSONObject deviceData = new JSONObject();
        JSONObject accessList = new JSONObject();

        try {

           int entriesNum = accessListContainer.getChildCount();

           for (int i = 0; i < entriesNum; ++i) {
               CardView child = (CardView) accessListContainer.getChildAt(i);
               LinearLayout childLayout = (LinearLayout) child.getChildAt(0);
               TextView name = (TextView) childLayout.getChildAt(0);
               TextView password = (TextView) childLayout.getChildAt(1);
               TextView endDate = (TextView) childLayout.getChildAt(2);

               JSONObject personalData = new JSONObject();
               personalData.put("password", password.getText().toString());
               personalData.put("end-date", endDate.getText().toString());

               accessList.put(name.getText().toString(), personalData);
           }

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
        EditText newDate = new EditText(getApplicationContext());

        newName.setHint("Name");
        newPass.setHint("Password");
        newPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newDate.setHint("date");
        newDate.setInputType(InputType.TYPE_CLASS_DATETIME);

        CardView newEntry = new CardView(getApplicationContext());
        LinearLayout cardLayout = new LinearLayout(getApplicationContext());

        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.addView(newName);
        cardLayout.addView(newPass);
        cardLayout.addView(newDate);


        newEntry.addView(cardLayout);

        LinearLayout list = findViewById(R.id.addToListView);

        newEntry.setMinimumWidth(list.getWidth());;
        newEntry.setCardBackgroundColor(0xFF9BF3F0);
        newEntry.setRadius(25);
        newEntry.setPadding(10, 10, 10, 25);

        list.addView(newEntry);

        Log.d("DEBUG", "added new device");

    }
}


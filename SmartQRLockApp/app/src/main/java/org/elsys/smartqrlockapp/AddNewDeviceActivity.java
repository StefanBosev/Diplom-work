package org.elsys.smartqrlockapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.elsys.smartqrlockapp.factories.AccessCardFactory;
import org.elsys.smartqrlockapp.factories.FileManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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
        LinearLayout accessListContainer = findViewById(R.id.addToListViewEdit);

        JSONObject deviceData = new JSONObject();
        JSONObject accessList = new JSONObject();

        try {

           int entriesNum = accessListContainer.getChildCount();

           for (int i = 0; i < entriesNum; ++i) {
               CardView child = (CardView) accessListContainer.getChildAt(i);
               LinearLayout childLayout = (LinearLayout) child.getChildAt(0);
               TextView name = (TextView) childLayout.getChildAt(1);
               TextView password = (TextView) childLayout.getChildAt(2);
               TextView endDate = (TextView) childLayout.getChildAt(3);

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

        File directory = new File(this.getFilesDir() + File.separator + FileManager.devicesDir);

        if (!directory.exists()) {
            directory.mkdir();
        }

        StringBuilder filename = new StringBuilder();
        filename.append(deviceName.getText().toString().toLowerCase().replaceAll(" ", "-"));

        FileManager fileManager = FileManager.getInstance();

        fileManager.createDeviceFile(directory, filename,  deviceData);
    }

    private void addNewAccessEntry() {
        LinearLayout list = findViewById(R.id.addToListViewEdit);
        CardView newEntry = AccessCardFactory.getInstance().getCard(getApplicationContext(), list, null, null, null);
        newEntry.setClickable(true);

        newEntry.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewDeviceActivity.this);

                builder.setCancelable(true);
                builder.setTitle("Delete entry");
                builder.setMessage("Are you sure you want to delete this entry?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.removeView(newEntry);
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

        list.addView(newEntry);

    }
}


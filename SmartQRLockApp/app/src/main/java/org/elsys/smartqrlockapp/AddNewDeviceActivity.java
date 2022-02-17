package org.elsys.smartqrlockapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

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
//        intent.putExtra("device_name", deviceName.getText().toString());

        startActivity(intent);
    }

    public void addToDevicesList() {
        EditText deviceName = (EditText) findViewById(R.id.setDeviceName);

        DevicesActivity.addDevice(deviceName.getText().toString());
    }
}
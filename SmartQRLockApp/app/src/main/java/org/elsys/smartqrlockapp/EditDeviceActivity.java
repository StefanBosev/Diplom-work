package org.elsys.smartqrlockapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class EditDeviceActivity extends AppCompatActivity {

    Bundle deviceData;
    TextView view;
    ConstraintLayout activityBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_device);

        deviceData = getIntent().getExtras();
        view = findViewById(R.id.deviceNameInEdit);
        activityBody = findViewById(R.id.editDeviceActivityBody);

        activityBody.setBackgroundColor(Color.WHITE);

        view.setText(deviceData.get("name").toString());
        view.setTextColor(Color.BLACK);

    }
}
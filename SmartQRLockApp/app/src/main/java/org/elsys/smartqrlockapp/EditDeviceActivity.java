package org.elsys.smartqrlockapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONObject;

public class EditDeviceActivity extends AppCompatActivity {

    Bundle deviceData;
    TextView view;
    ConstraintLayout activityBody;
    ImageView qrCodeView;
    Bitmap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_device);

        deviceData = getIntent().getExtras();
        view = findViewById(R.id.deviceNameInEdit);
        activityBody = findViewById(R.id.editDeviceActivityBody);
        qrCodeView = findViewById(R.id.qrCodeView);

        activityBody.setBackgroundColor(Color.WHITE);

        view.setText(deviceData.get("name").toString());
        view.setTextColor(Color.BLACK);

        Button generateQRButton = findViewById(R.id.generateQRButton);
        generateQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map = generateQR(view.getText().toString());

                qrCodeView.setImageBitmap(map);
            }
        });
    }

    private Bitmap generateQR(String content) {
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        Display display = manager.getDefaultDisplay();

        Point p = new Point();
        display.getSize(p);

        int width = p.x;
        int height = p.y;

        int dimen = Math.min(width, height);
        dimen = dimen * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(content, null, QRGContents.Type.TEXT, dimen);

        Bitmap newMap = Bitmap.createBitmap(400, 400, Bitmap.Config.RGB_565);

        try {
            newMap = qrgEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            e.printStackTrace();
        }

        return newMap;
    }
}
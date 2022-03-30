package org.elsys.smartqrlockapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ShowQRActivity extends AppCompatActivity {

    ConstraintLayout body;
    ImageView qrCodeView;
    Bitmap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_qr);

        body = findViewById(R.id.showQRActivityBody);
        body.setBackgroundColor(Color.WHITE);
        qrCodeView = findViewById(R.id.qrCodeView);

        map = generateQRMap(getIntent().getStringExtra("data"));
        qrCodeView.setImageBitmap(map);

        Button shareQR = findViewById(R.id.shareQR);
        shareQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable drawable = (BitmapDrawable) qrCodeView.getDrawable();
                Bitmap map = drawable.getBitmap();

                Uri uri = generateUri(map);

                if (uri != null) {
                    shareImage(uri);
                }
            }
        });
    }

    private Bitmap generateQRMap(String content) {
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

    private Uri generateUri(Bitmap map) {
        File directory = new File(getCacheDir(), "images");

        directory.mkdirs();
        File newImage = new File(directory, "qr-for-share.png");

        try {
            FileOutputStream fos = new FileOutputStream(newImage);
            map.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return FileProvider.getUriForFile(getApplicationContext(), "com.anni.shareimage.fileprovider", newImage);
    }

    private void shareImage(Uri uri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sending QR code");
        shareIntent.setType("image/png");

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}
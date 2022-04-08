package org.elsys.smartqrlockapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.elsys.smartqrlockapp.factories.AccessCardFactory;
import org.elsys.smartqrlockapp.factories.FileManager;
import org.elsys.smartqrlockapp.values.Colors;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.CharBuffer;
import java.util.Iterator;

public class EditDeviceActivity extends AppCompatActivity {

    private Bundle deviceData;
    private ConstraintLayout activityBody;
    private JSONObject data;
    private TextView deviceName;
    private TextView devicePlacement;
    private TextView wifiName;
    private TextView wifiPassword;
    private LinearLayout accessList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_device);

        deviceData = getIntent().getExtras();
        activityBody = findViewById(R.id.editDeviceActivityBody);
        activityBody.setBackgroundColor(Colors.APP_BACKGROUND);
        deviceName = findViewById(R.id.setDeviceNameEdit);
        devicePlacement = findViewById(R.id.setDevicePlacementEdit);
        wifiName = findViewById(R.id.wifiNameEdit);
        wifiPassword = findViewById(R.id.wifiPassEdit);
        accessList = findViewById(R.id.accessListScrollViewEdit);

        deviceName.setHint("Device name");
        devicePlacement.setHint("Placement");

        activityBody.setBackgroundColor(Color.WHITE);

        try {
            data = new JSONObject(deviceData.getString("data"));
            wifiName.setText(data.getString("wi-fi-name"));
            wifiPassword.setText(data.getString("wi-fi-pass"));
            deviceName.setText(data.getString("name"));
            devicePlacement.setText(data.getString("place"));

            System.out.println(deviceData.toString());

            JSONObject list = new JSONObject(data.getString("access-list"));

            Iterator<String> listIterator = list.keys();

            while (listIterator.hasNext()) {
                String personName = listIterator.next();
                JSONObject personalData = list.getJSONObject(personName);
                System.out.println(personName);

                CardView newCard = AccessCardFactory.getInstance().getCard(
                                                                        getApplicationContext(),
                                                                        accessList,
                                                                        personName,
                                                                        personalData.get("password").toString(),
                                                                        personalData.get("end-date").toString());

                newCard.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditDeviceActivity.this);

                        builder.setCancelable(true);
                        builder.setTitle("Delete entry?");
                        builder.setMessage("Are you sure you want to delete this entry?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                accessList.removeView(newCard);
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

                newCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject data = new JSONObject();

                        try {
                            data.put(personName, personalData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        showQR(data.toString());
                    }
                });

                accessList.addView(newCard);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button addAccessEntry = findViewById(R.id.addAccessEntryEdit);
        addAccessEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardView newCard = AccessCardFactory.getInstance().getCard(getApplicationContext(), accessList, null, null, null);
                newCard.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditDeviceActivity.this);

                        builder.setCancelable(true);
                        builder.setTitle("Delete entry");
                        builder.setMessage("Are you sure you want to delete this entry?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                accessList.removeView(newCard);
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

                newCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject data = new JSONObject();
                        JSONObject personalData = new JSONObject();
                        TextView name = (TextView) newCard.getChildAt(1);
                        TextView password = (TextView) newCard.getChildAt(2);
                        TextView endDate = (TextView) newCard.getChildAt(3);

                        try {
                            personalData.put("password", password.getText().toString());
                            personalData.put("end-date", endDate.getText().toString());
                            data.put(name.getText().toString(), personalData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        showQR(data.toString());
                    }
                });
                accessList.addView(newCard);
            }
        });

        Button saveEdit = findViewById(R.id.saveEdit);
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFile(deviceData.getString("file-path"));
                returnToMainPage();
            }
        });

        Button sendDeviceFile = findViewById(R.id.sendDeviceFile);
        sendDeviceFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File fileToShare = new File(deviceData.getString("file-path"));
                Uri uri = generateUri(fileToShare);
                shareFile(uri);
            }
        });

    }

    public void returnToMainPage() {

        Intent intent = new Intent(this, DevicesActivity.class);

        startActivity(intent);
    }

    private void showQR(String textToEncode) {
        Intent intent = new Intent(this, ShowQRActivity.class);

        intent.putExtra("data", textToEncode);

        startActivity(intent);
    }

    private void saveFile(String filepath) {
        JSONObject newData = new JSONObject();
        JSONObject newAccessList = new JSONObject();

        try {

            int entriesNum = accessList.getChildCount();

            for (int i = 0; i < entriesNum; ++i) {
                CardView child = (CardView) accessList.getChildAt(i);
                LinearLayout childLayout = (LinearLayout) child.getChildAt(0);
                TextView name = (TextView) childLayout.getChildAt(1);
                TextView password = (TextView) childLayout.getChildAt(2);
                TextView endDate = (TextView) childLayout.getChildAt(3);

                JSONObject personalData = new JSONObject();
                personalData.put("password", password.getText().toString());
                personalData.put("end-date", endDate.getText().toString());

                newAccessList.put(name.getText().toString(), personalData);
            }

            newData.put("name", deviceName.getText().toString());
            newData.put("place", devicePlacement.getText().toString());
            newData.put("wi-fi-name", wifiName.getText().toString());
            newData.put("wi-fi-pass", wifiPassword.getText().toString());
            newData.put("access-list", newAccessList);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        File file = new File(filepath);

        FileManager fileManager = FileManager.getInstance();

        System.out.println(newData.toString());

        fileManager.overwriteFile(file, newData.toString());
    }

    private Uri generateUri(File fileToShare) {
        File directory = new File(getCacheDir(), "file_share");

        if (!directory.exists()) {
            directory.mkdir();
        }

        File fileCopy = new File(directory, "device_data.json");

        try {
            FileInputStream fis = new FileInputStream(fileToShare);
            InputStreamReader inStreamReader = new InputStreamReader(fis);

            FileOutputStream fos = new FileOutputStream(fileCopy);
            OutputStreamWriter outStreamWriter = new OutputStreamWriter(fos);

            int res;
            while ((res = inStreamReader.read()) >= 0) {
                outStreamWriter.write(res);
            }

            inStreamReader.close();
            fis.close();
            outStreamWriter.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return FileProvider.getUriForFile(getApplicationContext(), "com.anni.shareimage.fileprovider", fileCopy);
    }

    private void shareFile(Uri uri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sending Device Data");
        shareIntent.setType("application/json");

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }


}
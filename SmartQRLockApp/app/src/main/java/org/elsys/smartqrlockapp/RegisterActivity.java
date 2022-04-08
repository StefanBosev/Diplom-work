package org.elsys.smartqrlockapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.elsys.smartqrlockapp.factories.FileManager;
import org.elsys.smartqrlockapp.values.Colors;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class RegisterActivity extends AppCompatActivity {

    private static final String credentialsDirName = "crd";

    ConstraintLayout registerBackground;
    Button submit;
    EditText username;
    EditText password;
    EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerBackground = findViewById(R.id.registerBackground);
        registerBackground.setBackgroundColor(Colors.APP_BACKGROUND);

        username = findViewById(R.id.setUsername);
        password = findViewById(R.id.setPassword);
        confirmPassword = findViewById(R.id.setConfirmPassword);

        submit = findViewById(R.id.submit_registration);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passText = password.getText().toString();
                String confirmPassText = confirmPassword.getText().toString();
                String usernameText = username.getText().toString();

                if (!passText.equals(confirmPassText)) {
                    findViewById(R.id.errorMessageRegister).setVisibility(View.VISIBLE);
                    return;
                }

                boolean res = addAccessEntry(usernameText, passText);

                System.out.println();
                goToMainPage();
            }
        });
    }

    private boolean addAccessEntry(String username, String password) {
        File dir = new File(this.getFilesDir() + File.separator + credentialsDirName);

        if (!dir.exists()) {
            Log.d("DEBUG", "creating folder");
            dir.mkdir();
        }

        MessageDigest digestor = null;

        try {
            digestor = MessageDigest.getInstance("SHA-256");
            Log.d("DEBUG", "Created digestor");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (digestor == null) {
            Log.d("DEBUG", "digestor not created");
            return false;
        }

        byte[] hashedUsername = digestor.digest(username.getBytes(StandardCharsets.UTF_8));
        byte[] hashedPassword = digestor.digest(password.getBytes(StandardCharsets.UTF_8));

        Log.d("DEBUG", "hashed username is: " + Arrays.toString(hashedUsername));
        Log.d("DEBUG", "hashed password is: " + Arrays.toString(hashedPassword));

        JSONObject data = new JSONObject();

        try {
            data.put("username", Arrays.toString(hashedUsername));
            data.put("password", Arrays.toString(hashedPassword));
            Log.d("DEBUG", "Added data to json");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        File file = new File(dir, "file.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
                Log.d("DEBUG", "file created");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(file, false);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);

            outputStreamWriter.write(data.toString());
            outputStreamWriter.close();
            fos.close();

            Log.d("DEBUG", "data is written");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private void goToMainPage() {
        Intent intent = new Intent(this, DevicesActivity.class);
        startActivity(intent);
    }
}
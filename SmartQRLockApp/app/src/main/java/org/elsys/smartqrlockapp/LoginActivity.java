package org.elsys.smartqrlockapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.elsys.smartqrlockapp.factories.FileManager;
import org.elsys.smartqrlockapp.values.Colors;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final String credentialsDirName = "crd";

    ConstraintLayout backgroundLayout;
    Button enter;
    Button register;
    EditText username;
    EditText password;
    TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        backgroundLayout = findViewById(R.id.loginBackground);
        backgroundLayout.setBackgroundColor(Colors.APP_BACKGROUND);
        username = findViewById(R.id.getUsername);
        password = findViewById(R.id.getPassword);
        errorMessage = findViewById(R.id.errorMessage);

        enter = findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = username.getText().toString();
                String pass = password.getText().toString();

                int ifExists = checkCredentials(userName, pass);

                switch (ifExists) {
                    case 0:
                        goToMainPage();
                        break;
                    case 1:
                    case 2:
                        errorMessage.setText("No user registered! Register first.");
                        errorMessage.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        errorMessage.setText("Wrong username or password");
                        errorMessage.setVisibility(View.VISIBLE);
                        break;
                    default:
                        errorMessage.setText("Internal error");
                        errorMessage.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegistrationPage();
            }
        });
    }


    private int checkCredentials(String username, String password) {
        File directory = new File(this.getFilesDir() + File.separator + LoginActivity.credentialsDirName);

        if (!directory.exists()) {
            directory.mkdirs();
            return 1;
        }

        MessageDigest digestor = null;

        try {
            digestor = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (digestor == null) {
            return -1;
        }

        byte[] hashedPassword = digestor.digest(password.getBytes(StandardCharsets.UTF_8));
        byte[] hashedUsername = digestor.digest(username.getBytes(StandardCharsets.UTF_8));

        File file = new File(directory, "file.json");

        if (!file.exists()) {
            return 2;
        }

        FileManager fileManager = FileManager.getInstance();
        JSONObject data = fileManager.readFile(file);

        boolean match = true;
        try {
            match = Arrays.toString(hashedPassword).equals(data.getString("password")) &&
                    Arrays.toString(hashedUsername).equals(data.getString("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(data);
        System.out.println("Hashed username: " + Arrays.toString(hashedUsername));
        System.out.println("Hashed password: " + Arrays.toString(hashedPassword));
        System.out.println(match);

        if (match) {
            return 0;
        }

        return 3;
    }

    private void goToMainPage() {
        Intent intent = new Intent(this, DevicesActivity.class);
        startActivity(intent);
    }

    private void goToRegistrationPage() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
package org.elsys.smartqrlockapp.factories;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileManager {
    public static final String devicesDir = "devices_dir";
    private static FileManager fileManager = null;

    public static FileManager getInstance() {
        if (fileManager == null) {
            fileManager = new FileManager();
        }

        return fileManager;
    }


    private FileManager() {}

    private boolean ifNameExistsInDir(String fileName, File[] directory) {
        for (File file : directory) {
            if (file.getName().equals(fileName + ".json")) {
                return true;
            }
        }
        return false;
    }

    public File createDeviceFile(File directory, StringBuilder filename, JSONObject deviceData) {
        while (ifNameExistsInDir(filename.toString(), directory.listFiles())) {
            Log.d("DEBUG", "Filename '" + filename.toString() + "' already exists");
            filename.append("-");
        }

        File newDevice = new File(directory, filename + ".json");

        boolean res = true;
        if (!newDevice.exists()) {
            try {
                res = newDevice.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileOutputStream fos = new FileOutputStream(newDevice);
            OutputStreamWriter outStreamWriter = new OutputStreamWriter(fos);

            outStreamWriter.write(deviceData.toString());
            outStreamWriter.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDevice;
    }


    public void overwriteFile(File file, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(file, false);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);

            outputStreamWriter.write(content);
            outputStreamWriter.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject readFile(File device) {
        StringBuilder data = new StringBuilder();
        JSONObject info = null;

        try {
            BufferedReader bufReader = new BufferedReader(new FileReader(device));
            String line;

            while ((line = bufReader.readLine()) != null) {
                data.append(line);
                data.append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            info = new JSONObject(data.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return info;
    }
}

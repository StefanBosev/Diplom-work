package org.elsys.smartqrlockapp.factories;

import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileManager {
    private static FileManager fileManager = null;

    public static FileManager getInstance() {
        if (fileManager == null) {
            fileManager = new FileManager();
        }

        return fileManager;
    }

//    private File devicesDir = new File(this.getFilesDir() + File.separator + "demo-json-dir");

    private FileManager() {
//        if (!devicesDir.exists()) {
//            devicesDir.mkdirs();
//        }
    }

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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return newDevice;
    }



}
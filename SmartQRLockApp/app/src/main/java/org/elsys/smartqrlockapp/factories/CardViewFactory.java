package org.elsys.smartqrlockapp.factories;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import org.elsys.smartqrlockapp.DevicesActivity;
import org.elsys.smartqrlockapp.EditDeviceActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class CardViewFactory {
    private static CardViewFactory cardViewFactory = null;

    public static CardViewFactory getInstance() {
        if (cardViewFactory == null) {
            cardViewFactory = new CardViewFactory();
        }

        return cardViewFactory;
    }

    LinearLayout.LayoutParams cardLayoutParams;
    LinearLayout.LayoutParams textLayoutParams;

    private CardViewFactory() {
        this.cardLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        this.textLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }

    public CardView getCard(JSONObject cardInfo, String filePath, Context applicationContext, ViewGroup activityBody) {
        CardView newDevice = new CardView(applicationContext);

        newDevice.setPadding(25, 25, 25, 25);
        newDevice.setMinimumWidth(activityBody.getWidth() - 50);
        newDevice.setCardBackgroundColor(0xFF84C984);
        newDevice.setMaxCardElevation(60);
        newDevice.setRadius(25);
        newDevice.setMinimumHeight(60);
        newDevice.setClickable(true);

        LinearLayout textLayout = new LinearLayout(applicationContext);
        textLayout.setLayoutParams(this.textLayoutParams);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setPadding(16, 16, 16, 16);


        TextView heading = new TextView(applicationContext);
        TextView secondary = new TextView(applicationContext);

        String deviceName = "";
        String devicePlace = "";
        try {
            deviceName = cardInfo.getString("name");
            devicePlace = cardInfo.getString("place");
        } catch (JSONException e) {
            deviceName = "Error extracting name from json";
            e.printStackTrace();
        }

        heading.setText(deviceName);
        heading.setTextColor(Color.WHITE);
        heading.setTextSize(20);
        heading.setGravity(Gravity.LEFT);

        secondary.setText(devicePlace);
        secondary.setTextColor(Color.GRAY);
        secondary.setTextSize(15);
        secondary.setGravity(Gravity.LEFT);

        textLayout.addView(heading);
        textLayout.addView(secondary);

        newDevice.addView(textLayout);

        return newDevice;
    }


}

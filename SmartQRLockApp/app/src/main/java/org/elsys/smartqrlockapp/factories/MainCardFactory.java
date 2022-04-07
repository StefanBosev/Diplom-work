package org.elsys.smartqrlockapp.factories;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import org.json.JSONException;
import org.json.JSONObject;
import org.elsys.smartqrlockapp.values.Colors;

public class MainCardFactory {
    private static MainCardFactory mainCardFactory = null;

    public static MainCardFactory getInstance() {
        if (mainCardFactory == null) {
            mainCardFactory = new MainCardFactory();
        }

        return mainCardFactory;
    }

    LinearLayout.LayoutParams cardLayoutParams;
    LinearLayout.LayoutParams textLayoutParams;

    private MainCardFactory() {
        this.cardLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        this.textLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
    }

    public CardView getCard(JSONObject cardInfo, Context applicationContext, ViewGroup activityBody, Integer entriesNum) {
        CardView newDevice = new CardView(applicationContext);

        newDevice.setPadding(25, 25, 25, 25);
        newDevice.setMinimumWidth(activityBody.getWidth() - 50);
        newDevice.setCardBackgroundColor(Colors.CARD_BACKGROUND);
        newDevice.setMaxCardElevation(60);
        newDevice.setRadius(25);
        newDevice.setMinimumHeight(60);
        newDevice.setClickable(true);

        LinearLayout cardLayout = new LinearLayout(applicationContext);
        cardLayout.setLayoutParams(this.textLayoutParams);
        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.setPadding(16, 16, 16, 16);

        LinearLayout textLayout = new LinearLayout(applicationContext);
        textLayout.setLayoutParams(this.textLayoutParams);
        textLayout.setOrientation(LinearLayout.VERTICAL);

        TextView heading = new TextView(applicationContext);
        TextView secondary = new TextView(applicationContext);
        TextView entriesNumView = new TextView(applicationContext);


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
        heading.setTextColor(Colors.CARD_HEADING_TEXT);
        heading.setTextSize(20);
        heading.setGravity(Gravity.LEFT);

        secondary.setText(devicePlace);
        secondary.setTextColor(Colors.CARD_SECONDARY_TEXT);
        secondary.setTextSize(15);
        secondary.setGravity(Gravity.LEFT);

        entriesNumView.setTextSize(13);
        entriesNumView.setGravity(Gravity.LEFT);
        entriesNumView.setTextColor(Colors.CARD_SECONDARY_TEXT);
        if (entriesNum != null) {
            entriesNumView.setText("Entries: " + entriesNum.toString());
        }

        textLayout.addView(heading);
        textLayout.addView(secondary);
        textLayout.addView(entriesNumView);

        cardLayout.addView(textLayout);

        newDevice.addView(cardLayout);

        return newDevice;
    }


}

package org.elsys.smartqrlockapp.factories;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import org.elsys.smartqrlockapp.R;
import org.elsys.smartqrlockapp.values.Colors;

public class AccessCardFactory {
    private static AccessCardFactory accessCardFactory = null;

    public static AccessCardFactory getInstance() {
        if (accessCardFactory == null) {
            accessCardFactory = new AccessCardFactory();
        }

        return accessCardFactory;
    }

    private AccessCardFactory() {}

    public CardView getCard(Context applicationContext, LinearLayout container, String name, String password, String date) {
        EditText newName = new EditText(applicationContext);
        newName.setText(name);
        newName.setHint("Name");
        newName.setMaxWidth(container.getWidth() / 4 * 3);
        newName.setGravity(Gravity.LEFT);

        EditText newPass = new EditText(applicationContext);
        newPass.setText(password);
        newPass.setHint("Password");
        newPass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newPass.setMaxWidth(container.getWidth() / 4 * 3);
        newPass.setGravity(Gravity.LEFT);

        EditText newDate = new EditText(applicationContext);
        newDate.setText(date);
        newDate.setHint("date");
        newDate.setInputType(InputType.TYPE_CLASS_DATETIME);
        newDate.setMaxWidth(container.getWidth() / 4 * 3);
        newDate.setGravity(Gravity.LEFT);

        TextView heading = new TextView(applicationContext);
        String value = "New Entry";
        heading.setText(value);
        heading.setTextColor(Color.BLACK);
        heading.setTextSize(20);


        CardView newEntry = new CardView(applicationContext);
        LinearLayout cardLayout = new LinearLayout(applicationContext);

        cardLayout.setOrientation(LinearLayout.VERTICAL);
        cardLayout.setMinimumHeight(60);
        cardLayout.setPadding(20, 20, 20, 20);

        cardLayout.addView(heading);
        cardLayout.addView(newName);
        cardLayout.addView(newPass);
        cardLayout.addView(newDate);


        newEntry.addView(cardLayout);

        newEntry.setMinimumWidth(container.getWidth() - 20);;
        newEntry.setCardBackgroundColor(0xFF9BF3F0);
        newEntry.setRadius(25);
        newEntry.setPadding(10, 10, 10, 25);
        newEntry.setClickable(true);

        return newEntry;
    }


}

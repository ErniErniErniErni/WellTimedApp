package com.erniwo.timetableconstruct;

import android.content.Context;
import android.widget.Toast;

public class Message {
    public static void showMessage(Context context,String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}

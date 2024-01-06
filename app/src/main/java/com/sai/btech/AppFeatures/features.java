package com.sai.btech.AppFeatures;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class features {
    public static void changeTheme(boolean isChecked) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    public static void SnackBar(View v, String message){
        Snackbar s = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
        s.show();
    }
    public static String readableTime(String time) {
        long timeInMilli = Long.parseLong(time);
        Instant instant = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            instant = Instant.ofEpochMilli(timeInMilli);
        }

        // Convert to LocalDateTime and apply the IST timezone
        LocalDateTime dateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Kolkata"));
        }

        // Format the time as hh:mm:ss a
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        }
        String formattedTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formattedTime = dateTime.format(formatter);
        }

        return formattedTime;
    }

    public static String getDate(String milliseconds) {
        long ms = Long.parseLong(milliseconds);
        Date d = new Date(ms);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return dateFormat.format(d);
    }
}
/*
  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            instant = Instant.ofEpochMilli(timeInMilli);
            LocalDateTime dateTime = null;
            dateTime = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Kolkata"));
            DateTimeFormatter formatter = null;
            formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
            String formattedTime = null;
            formattedTime = dateTime.format(formatter);
        }

 */

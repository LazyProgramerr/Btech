package com.sai.btech.notification;


import android.util.Log;

import androidx.annotation.NonNull;

import com.sai.btech.constants.btech;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CallApi {
    public static void send(JSONObject jsonObject) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        Request request = new Request.Builder()
                .url(btech.FCM_SEND_URL)
                .post(body)
                .addHeader(btech.AUTHORIZATION_HEADER, "key=" + btech.FCM_SERVER_KEY)
                .addHeader(btech.CONTENT_TYPE_HEADER, "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Handle response
                if (!response.isSuccessful()) {
                    Log.i("Error with response", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("Error with response", e.toString());
            }
        });
    }
}


package com.sai.btech.firebaseUtil;

import android.util.Log;

import androidx.annotation.NonNull;

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
    private static final String FCM_SEND_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String FCM_SERVER_KEY = "AAAAcgKTt2Q:APA91bG7CP_-jucmNT-VULLRcmSUSEPyrK6CZoEEbtpSB08UUzpkTBVNNYW-hjDWobB7m1dJ_CSpMrmrR4AkB_4MRQCIivxO4mgslNgT80TxGtDszi3YeSDedlBdm0xrrgM-EDok8oKd";

    public static void call(JSONObject jsonObject) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        Request request = new Request.Builder()
                .url(FCM_SEND_URL)
                .post(body)
                .addHeader(AUTHORIZATION_HEADER, "key=" + FCM_SERVER_KEY)
                .addHeader(CONTENT_TYPE_HEADER, "application/json")
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


package com.sai.btech.notification;

import static com.sai.btech.constants.btech.FCM_SEND_URL;
import static com.sai.btech.constants.btech.FCM_SERVER_KEY;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SendNotificationUsingVolley {
    public static void sendFCMNotification(Context context, ArrayList<String> toList, String title, String body) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JSONObject jsonBody = new JSONObject();
        try {
            // Use "to" field instead of "registration_ids" for newer versions of FCM
            jsonBody.put("to", getToParameter(toList));

            JSONObject data = new JSONObject();
            data.put("title", title);
            data.put("body", body);

            jsonBody.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, FCM_SEND_URL, jsonBody,
                response -> Log.d("FCM Response", "Response: " + response.toString()),
                error -> Log.e("FCM Error", "Error: " + error.toString())) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                return jsonBody.toString().getBytes();
            }

            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> headers = new java.util.HashMap<>();
                headers.put("Authorization", "key=" + FCM_SERVER_KEY);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    private static String getToParameter(ArrayList<String> toList) throws JSONException {
        if (toList.size() == 1) {
            return toList.get(0);
        } else {
            JSONArray toJSONArray = new JSONArray(toList);
            return toJSONArray.toString();
        }
    }
}

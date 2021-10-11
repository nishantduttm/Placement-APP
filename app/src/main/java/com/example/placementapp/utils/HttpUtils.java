package com.example.placementapp.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

public final class HttpUtils {

    public static void addRequestToHttpQueue(Request<?> httpRequest, Context context) {
        if (context == null) {
            if (httpRequest.getErrorListener() != null) {
                httpRequest.getErrorListener().onErrorResponse(new VolleyError("Context is Null"));
            }
            return;
        }

        // Add Request to queue
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(httpRequest);
    }
}

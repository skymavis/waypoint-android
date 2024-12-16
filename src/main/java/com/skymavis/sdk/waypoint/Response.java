package com.skymavis.sdk.waypoint;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Response {
    public final boolean success;
    public final String state;
    public final ResponseError error;
    public final Map<String, String> data;

    public Response(boolean success, String state, @Nullable ResponseError error, @Nullable Map<String, String> data) {
        this.success = success;
        this.state = state;
        this.error = error;
        this.data = data != null ? data : new HashMap<>();
    }

    public static Response parseDeeplink(Uri deeplink) {
        if (deeplink == null) {
            return new Response(false, null, null, null);
        }

        String state = deeplink.getQueryParameter("state");
        Map<String, String> queryParams = new HashMap<>();
        for (String key : deeplink.getQueryParameterNames()) {
            String value = deeplink.getQueryParameter(key);
            if (value != null) {
                queryParams.put(key, value);
            }
        }

        boolean success = "success".equalsIgnoreCase(queryParams.get("type"));

        if (!success) {
            ResponseError error = new ResponseError(queryParams.getOrDefault("message", "Unknown error"), Integer.parseInt(queryParams.getOrDefault("code", "0")));
            return new Response(false, state, error, null);
        }

        return new Response(true, state, null, queryParams);
    }

    public static Response parseDeeplink(String deeplink) {
        if (deeplink == null || deeplink.isEmpty()) {
            return new Response(false, null, null, null);
        }

        Uri uri = Uri.parse(deeplink);
        return parseDeeplink(uri);
    }

    @Nullable
    public String getValue(String key) {
        return data.get(key);
    }

    @NonNull
    @Override
    public String toString() {
        return "Response{" +
                "success=" + success +
                ", state='" + state + '\'' +
                ", error=" + error +
                ", data=" + data +
                '}';
    }
}

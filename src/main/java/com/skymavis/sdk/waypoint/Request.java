package com.skymavis.sdk.waypoint;

import android.content.Context;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;

import java.util.Map;
import java.util.stream.Collectors;

public class Request {
    private final Context context;
    private final String endpoint;
    private final Map<String, String> params;

    public Request(Context context, String endpoint, Map<String, String> params) {
        this.context = context;
        this.endpoint = endpoint;
        this.params = params.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Uri constructURLWithParams(String urlString, Map<String, String> params) {
        Uri.Builder builder = Uri.parse(urlString).buildUpon();
        for (Map.Entry<String, String> param : params.entrySet()) {
            builder.appendQueryParameter(param.getKey(), param.getValue());
        }
        return builder.build();
    }

    public void execute() {
        Uri sessionUrl = constructURLWithParams(endpoint, params);

        CustomTabsIntent.Builder customTabBuilder = new CustomTabsIntent.Builder();
        customTabBuilder
                .setCloseButtonPosition(CustomTabsIntent.CLOSE_BUTTON_POSITION_END)
                .setShowTitle(false)
                .setUrlBarHidingEnabled(false)
                .setShareIdentityEnabled(false)
                .setShareState(CustomTabsIntent.SHARE_STATE_DEFAULT);

        CustomTabsIntent intent = customTabBuilder.build();

        intent.launchUrl(context, sessionUrl);
    }
}

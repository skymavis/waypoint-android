package com.skymavis.sdk.waypoint;

import androidx.annotation.NonNull;

public class ResponseError {
    public final String message;
    public final int code;

    public ResponseError(String message, int code) {
        this.message = message;
        this.code = code;
    }

    @NonNull
    @Override
    public String toString() {
        return "ResponseError{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}

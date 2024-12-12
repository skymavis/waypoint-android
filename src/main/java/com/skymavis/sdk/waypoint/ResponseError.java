package com.skymavis.sdk.waypoint;

public class ResponseError {
    public final String message;
    public final int code;

    public ResponseError(String message, int code) {
        this.message = message;
        this.code = code;
    }
}

package com.skymavis.sdk.waypoint;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Waypoint {
    private final String clientId;
    private final String waypointOrigin;
    private final String redirectUri;
    private final String rpcUrl;
    private final int chainId;

    public Waypoint(String waypointOrigin, String clientId, String redirectUri, Boolean isTestnet) {
        this.waypointOrigin = waypointOrigin;
        this.clientId = clientId;
        this.redirectUri = redirectUri;

        Network network = isTestnet ? Network.Testnet : Network.Mainnet;
        this.rpcUrl = network.rpcUrl;
        this.chainId = network.chainId;
    }

    private String constructWaypointEndpoint(String method) {
        String path;
        switch (method) {
            case ServicePaths.authorize:
                path = "/" + ServicePaths.client + "/" + clientId + "/" + ServicePaths.authorize;
                break;
            case ServicePaths.send:
            case ServicePaths.sign:
                path = "/" + ServicePaths.wallet + "/" + method;
                break;
            case ServicePaths.guests:
                path = "/" + ServicePaths.seamless + "/" + ServicePaths.guests + "/" + ServicePaths.start;
                break;
            case ServicePaths.register:
                path = "/" + ServicePaths.guests + "/" + ServicePaths.register;
                break;
            case ServicePaths.setup:
                path = "/" + ServicePaths.wallet + "/" + ServicePaths.setup + "/" + ServicePaths.introduce;
                break;
            default:
                path = "";
        }
        return waypointOrigin + path;
    }

    private Map<String, String> constructWaypointParams(Map<String, String> additionalParams) {
        Map<String, String> params = new HashMap<>();
        params.put(RequestParams.clientId, clientId);
        params.put(RequestParams.redirect, redirectUri);
        params.put(RequestParams.chainId, String.valueOf(chainId));
        if (additionalParams != null) {
            additionalParams.entrySet().stream()
                    .filter(entry -> entry.getValue() != null)
                    .forEach(entry -> params.put(entry.getKey(), entry.getValue()));
        }
        return params;
    }

    public void authorize(Context context, String state, String scope) {
        String endpoint = constructWaypointEndpoint(ServicePaths.authorize);

        HashMap<String, String> additionalParams = new HashMap<>();
        additionalParams.put(RequestParams.state, state);
        additionalParams.put(RequestParams.scope, scope);

        Request request = new Request(context, endpoint, constructWaypointParams(additionalParams));
        request.execute();
    }

    public void personalSign(Context context, String state, String message, String from) {
        String endpoint = constructWaypointEndpoint(ServicePaths.sign);

        HashMap<String, String> additionalParams = new HashMap<>();
        additionalParams.put(RequestParams.state, state);
        additionalParams.put(RequestParams.message, message);
        additionalParams.put(RequestParams.expectAddress, from);

        Request request = new Request(context, endpoint, constructWaypointParams(additionalParams));
        request.execute();
    }

    public void signTypedData(Context context, String state, String typedData, String from) {
        String endpoint = constructWaypointEndpoint(ServicePaths.sign);

        HashMap<String, String> additionalParams = new HashMap<>();
        additionalParams.put(RequestParams.state, state);
        additionalParams.put(RequestParams.typedData, typedData);
        additionalParams.put(RequestParams.expectAddress, from);

        Request request = new Request(context, endpoint, constructWaypointParams(additionalParams));
        request.execute();
    }

    public void sendTransaction(Context context, String state, String to, String data, String value,
                                String from) {
        String endpoint = constructWaypointEndpoint(ServicePaths.send);

        HashMap<String, String> additionalParams = new HashMap<>();
        additionalParams.put(RequestParams.state, state);
        additionalParams.put(RequestParams.to, to);
        additionalParams.put(RequestParams.data, data);
        additionalParams.put(RequestParams.value, value);
        additionalParams.put(RequestParams.expectAddress, from);

        Request request = new Request(context, endpoint, constructWaypointParams(additionalParams));
        request.execute();
    }

    public void sendNativeToken(Context context, String state, String to, String value, String from) {
        String endpoint = constructWaypointEndpoint(ServicePaths.send);

        HashMap<String, String> additionalParams = new HashMap<>();
        additionalParams.put(RequestParams.state, state);
        additionalParams.put(RequestParams.to, to);
        additionalParams.put(RequestParams.value, value);
        additionalParams.put(RequestParams.expectAddress, from);

        Request request = new Request(context, endpoint, constructWaypointParams(additionalParams));
        request.execute();
    }

    public void authAsGuest(Context context, String state, String credential, String authDate,
                            String hash, String scope) {
        String endpoint = constructWaypointEndpoint(ServicePaths.guests);

        HashMap<String, String> additionalParams = new HashMap<>();
        additionalParams.put(RequestParams.state, state);
        additionalParams.put(RequestParams.credential, credential);
        additionalParams.put(RequestParams.authDate, authDate);
        additionalParams.put(RequestParams.hash, hash);
        additionalParams.put(RequestParams.scope, scope);

        Request request = new Request(context, endpoint, constructWaypointParams(additionalParams));
        request.execute();
    }

    public void registerGuestAccount(Context context, String state) {
        String endpoint = constructWaypointEndpoint(ServicePaths.register);

        HashMap<String, String> additionalParams = new HashMap<>();
        additionalParams.put(RequestParams.state, state);

        Request request = new Request(context, endpoint, constructWaypointParams(additionalParams));
        request.execute();
    }

    public void createKeylessWallet(Context context, String state) {
        String endpoint = constructWaypointEndpoint(ServicePaths.setup);

        HashMap<String, String> additionalParams = new HashMap<>();
        additionalParams.put(RequestParams.state, state);

        Request request = new Request(context, endpoint, constructWaypointParams(additionalParams));
        request.execute();
    }
}

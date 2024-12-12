package com.skymavis.sdk.waypoint;

public class Network {
    public static final Network Mainnet = new Network(2020, "https://api.roninchain.com/rpc");
    public static final Network Testnet = new Network(2021, "https://saigon-testnet.roninchain.com/rpc");

    public final int chainId;
    public final String rpcUrl;

    public Network(int chainId, String rpcUrl) {
        this.chainId = chainId;
        this.rpcUrl = rpcUrl;
    }
}

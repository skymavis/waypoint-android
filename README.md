# Mavis ID Android SDK

Android SDK use to interact with Mavis ID, use to develop in app Web3 wallet

# Table of contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Initialization](#initialization)
- [Getting started](#getting-started)
  - [Config Android Manifest](#config-android-manifest)
  - [Examples](#examples)

## Features

- [x] Authorize
- [x] Sign message
- [x] Sign typed data
- [x] Send transaction
- [x] Call contract

## Prerequisites

- `Android` : >= `7.0` (API Level 24)
- Register your application with Sky Mavis to get `YOUR_APP_ID` and `YOUR_DEEP_LINK_SCHEMA`
- Request permission to use Mavis ID
- Go to Developer Console > your app > App Permission > Mavis ID > Request Access

[Head to the detail guide](https://docs.skymavis.com/comming-soon) to acquired `YOUR_APP_ID` and `YOUR_DEEP_LINK_SCHEMA`

## Installation

## Initialization

Initial SDK `client`

```kotlin
    val appId = ${YOUR_APP_ID}
    val deeplinkSchema = ${YOUR_DEEP_LINK_SCHEMA}
    val state = "test";
    // Gate orign
    val gateOrigin = "http://id.skymavis.one"
    // Ronin Testnet
    val chainId = 2021;
    val rpcUrl = "https://saigon-testnet.roninchain.com/rpc"

    val client = Client(
        gateOrigin,
        clientId,
        rpcUrl,
        chainId
    );
```

## Getting started

### Config Android Manifest

Config your `AndroidManifest.xml` with deeplink schema has been registered

```xml
    <intent-filter android:label="id.open">
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <!-- Accepts URIs that begin with "deeplinkSchema://open” -->
        <data android:scheme=${deeplink_Schema} android:host="open" />
    </intent-filter>
```

### Examples

Authorize :

```kotlin
fun AuthComponent(uri: Uri?, context: Context, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            client.authorize(context, redirect, state) // Example function call
        }) {
            Text("Authorize")
        }
    }
```

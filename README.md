# Ronin Waypoint Android SDK

The Ronin Waypoint Android SDK lets developers integrate the account and wallet features of the Ronin Waypoint service into Android apps developed with Kotlin. After the integration, users can sign in to your game with their Ronin Waypoint account and connect their keyless wallet for instant in-game transactions.

## Features

- Authorize users: let users sign in to your app with Ronin Waypoint to connect their keyless wallet and an optional externally owned account (EOA) wallet.
- Send transactions: transfer RON, ERC-20 tokens, and make contract calls for in-game transactions.
- Sign messages and typed data: prove ownership of a wallet or sign structured data.

## Prerequisites

- [Android API level 24](https://developer.android.com/about/versions/nougat) or later.
- An app created in the [Developer Console](https://developers.skymavis.com/console/applications/).
- Permission to use Ronin Waypoint. Request in **Developer Console > your app > App Permission > Sky Mavis Account (OAuth 2.0) > Request Access**.
- A client ID that you can find in **Developer Console > Products > Waypoint Service > CLIENT ID (APPLICATION ID)**.
- A redirect URI registered in **Developer Console > Products > Waypoint Service > REDIRECT URI**.

For more information about the initial setup, see [Get started](https://docs.skymavis.com/mavis/mavis-id/guides/get-started).

## Installation

### Import the library

1. In your Android project, create a `libs` directory.
2. Download the [Ronin Waypoint Android SDK](https://github.com/skymavis/mavis-id-android/releases) release package and extract the contents into `libs`.
3. In your app's `build.gradle` file, add the following dependency:

```gradle
dependencies {
    implementation(files("libs/waypoint-0.1.0.aar"))
}
```

### Configure Gradle

In `settings.gradle`, include the Maven repository URL:

```gradle
maven {
    url = uri("https://maven.pkg.github.com/skymavis/waypoint-android")
}
```

Add the dependency to `build.gradle`:

```gradle
dependencies {
    implementation("com.skymavis.sdk:waypoint:0.1.0")
}
```

### Configure Android Manifest

Update your app's `AndroidManifest.xml` file with the redirect URI that you registered in the Developer Console:

```xml
<intent-filter android:label="id.open">
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <!-- Accepts URIs that begin with "deeplinkSchema://callback -->
    <data android:scheme="${deeplink_Schema}" android:host="callback" />
</intent-filter>
```

## Initialization

```kotlin
import com.skymavis.sdk.waypoint.Waypoint
...
// Redirect URI registered in the Ronin Waypoint settings in the Developer Console
// Example : "mydapp://callback"
val redirectUri = "{YOUR_REDIRECT_URI}"
// Client ID registered in the Ronin Waypoint settings in the Developer Console
val clientId = "${YOUR_CLIENT_ID}"
// Base URL of Ronin Waypoint for all API calls
val waypointOrigin = "https://waypoint.roninchain.com"
// Saigon testnet chain ID
val chainId = 2021
// Saigon testnet public RPC endpoint
val rpcUrl = "https://saigon-testnet.roninchain.com/rpc"

val waypoint = Waypoint(
    waypointOrigin,
    clientId,
    rpcUrl,
    chainId
);
```

## Usage

### Listen response

Ronin Waypoint will return result via deeplink, `onResponse` listen all match with state which is define when call.

```kotlin
// In onCreate of activity listen all incoming deeplink
val data: Uri? = intent?.data // Capture the URI data
       if(data != null) {
           waypoint.onResponse(data)
       }
```

### Authorize users

Authorizes a user with an existing Ronin Waypoint account, returning an ID token and the user's wallet address.

```kotlin
// Random request ID
val state = UUID.randomUUID().toString();
waypoint.authorize(context, state, redirectUri).thenAccept { data ->
                if(data != null) {
                    Log.d(TAG, "Auth data : $data")
                } else {
                    print("No data received !")
                }
            }
```

### Send transactions

Transfers 0.1 RON to another address, returning a transaction hash.

```kotlin
// Random request ID
val state = UUID.randomUUID().toString()
// Recipient address
val to = "0xD36deD8E1927dCDD76Bfe0CC95a5C1D65c0a807a";
// 0.1 RON
val value = "100000000000000000";

waypoint.sendTransaction(context, state, redirectUri, to, value).thenAccept { data ->
    if(data != null) {
        Log.d(TAG, "Send transaction data : $data")
    } else {
        print("No data received !")
    }
};
```

### Sign messages

Signs a plain text message, returning a signature in hex format.

```kotlin
val state = UUID.randomUUID().toString()
// Message to sign
val message = "Hello Axie";
waypoint.personalSign(context, state, redirectUri, message).thenAccept { data ->
    if(data != null) {
        Log.d(TAG, "Personal sign data : $data")
    } else {
        print("No data received !")
    }
}
```

### Sign typed data

Signs [EIP-712](https://eips.ethereum.org/EIPS/eip-712) typed data for an order on Axie Marketplace, returning a signature in hex format.

```kotlin
val state = UUID.randomUUID().toString()
val typedData = """{"types":{"Asset":[{"name":"erc","type":"uint8"},{"name":"addr","type":"address"},{"name":"id","type":"uint256"},{"name":"quantity","type":"uint256"}],"Order":[{"name":"maker","type":"address"},{"name":"kind","type":"uint8"},{"name":"assets","type":"Asset[]"},{"name":"expiredAt","type":"uint256"},{"name":"paymentToken","type":"address"},{"name":"startedAt","type":"uint256"},{"name":"basePrice","type":"uint256"},{"name":"endedAt","type":"uint256"},{"name":"endedPrice","type":"uint256"},{"name":"expectedState","type":"uint256"},{"name":"nonce","type":"uint256"},{"name":"marketFeePercentage","type":"uint256"}],"EIP712Domain":[{"name":"name","type":"string"},{"name":"version","type":"string"},{"name":"chainId","type":"uint256"},{"name":"verifyingContract","type":"address"}]},"domain":{"name":"MarketGateway","version":"1","chainId":2021,"verifyingContract":"0xfff9ce5f71ca6178d3beecedb61e7eff1602950e"},"primaryType":"Order","message":{"maker":"0xd761024b4ef3336becd6e802884d0b986c29b35a","kind":"1","assets":[{"erc":"1","addr":"0x32950db2a7164ae833121501c797d79e7b79d74c","id":"2730069","quantity":"0"}],"expiredAt":"1721709637","paymentToken":"0xc99a6a985ed2cac1ef41640596c5a5f9f4e19ef5","startedAt":"1705984837","basePrice":"500000000000000000","endedAt":"0","endedPrice":"0","expectedState":"0","nonce":"0","marketFeePercentage":"425"}}""";
waypoint.signTypedData(context, state, redirectUri, typedData).thenAccept { data ->
    if(data != null) {
        Log.d(TAG, "Sign typed data : $data")
    } else {
        print("No data received !")
    }
}
```

### Call contracts

Allows another contract to spend 1 AXS on user's behalf, returning a transaction hash.

```kotlin
val contractAddress = "0x3c4e17b9056272ce1b49f6900d8cfd6171a1869d";
// No RON is being sent
val value = "0x0"
// Approve data
val data =
    "0xa9059cbb000000000000000000000000edb40e7abaa613a0b06d86260dd55c7eb2df2447000000000000000000000000000000000000000000000000016345785d8a0000";
val state = UUID.randomUUID().toString()
waypoint.callContract(
    context,
    state,
    redirectUri,
    contractAddress,
    data,
    value
).thenAccept { data ->
    if(data != null) {
        Log.d(TAG, "Call contract data : $data")
    } else {
        print("No data received !")
    }
}
```

## Documentation

For more information, see the [Ronin Waypoint Android SDK](https://docs.skymavis.com/mavis/mavis-id/guides/android-sdk) integration guide.

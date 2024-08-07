# Mavis ID Android SDK

The Mavis ID iOS SDK lets developers integrate Mavis ID into mobile apps developed for the Android platform. After the integration, users can sign in to your app with Mavis ID and create an embedded Web3 wallet to interact with the blockchain to send and receive tokens, sign messages, and more.

## Features

* Authorize users: sign in to your app with Mavis ID.
* Send transactions: transfer tokens to other addresses.
* Sign messages: sign plain text messages.
* Sign typed data: sign structured data according to the EIP-712 standard.
* Call contracts: execute custom transactions on smart contracts.

## Prerequisites

* [Android API level 24](https://developer.android.com/about/versions/nougat) or later.
* An app created in the [Developer Console](https://developers.skymavis.com/console/applications/).
* Permission to use Mavis ID. Request in **Developer Console > your app > App Permission > Sky Mavis Account (OAuth 2.0) > Request Access**.
* A client ID that you can find in **Developer Console > Products > ID Service > CLIENT ID (APPLICATION ID)**.
* A redirect URI registered in **Developer Console > Products > ID Service > REDIRECT URI**.

For more information about the initial setup, see [Get started](https://docs.skymavis.com/mavis/mavis-id/guides/get-started).

## Installation

### Import the lib

1. In your Android project, create a `libs` directory.
2. Download the [Mavis ID Android SDK](https://github.com/skymavis/mavis-id-android/releases) release package and extract the contents into `libs`.
3. In your app's `build.gradle` file, add the following dependency:

```gradle
dependencies {
    implementation files("libs/id-0.1.0.aar")
}
```

### Configure Gradle

In `settings.gradle`, include the Maven repository URL:

```gradle
maven {
    url = uri("https://maven.pkg.github.com/skymavis/mavis-id-android")
}
```

Add the dependency to `build.gradle`:

```gradle
dependencies {
    implementation("com.skymavis.sdk:id:0.1.0")
}
```

#### Configure Android Manifest

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
// Redirect URI registered in the Mavis ID settings in the Developer Console
val redirectUri = "mydapp://callback"
// Client ID registered in the Mavis ID settings in the Developer Console
val clientId = "cbabcb00-9c99-404b-a6e4-c76b3b59f0d8"
// Base URL of Mavis ID for all API calls
val gateOrigin = "http://id.skymavis.com"
// Ronin chain ID to connect to: `2021` for Saigon testnet and `2020` for Ronin mainnet
val chainId = 2021
// 
val rpcUrl = "https://saigon-testnet.roninchain.com/rpc"

client = Client(
    gateOrigin,
    clientId,
    rpcUrl,
    chainId
)
```

## Usage

### Authorize users

Authorizes a user with an existing Mavis ID account, returning an ID token and the user's wallet address.

```kotlin
// Request ID
val state = client.authorize(context, redirectUri)

// Get result via deeplink
var result = Result.from(uri)

println("Is success: ${result.isSuccess}")
println("Method: ${result.method}")
println("Address: ${result.address}")
println("Data: ${result.data}")
println("State: ${result.state}")
```

### Send transactions

Transfers 1 RON to another address, returning a transaction hash.

```kotlin
import com.skymavis.sdk.id.Client
import com.skymavis.sdk.id.Result

// Recipient address
val to = "0xD36deD8E1927dCDD76Bfe0CC95a5C1D65c0a807a";
// Value in wei (1 RON = 10^18 wei)
val value = "1000000000000000000";
// Request ID
val state = client.sendTransaction(context, redirectUri, to, value);

// Get result from the deeplink
var result = Result.from(uri)

println("Is success: ${result.isSuccess}")
println("Method: ${result.method}")
println("Address: ${result.address}")
println("Data: ${result.data}")
println("State: ${result.state}")
```

### Sign messages

Signs a plain text message, returning a signature in hex format.

```kotlin
// Message to sign
val message = "I accept the terms and conditions.";
// Request ID
val state = client.personalSign(context, redirectUri, message)
// Get result from the deeplink
var result = Result.from(uri)

println("Is success: ${result.isSuccess}")
println("Method: ${result.method}")
println("Address: ${result.address}")
println("Data: ${result.data}")
println("State: ${result.state}")
```

### Sign typed data

Signs EIP-712 typed data for an order on Axie Marketplace, returning a signature in hex format.

```kotlin
val typedData = """{"types":{"Asset":[{"name":"erc","type":"uint8"},{"name":"addr","type":"address"},{"name":"id","type":"uint256"},{"name":"quantity","type":"uint256"}],"Order":[{"name":"maker","type":"address"},{"name":"kind","type":"uint8"},{"name":"assets","type":"Asset[]"},{"name":"expiredAt","type":"uint256"},{"name":"paymentToken","type":"address"},{"name":"startedAt","type":"uint256"},{"name":"basePrice","type":"uint256"},{"name":"endedAt","type":"uint256"},{"name":"endedPrice","type":"uint256"},{"name":"expectedState","type":"uint256"},{"name":"nonce","type":"uint256"},{"name":"marketFeePercentage","type":"uint256"}],"EIP712Domain":[{"name":"name","type":"string"},{"name":"version","type":"string"},{"name":"chainId","type":"uint256"},{"name":"verifyingContract","type":"address"}]},"domain":{"name":"MarketGateway","version":"1","chainId":2021,"verifyingContract":"0xfff9ce5f71ca6178d3beecedb61e7eff1602950e"},"primaryType":"Order","message":{"maker":"0xd761024b4ef3336becd6e802884d0b986c29b35a","kind":"1","assets":[{"erc":"1","addr":"0x32950db2a7164ae833121501c797d79e7b79d74c","id":"2730069","quantity":"0"}],"expiredAt":"1721709637","paymentToken":"0xc99a6a985ed2cac1ef41640596c5a5f9f4e19ef5","startedAt":"1705984837","basePrice":"500000000000000000","endedAt":"0","endedPrice":"0","expectedState":"0","nonce":"0","marketFeePercentage":"425"}}""";

// Request ID
val state = client.signTypeData(context, redirectUri, typedData)

// Get result from the deeplink
var result = Result.from(uri)

println("Is success: ${result.isSuccess}")
println("Method: ${result.method}")
println("Address: ${result.address}")
println("Data: ${result.data}")
println("State: ${result.state}")
```

### Call contracts

Allows another contract to spend 1 AXS on user's behalf.

```kotlin
// Address of the smart contract to interact with
val contractAddress = "0x3c4e17b9056272ce1b49f6900d8cfd6171a1869d";
// No RON is being sent
val value = "0x0"

// Data for approving 1 AXS
val data =
"0x095ea7b30000000000000000000000006b190089ed7f75fe17b3b0a17f6ebd69f72c3f630000000000000000000000000000000000000000000000000de0b6b3a7640000";

// Request ID
val state = client.callContract(
    context,
    redirectUri,
    contractAddress,
    data,
    value
)

// Get result from the deeplink
var result = Result.from(uri)

println("Is success: ${result.isSuccess}")
println("Method: ${result.method}")
println("Address: ${result.address}")
println("Data: ${result.data}")
println("State: ${result.state}")
```

## Documentation

For more information, see the [Mavis ID Android SDK](https://docs.skymavis.com/mavis/mavis-id/guides/android-sdk) guide.

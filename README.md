# Mavis ID Android SDK

Android SDK use to interact with Mavis ID, use to develop in app Web3 wallet

## Features

- [x] Authorize
- [x] Sign message
- [x] Sign typed data
- [x] Send transaction
- [x] Call contract

## Prerequisites

- `Android` : >= `7.0` (API Level 24)
- Register your application with Sky Mavis to get `YOUR_APP_ID` and `YOUR_REDIRECT_URI`
- Request permission to use Mavis ID
- Go to Developer Console > your app > App Permission > Mavis ID > Request Access

[Go to developer portal](https://developers.skymavis.com/) to acquired `YOUR_APP_ID` and `YOUR_REDIRECT_URI`

## Installation

### Import lib

- Create a `libs` directory in `example`
- Download release package to the `libs` dir has been created
- Add : `implementation(files("libs/mavis-id-sdk-v0.1.0-rc.aar"))` to `dependencies` block in `build.gradle`

### Gradle

Config maven `settings.gradle`

```
maven
{
    url = uri("https://maven.pkg.github.com/skymavis/mavis-id-android")
}
```

Add to `build.gradle`

```
implementation("com.skymavis.sdk.id:1.0.0")
```

## Getting started

### Config Android Manifest

Config your `AndroidManifest.xml` with `redirectUri` has been registered with Mavid ID

```xml
    <intent-filter android:label="id.open">
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <!-- Accepts URIs that begin with "deeplinkSchema://open” -->
        <data android:scheme=${deeplink_Schema} android:host="open" />
    </intent-filter>
```

## Initialization

Initial SDK `Client`

- `redirectUri` : The redirect URI registered in Mavis ID. This is the URI that the user will be redirected to after the operation.
- `clientId`: The client ID registered in Mavis ID. This is used to identify your application.
- `gateOrigin` : The URL of Mavis ID. This is the base URL for all API calls.
- `chainId`: The ID of the blockchain network. In this example, it's set to 2021 for the Ronin Testnet.

```kotlin
// Redirect URI registered in Mavis ID
var redirectUri = "axie://open"
// The client ID registered in Mavis ID
val clientId = "axie"
// The url of Mavis ID
val gateOrigin = "http://id.skymavis.com"
// Ronin Testnet
val chainId = 2021;
val rpcUrl = "https://saigon-testnet.roninchain.com/rpc"

client = Client(
    gateOrigin,
    clientId,
    rpcUrl,
    chainId
);
```

### Examples

Authorize :

- This function authorizes a user with an existing Mavis ID account. If the user does not have an account, they will be prompted to create one.

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

Send transaction :

- Send 1 Ron

```kotlin
import com.skymavis.sdk.id.Client
import com.skymavis.sdk.id.Result
...
// Reciver address
val to = "0xD36deD8E1927dCDD76Bfe0CC95a5C1D65c0a807a";
// Value with decimal
val value = "1000000000000000000";
// Request ID
val state = client.sendTransaction(context, redirectUri, to, value);

// Get result via deeplink
var result = Result.from(uri)

println("Is success: ${result.isSuccess}")
println("Method: ${result.method}")
println("Address: ${result.address}")
println("Data: ${result.data}")
println("State: ${result.state}")
```

Personal sign

- Sign a message

```kotlin
// Message want to sign
val message = "Hello Axie";
// Request ID
val state = client.personalSign(context, redirectUri, message)
// Get result via deeplink
var result = Result.from(uri)

println("Is success: ${result.isSuccess}")
println("Method: ${result.method}")
println("Address: ${result.address}")
println("Data: ${result.data}")
println("State: ${result.state}")
```

Sign Typed Data

- Signs the typed data value with types data structure for domain using the EIP-712 specification.
- Sign typed data of an Axie

```kotlin
// TypedData struct
val typedData = """{"types":{"Asset":[{"name":"erc","type":"uint8"},{"name":"addr","type":"address"},{"name":"id","type":"uint256"},{"name":"quantity","type":"uint256"}],"Order":[{"name":"maker","type":"address"},{"name":"kind","type":"uint8"},{"name":"assets","type":"Asset[]"},{"name":"expiredAt","type":"uint256"},{"name":"paymentToken","type":"address"},{"name":"startedAt","type":"uint256"},{"name":"basePrice","type":"uint256"},{"name":"endedAt","type":"uint256"},{"name":"endedPrice","type":"uint256"},{"name":"expectedState","type":"uint256"},{"name":"nonce","type":"uint256"},{"name":"marketFeePercentage","type":"uint256"}],"EIP712Domain":[{"name":"name","type":"string"},{"name":"version","type":"string"},{"name":"chainId","type":"uint256"},{"name":"verifyingContract","type":"address"}]},"domain":{"name":"MarketGateway","version":"1","chainId":2021,"verifyingContract":"0xfff9ce5f71ca6178d3beecedb61e7eff1602950e"},"primaryType":"Order","message":{"maker":"0xd761024b4ef3336becd6e802884d0b986c29b35a","kind":"1","assets":[{"erc":"1","addr":"0x32950db2a7164ae833121501c797d79e7b79d74c","id":"2730069","quantity":"0"}],"expiredAt":"1721709637","paymentToken":"0xc99a6a985ed2cac1ef41640596c5a5f9f4e19ef5","startedAt":"1705984837","basePrice":"500000000000000000","endedAt":"0","endedPrice":"0","expectedState":"0","nonce":"0","marketFeePercentage":"425"}}""";
// Request ID
val state = client.signTypeData(context, redirectUri, typedData)

// Get result via deeplink
var result = Result.from(uri)

println("Is success: ${result.isSuccess}")
println("Method: ${result.method}")
println("Address: ${result.address}")
println("Data: ${result.data}")
println("State: ${result.state}")
```

Call contract

- Approve 1 AXS

```kotlin

// Contract address want to interact with
val contractAddress = "0x3c4e17b9056272ce1b49f6900d8cfd6171a1869d";
// No RON is being sent
val value = "0x0"
// Approve data

val data =
"0x095ea7b30000000000000000000000006b190089ed7f75fe17b3b0a17f6ebd69f72c3f630000000000000000000000000000000000000000000000000de0b6b3a7640000";// Request ID
val state = client.callContract(
    context,
    redirectUri,
    contractAddress,
    data,
    value
)

// Get result via deeplink
var result = Result.from(uri)

println("Is success: ${result.isSuccess}")
println("Method: ${result.method}")
println("Address: ${result.address}")
println("Data: ${result.data}")
println("State: ${result.state}")
```

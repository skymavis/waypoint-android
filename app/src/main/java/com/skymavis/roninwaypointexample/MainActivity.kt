package com.skymavis.roninwaypointexample

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skymavis.roninwaypointexample.ui.theme.RoninWaypointExampleTheme
import com.skymavis.sdk.waypoint.Result
import com.skymavis.sdk.waypoint.Waypoint
import java.util.UUID

var redirectUri = "mydapp://callback";
val clientId = "71c2f8ff-0ebe-419d-acd3-c8ebd6d9e676"
//val clientId = "6fcda80b-6c9a-4c23-8923-60946d7f3817"
val gateOrigin = "https://waypoint.roninchain.com"
val chainId : Int = 2021;
val rpcUrl = "https://saigon-testnet.roninchain.com/rpc"
val testAddress = null

val waypoint = Waypoint(
    gateOrigin,
    clientId,
    rpcUrl,
    chainId
);

var TAG = "Waypoint Example Activity"

class MainActivity : ComponentActivity() {
    var state = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        val data: Uri? = intent?.data // Capture the URI data
        if(data != null) {
            waypoint.onResponse(data)
        }
        super.onCreate(savedInstanceState)

        setContent {
            RoninWaypointExampleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(96.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                         Image(
                    painter = painterResource(id = R.drawable.ronin),
                    contentDescription = "Centered Image",
                    modifier = Modifier.size(128.dp)
                )
                        Spacer(modifier = Modifier.height(16.dp))
                        AuthButton(context = this@MainActivity) {
                                newState -> state = newState
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        PersonalSignButton(context = this@MainActivity) {
                                newState -> state = newState
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        SignTypedDataButton(context = this@MainActivity) {
                                newState -> state = newState
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        SendTransactionButton(context = this@MainActivity) {
                                newState -> state = newState
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        CallContractButton(context = this@MainActivity) {
                                newState -> state = newState
                        }
                        ShowDialogResult(uri = data, state = state)
                    }
                }
            }
        }
    }
}

@Composable
fun ShowDialogResult(uri: Uri?, state: String) {
    var showDialog by remember { mutableStateOf(true) }

    uri?.let {
        var result = Result.from(uri)
        if (result.state == state) {
            showDialog = true
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Result") },
                text = {
                    Column {
                        Text("Is success: ${result.isSuccess}")
                        Text("Method: ${result.method}")
                        Text("Address: ${result.address}")
                        Text("Data: ${result.data}")
                        Text("State: ${result.state}")
                    }
                },
                confirmButton = { Button(onClick = { showDialog = false }) { Text("OK") } }
            )
        }
    }
}

@Composable
fun AuthButton( context: Context, onStateChange : (String) -> Unit) {
    Button(
        onClick = {
            val state = UUID.randomUUID().toString();
            waypoint.authorize(context, state, redirectUri).thenAccept { data ->
                if(data != null) {
                    Log.d(TAG, "Auth data : $data")
                } else {
                    print("No data received !")
                }
            }
            onStateChange(state)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Authorize")
    }

}

@Composable
fun CallContractButton( context: Context, onStateChange : (String) -> Unit) {
    Button(
        onClick = {
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
            onStateChange(state)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Call contract")
    }
}

@Composable
fun SendTransactionButton(context: Context, onStateChange : (String) -> Unit ) {
    Button(onClick = {
        val state = UUID.randomUUID().toString()
        val to = "0xD36deD8E1927dCDD76Bfe0CC95a5C1D65c0a807a";
        val value = "100000000000000000";

        waypoint.sendTransaction(context, state, redirectUri, to, value).thenAccept { data ->
            if(data != null) {
                Log.d(TAG, "Send transaction data : $data")
            } else {
                print("No data received !")
            }
        };
        onStateChange(state)
    },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Send Transaction")
    }
}

@Composable
fun PersonalSignButton(context: Context, onStateChange : (String) -> Unit) {
    Button(
        onClick = {

            val state = UUID.randomUUID().toString()
            val message = "Hello Axie";
            waypoint.personalSign(context, state, redirectUri, message).thenAccept { data ->
                if(data != null) {
                    Log.d(TAG, "Personal sign data : $data")
                } else {
                    print("No data received !")
                }
            }
            onStateChange(state)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Personal Sign")
    }
}

@Composable
fun SignTypedDataButton(context: Context, onStateChange : (String) -> Unit) {
    Button(onClick = {
        val state = UUID.randomUUID().toString()
        val typedData = """{"types":{"Asset":[{"name":"erc","type":"uint8"},{"name":"addr","type":"address"},{"name":"id","type":"uint256"},{"name":"quantity","type":"uint256"}],"Order":[{"name":"maker","type":"address"},{"name":"kind","type":"uint8"},{"name":"assets","type":"Asset[]"},{"name":"expiredAt","type":"uint256"},{"name":"paymentToken","type":"address"},{"name":"startedAt","type":"uint256"},{"name":"basePrice","type":"uint256"},{"name":"endedAt","type":"uint256"},{"name":"endedPrice","type":"uint256"},{"name":"expectedState","type":"uint256"},{"name":"nonce","type":"uint256"},{"name":"marketFeePercentage","type":"uint256"}],"EIP712Domain":[{"name":"name","type":"string"},{"name":"version","type":"string"},{"name":"chainId","type":"uint256"},{"name":"verifyingContract","type":"address"}]},"domain":{"name":"MarketGateway","version":"1","chainId":2021,"verifyingContract":"0xfff9ce5f71ca6178d3beecedb61e7eff1602950e"},"primaryType":"Order","message":{"maker":"0xd761024b4ef3336becd6e802884d0b986c29b35a","kind":"1","assets":[{"erc":"1","addr":"0x32950db2a7164ae833121501c797d79e7b79d74c","id":"2730069","quantity":"0"}],"expiredAt":"1721709637","paymentToken":"0xc99a6a985ed2cac1ef41640596c5a5f9f4e19ef5","startedAt":"1705984837","basePrice":"500000000000000000","endedAt":"0","endedPrice":"0","expectedState":"0","nonce":"0","marketFeePercentage":"425"}}""";
        waypoint.signTypedData(context, state, redirectUri, typedData).thenAccept { data ->
            if(data != null) {
                Log.d(TAG, "Sign typed data : $data")
            } else {
                print("No data received !")
            }
        }
        onStateChange(state)
    },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Sign Typed Data")
    }
}

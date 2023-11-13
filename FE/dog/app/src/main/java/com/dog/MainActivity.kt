package com.dog;

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dog.ui.theme.DogTheme
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DogApp { finish() }
                }
//                runBlocking {
////                     비동기 함수 호출을 runBlocking 블록 안에서 수행
//                    val response = HomeViewModel().loadBoarderNearData(127.11, 35.11, "test1")
//                    Log.d("model", response.body().toString())
//                }
//                ForumScreen()
            }
        }

        // 앱이 생성될 때 즉시 Firebase messaging token을 생성
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String> ->
                if (!task.isSuccessful) {
                    Log.w("FCM Log", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                Log.d("FCM Log", "Current token: $token")
            }


    }
}



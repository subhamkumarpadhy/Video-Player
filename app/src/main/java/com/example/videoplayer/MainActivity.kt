package com.example.videoplayer

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.example.videoplayer.repo.VideoFilesRepo
import com.example.videoplayer.screens.VideoListScreen
import com.example.videoplayer.ui.theme.VideoPlayerTheme
import com.example.videoplayer.utils.askPermissions
import com.example.videoplayer.utils.checkPermissions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val videoFilesRepo = VideoFilesRepo(this)
        GlobalScope.launch {
            videoFilesRepo.getVideoFile().collect{
                Log.d("MainActivity", "onCreate: $it")
            }
        }
        setContent {
            val isGranted = remember {
                mutableStateOf(false)
            }
            VideoPlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        val permission =
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                android.Manifest.permission.READ_MEDIA_VIDEO
                            } else {
                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                            }

                        isGranted.value = checkPermissions(permission)

                        if (isGranted.value) {
                            Navigator(screen = VideoListScreen(modifier = Modifier))
                        } else {
                            Button(
                                onClick = {
                                    askPermissions(
                                        permission,
                                        onPermissionGranted = {
                                            isGranted.value = true
                                        },
                                        onPermissionDenied = {
                                            isGranted.value = false
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Please Allow Permission",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        onPermissionsResultCallback = {

                                        }
                                    )
                                },
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text(text = "Allow Permission")
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.example.videoplayer.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.videoplayer.repo.VideoFilesRepo
import com.example.videoplayer.screens.components.VideoItem
import com.example.videoplayer.viewmodels.VideoViewModel

class VideoListScreen(private val modifier: Modifier) : Screen {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val viewModel = rememberScreenModel {
            VideoViewModel(VideoFilesRepo(context))
        }
        viewModel.getAllVideoFiles()
        val list by viewModel.state.collectAsState()

        if (list.isEmpty()) {
            Text(
                text = "No Videos Found",
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        } else {
            val navigator = LocalNavigator.currentOrThrow
            LazyColumn(modifier = modifier.fillMaxSize()) {
                items(list.size) {
                    VideoItem(videoFile = list[it], modifier = Modifier.padding(8.dp)) {
                        navigator.push(VideoPlayerScreen(list[it].pathUri))
                    }
                }
            }
        }
    }
}
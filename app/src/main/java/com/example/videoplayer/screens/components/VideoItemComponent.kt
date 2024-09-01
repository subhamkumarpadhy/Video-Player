package com.example.videoplayer.screens.components

import android.os.Build
import android.util.Size
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.videoplayer.R
import com.example.videoplayer.model.VideoFile
import com.example.videoplayer.utils.toFormattedSize
import com.example.videoplayer.utils.toFormattedTime

@Composable
fun VideoItem(
    modifier: Modifier = Modifier, videoFile: VideoFile,
    onClick: () -> Unit
) {
    Column(modifier = modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
        ) {
            Card(
                modifier = Modifier
                    .width(180.dp)
                    .fillMaxHeight()
            ) {
                Box(
                    modifier = Modifier.fillMaxHeight()
                ) {
                    val thumbnail =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            LocalContext.current.contentResolver.loadThumbnail(
                                videoFile.pathUri,
                                Size(640, 480),
                                null
                            )
                        } else {
                            null
                        }
                    if (thumbnail != null) {
                        Image(
                            modifier = Modifier.fillMaxHeight(),
                            contentScale = ContentScale.Crop,
                            bitmap = thumbnail.asImageBitmap(),
                            contentDescription = null
                        )
                    } else {
                        Image(
                            modifier = Modifier.fillMaxHeight(),
                            contentScale = ContentScale.Crop,
                            painter = painterResource(id = R.drawable.image_placeholder),
                            contentDescription = null
                        )
                    }
                    Text(
                        text = videoFile.duration.toFormattedTime(),
                        modifier = Modifier
                            .padding(8.dp)
                            .background(Color.Black)
                            .padding(horizontal = 4.dp)
                            .align(Alignment.BottomEnd),
                        color = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = videoFile.name,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = videoFile.size.toFormattedSize(),
                    modifier = Modifier
                        .padding(top = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        HorizontalDivider()
    }
}
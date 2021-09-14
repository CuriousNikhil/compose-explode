package me.nikhilchaudhari.explode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.nikhilchaudhari.explode.ui.theme.ParticlesTheme

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParticlesTheme {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(Color.Yellow),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center

                    ) {
                        val shape = CircleShape
                        Text(
                            text = "Test Button",
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier
                                .explodeOnClick(color = Color.Magenta, repeatable = true)
                                .padding(16.dp)
                                .background(Color.Magenta, shape)
                                .padding(16.dp)
                        )

                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(Color.Cyan)
                    ) {

                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(Color.LightGray)
                    ) {

                    }
                }
            }
        }
    }
}
//
//class Scene {
//
//    var sceneEntity = mutableStateListOf<SceneEntity>()
//    val stars = mutableListOf<Star>()
//
//    fun setupScene() {
//        sceneEntity.clear()
//        stars.clear()
//        repeat(800 * 5) { stars.add(Star()) }
//        sceneEntity.addAll(stars)
//    }
//
//    fun update() {
//        for (entity in sceneEntity) {
//            entity.update(this)
//        }
//    }
//
//    @Composable
//    fun render(frameState: State<Long>) {
//
//        Column(
//            modifier = Modifier.fillMaxSize()
//        ) {
//
//
//            Box {
//                Canvas(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(color = Color.Black),
//                ) {
//
//                    val frameTime = frameState.value
//                    val canvasWidth = size.width
//                    val canvasHeight = size.height
//                    val centerX = canvasWidth / 2
//                    val centerY = canvasHeight / 2
//
//                    for (star in stars) {
//                        val (headX, headY) = star.currentCoordinates
//                        val (tailX, tailY) = star.previousCoordinates
//
//                        drawLine(
//                            color = Color.White,
//                            start = Offset(centerX - tailX, centerY - tailY),
//                            end = Offset(centerX - headX, centerY - headY),
//                            strokeWidth = star.radius,
//                        )
//                    }
//
//                }
//
//            }
//
//        }
//
//
//    }
//}


@Composable
fun StepFrame(callback: () -> Unit): State<Long> {
    val millis = remember { mutableStateOf(0L) }
    LaunchedEffect(Unit) {
        val startTime = withFrameNanos { it }
        while (true) {
            withFrameMillis { frameTime ->
                millis.value = frameTime - startTime
            }
            callback.invoke()
        }
    }
    return millis
}
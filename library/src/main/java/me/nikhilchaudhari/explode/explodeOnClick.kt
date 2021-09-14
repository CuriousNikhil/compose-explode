package me.nikhilchaudhari.explode


import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.random.Random


const val PARTICLE_LENGTH = 15

@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.explodeOnClick(
    color: Color = Color.Black,
    durationMillis: Int = 1000,
    easing: Easing = LinearEasing,
    repeatable: Boolean = false
): Modifier = composed {
    val animationScope = rememberCoroutineScope()
    val factor = remember { Animatable(initialValue = 0f) }
    val alpha = remember { Animatable(initialValue = 0f) }

    val particles = arrayOfNulls<Particle>(PARTICLE_LENGTH * PARTICLE_LENGTH)

    this.then(
        onGloballyPositioned {
            val bounds = it.positionInParent()
            val random = Random(System.currentTimeMillis())
            for (i in 0 until PARTICLE_LENGTH) {
                for (j in 0 until PARTICLE_LENGTH) {
                    particles[(i * PARTICLE_LENGTH) + j] = generateParticle(
                        random,
                        it.size.height,
                        (bounds.x / 2).roundToInt(),
                        (bounds.y / 2).roundToInt(),
                    )
                }
            }
        }
            .drawWithContent {
                this.drawContent()
                for (particle in particles) {
                    particle?.let {
                        it.update(factor.value)
                        if (it.alpha > 0f) {
                            this.drawCircle(
                                color,
                                it.radius,
                                Offset(it.cx, it.cy)
                            )
                        }
                    }
                }
            }
            .alpha(1f - alpha.value)
            .clickable {
                animationScope.launch {
                    val result = async {
                        alpha.animateTo(
                            targetValue = 1f,
                            animationSpec = tween(durationMillis = durationMillis)
                        )
                    }
                    async {
                        factor.animateTo(
                            targetValue = 1.5f,
                            animationSpec = tween(durationMillis = durationMillis, easing = easing)
                        )
                    }
                    result.invokeOnCompletion {
                        if (repeatable) {
                            animationScope.launch {
                                alpha.snapTo(0f)
                                factor.snapTo(0f)
                            }
                        }
                    }
                }
            }
    )
}










package me.nikhilchaudhari.explode


import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.pow
import kotlin.random.Random


const val PARTICLE_LENGTH = 15
private val DENSITY = Resources.getSystem().displayMetrics.density
fun Int.dp2Px(): Float = (this * DENSITY)
private const val END_VALUE = 1.4f
private val X: Float = 5.dp2Px()
private val Y: Float = 20.dp2Px()
private val V: Float = 2.dp2Px()
private val W: Float = 1.dp2Px()


/**
 * Makes an explosion-ish animation on click
 *
 * @param color Set the color of the animation/particles, default is [Color.Black]
 * @param durationMillis Set the duration of animation, default is 1 sec
 * @param easing Set easing for the animation, default is [LinearEasing]
 * @param repeatable Set if you want to make the animation repeatable again after click
 * @param onClick Lambda will be called when user clicks the element
 *
 * Usage:
 * ```
 *  Text(
 *    text = text,
 *   modifier = Modifier.explodeOnClick()
 *   )
 * ```
 */
@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.explodeOnClick(
    color: Color = Color.Black,
    durationMillis: Int = 1000,
    easing: Easing = LinearEasing,
    repeatable: Boolean = false,
    onClick: (() -> Unit)? = null
): Modifier = composed {
    val animationScope = rememberCoroutineScope()
    // Advancement factor for a particle
    val factor = remember { Animatable(initialValue = 0f) }
    // Alpha to make the view invisible
    val alpha = remember { Animatable(initialValue = 0f) }
    // We don't want to compute particles over again so save the state to keep whether we've computed the required particles and positions.
    var isComputed by remember { mutableStateOf(false) }
    var height = 0
    var width = 0
    val particles = remember { arrayOfNulls<Particle>(PARTICLE_LENGTH * PARTICLE_LENGTH) }

    this.then(
        onGloballyPositioned {
            if (!isComputed) {
                height = it.size.height
                width = it.size.width
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
                    if (!isComputed) {
                        val random = Random(System.currentTimeMillis())
                        for (i in 0 until PARTICLE_LENGTH) {
                            for (j in 0 until PARTICLE_LENGTH) {
                                particles[(i * PARTICLE_LENGTH) + j] = generateParticle(
                                    random,
                                    height,
                                    width / 2,
                                    height / 2
                                )
                            }
                        }
                        isComputed = true
                    }
                    val result = async {
                        // Run the alpha animation
                        alpha.animateTo(
                            targetValue = 1f,
                            animationSpec = keyframes {
                                this.durationMillis = durationMillis
                                0.9f at durationMillis / 2
                            }
                        )
                    }
                    // Run the particle animation
                    async {
                        factor.animateTo(
                            targetValue = 1.5f,
                            animationSpec = tween(durationMillis = durationMillis, easing = easing)
                        )
                    }
                    // If it's repeatable then snap those Animatable values back to initial values,
                    // this is supposed to be done after the animation ends
                    result.invokeOnCompletion {
                        if (repeatable) {
                            this.launch {
                                alpha.snapTo(0f)
                                factor.snapTo(0f)
                            }
                        }
                    }
                }
                onClick?.invoke()
            }
    )
}

/**
 * Particle data class
 */
internal data class Particle(
    var alpha: Float = 0f,
    var color: Color = Color.Black,
    var cx: Float = 0f,
    var cy: Float = 0f,
    var radius: Float = 0f,
    var baseCx: Float = 0f,
    var baseCy: Float = 0f,
    var baseRadius: Float = 0f,
    var top: Float = 0f,
    var bottom: Float = 0f,
    var mag: Float = 0f,
    var neg: Float = 0f,
    var life: Float = 0f,
    var overflow: Float = 0f
) {

    fun update(factor: Float) {
        var f = 0f
        var normalization: Float = factor / END_VALUE
        if (normalization < life || normalization > 1f - overflow) {
            alpha = 0f
            return
        }
        normalization = (normalization - life) / (1f - life - overflow)
        val f2: Float = normalization * END_VALUE
        if (normalization >= 0.7f) {
            f = (normalization - 0.7f) / 0.3f
        }
        alpha = 1f - f
        f = bottom * f2
        cx = baseCx + f
        cy = (baseCy - neg * f.toDouble().pow(2.0)).toFloat() - f * mag
        radius = V + (baseRadius - V) * f2
    }
}

// Generate single particle
internal fun generateParticle(
    random: Random,
    height: Int, centerX: Int, centerY: Int
): Particle {
    val particle = Particle()
    particle.radius = V
    if (random.nextFloat() < 0.2f) {
        particle.baseRadius = V + (X - V) * random.nextFloat()
    } else {
        particle.baseRadius = W + (V - W) * random.nextFloat()
    }
    val nextFloat = random.nextFloat()
    particle.top = height * (0.18f * random.nextFloat() + 0.2f)
    particle.top = if (nextFloat < 0.2f) particle.top else particle.top + particle.top * 0.2f * random.nextFloat()
    particle.bottom = height * (random.nextFloat() - 0.5f) * 1.8f
    var f = if (nextFloat < 0.2f) particle.bottom else if (nextFloat < 0.8f) particle.bottom * 0.6f else particle.bottom * 0.3f
    particle.bottom = f
    particle.mag = 4.0f * particle.top / particle.bottom
    particle.neg = -particle.mag / particle.bottom
    f = centerX + Y * (random.nextFloat() - 0.5f)
    particle.baseCx = f
    particle.cx = f
    f = centerY + Y * (random.nextFloat() - 0.5f)
    particle.baseCy = f
    particle.cy = f
    particle.life = END_VALUE / 10 * random.nextFloat()
    particle.overflow = 0.4f * random.nextFloat()
    particle.alpha = 1f
    return particle
}

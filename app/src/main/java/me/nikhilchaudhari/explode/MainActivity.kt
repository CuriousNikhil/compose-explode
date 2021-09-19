package me.nikhilchaudhari.explode

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.nikhilchaudhari.explode.ui.theme.ParticlesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParticlesTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center

                    ) {
                        ExplodeText(text = "Click Me!", backColor = Color(0xFFe67e22))
                        ExplodeText(text = "Click Me!", backColor = Color(0xFF9b59b6))
                        ExplodeText(text = "Click Me!", backColor = Color(0xFF34495e))
                    }
                    ExplodeCard()
                }
            }
        }
    }
}


@Composable
fun ExplodeText(text: String, backColor: Color) {
    val context = LocalContext.current
    Text(
        text = text,
        style = TextStyle(
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .explodeOnClick(color = backColor,
                onClick = {
                    Toast
                        .makeText(context, "Toast msg", Toast.LENGTH_LONG)
                        .show()
                })
            .padding(16.dp)
            .background(backColor, CircleShape)
            .padding(16.dp)
    )
}


@Composable
fun ExplodeCard() {
    Card(
        modifier = Modifier
            .explodeOnClick(color = Color(0xFFc0392b), repeatable = true)
            .size(150.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.img), contentDescription = "launcher icon", modifier =
                Modifier.size(150.dp, 100.dp)
            )

            Text(
                text = "Click me!", style = TextStyle(
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ), modifier = Modifier.padding(8.dp)
            )
        }
    }
}

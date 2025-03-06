package sick.sick.fiftyfifty2

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun RouletteScreen() {
    val options = listOf("🍎", "🍌", "🍒", "🍇", "🎲", "⭐", "💰", "🎯")
    var selectedOption by remember { mutableStateOf("🎲") }
    var rotation by remember { mutableStateOf(0f) } // rotation
    var rotationAnim = remember { Animatable(0f) } // rotation animation

    Column(
       modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(250.dp)) // box för cirkel
        { // testar 200 istället för 250
            Canvas(modifier = Modifier.size(200.dp) // canvas för cirkel
                .rotate(rotationAnim.value) // roterar canvas)
            ) {
                val sliceAngle = 360f / options.size // slice angle
                options.forEachIndexed { index, option -> // loopar genom options
                    rotate(sliceAngle * index) { // roterar varje slice
                        drawCircle(color = if (index % 2 == 0) Color.Green else Color.Red, // färger
                            radius = size.minDimension / 2) // storlek
                    }
                }
            }
            Text(
                text = "🔻", // Pilar som pekar
            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
            modifier = Modifier.align(Alignment.TopCenter))
        }
        Spacer(modifier = Modifier.height(20.dp)) // mellanrum

        Button(onClick = {
            val targetRotation = Random.nextInt(5, 10) * 360 + (0 until 360).random()
            rotation = targetRotation.toFloat() // uppdaterar rotation

//            // Starta animationen
//            LaunchedEffect(rotation) {
//                rotationAnim.animateTo(
//                    targetValue = rotation,
//                    animationSpec = tween(durationMillis = 3000, easing = FastOutSlowInEasing)
//                )
//                selectedOption = options[((rotation / 360).roundToInt() % options.size)]
//            }
        }) {
            Text("Snurra!")
        }
        Spacer(modifier = Modifier.height(20.dp))

        Text("Resultat: $selectedOption", fontSize = MaterialTheme.typography.headlineLarge.fontSize)
    }

}



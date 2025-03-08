package sick.sick.fiftyfifty2

import android.R.attr.label
import android.R.attr.text
import android.R.attr.textStyle
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun RouletteScreen() {
    var options by remember { mutableStateOf(listOf("Option 1", "Option 2", "Option 3")) } // alternativ för roulette
    var selectedOption by remember { mutableStateOf<String?>(null)} // måste finnas null

    /**
     * Användaren kan skriva in alternativ i en textruta.
     * Hjulet snurrar och väljer en vinnare.
     * Resultatet visas efter snurrningen.
     */
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White), // sätt bakgrundsfärgen till svart
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RoulettWheel(options) { result -> // visa roulettehjulet
            selectedOption = result // sätt valda alternativ
        }
        Spacer(modifier = Modifier.height(16.dp)) // tom tomhet mellan roulettehjulet och resultat

        OutlinedTextField(
            value = options.joinToString(", "),
            onValueChange = { text ->
                options = text.split(",").filter { it.isNotBlank() }
            },
                label = { Text("Alternativ", color = Color.White) },
                //textStyle = TextStyle(color = Color.White)
                )

        selectedOption?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Result: $it", color = Color.White)

        }
    }

    if (selectedOption != null) {
        AlertDialog(
            onDismissRequest = { selectedOption = null },
            confirmButton = {
                Button(onClick = { selectedOption = null }) {
                    Text("OK")
                }
            },
            title = {
                Text("Resultat", color = Color.White)
            },
            text = {
                Text("Det valda alternativet är: $selectedOption", color = Color.White)
            },
            containerColor = Color.DarkGray
        )
    }
}

/**
 * Roulette Wheel
 *
 */
@Composable
fun RoulettWheel(options: List<String>, onResult: (String) -> Unit) {
    val rotation = remember { Animatable(0f) } // rotation av roulettehjulet
    val wheelSize = 300.dp // storlek på roulettehjulet
    val sectionCount = options.size // antal sektioner på roulettehjulet
    if (options.isEmpty()) return // om det är tomt så stängs appen)
    val anglePerSection = 360f / sectionCount // vinkel per sektion

    Box(
        modifier = Modifier
            .size(wheelSize)
            .rotate(rotation.value)
            .background(Color.White, shape = CircleShape), // bytt från darkgray till white
        contentAlignment = Alignment.Center) // centrera innehållet i boxen
    {
        Canvas(modifier = Modifier.size(wheelSize)) { // rita roulettehjulet på canvas
            for (i in options.indices) { // loopa igenom alla alternativ
                val startAngle = i * anglePerSection // startvinkel för varje sektion
                val sweepAngle = anglePerSection // vinkel för varje sektion todo ändra till 360/antal alternativ
                drawArc( // rita en båge för varje sektion
                    color = if (i % 2 == 0) Color.Red else Color.Black, // röd och svart sektioner
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true
                )
            }// )
        }
        Icon(
            imageVector = Icons.Default.ArrowDropDown, // ikon för att visa att man kan rotera roulettehjulet
            contentDescription = "Spin",
            modifier = Modifier.size(48.dp) // storlek på ikonen
                .align(alignment = Alignment.TopCenter)
        )
    }
    Spacer(modifier = Modifier.height(20.dp)) // tom tomhet mellan roulettehjulet och resultat


    val coroutineScope = rememberCoroutineScope()

    Button(onClick = {
        coroutineScope.launch {
            val randomRotation = (360 * 5) + (0..360).random() // Snurra 5 varv + slumpvinkel
            rotation.animateTo(
                targetValue = rotation.value + randomRotation,
                animationSpec = tween(durationMillis = 3000, easing = FastOutSlowInEasing)
            )

            val winningIndex = ((rotation.value % 360) / anglePerSection).toInt() % sectionCount
            onResult(options[winningIndex])
        }
    }) {
        Text("Snurra!")
    }
}
/**
 * ✅ Ett animerat snurrhjul
 * ✅ Användaren kan skriva in egna alternativ
 * ✅ Resultatet visas i en pop-up
 * ✅ Resultatet kan skickas vidare till MotorActivity
 */














package sick.sick.fiftyfifty2


import android.R.attr.textSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.nativeCanvas
import kotlin.math.roundToInt
import kotlin.random.Random

import android.graphics.Paint
import android.graphics.Color as AndroidColor

@Composable
fun RouletteScreen() {
   // var options by remember { mutableStateOf(listOf("Option 1", "Option 2", "Option 3")) } // alternativ för roulette
    //var options by remember { mutableStateOf<List<String>>(emptyList()) }
    var options by remember { mutableStateOf(listOf<String>()) } // för att kunna lägga till nya alternativ

    var selectedOption by remember { mutableStateOf<String?>(null)} // måste finnas null
    var textIn by remember { mutableStateOf("") } // för att skriva in nya alternativ @update

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.Black) // fixat svart igen
            .padding(16.dp), // Färg ska kanske vara svart senare lagt till padding också!
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Roulette", fontSize = 35.sp, color = Color.White, fontWeight = FontWeight.ExtraBold)

        Spacer(modifier = Modifier.height(16.dp)) // tom tomhet mellan rubrik och roulettehjulet

        // Nu gör vi textfält för att skriva in nya alternativ
        Row(
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = textIn,
                onValueChange = { textIn = it },
                label = { Text("Nytt alternativ", color = Color.White) },
                textStyle = TextStyle(color = Color.White), // ändra till vit
                modifier = Modifier.weight(1f) // för att det ska gå till höger i raden
            )
            Spacer(modifier = Modifier.width(8.dp)) // tom tomhet mellan textfält och knapp

            Button(
                onClick = {
                    if (textIn.isNotBlank()) { // om det är infyllt så skickas det vidare
                        options = options + textIn
                        textIn = "" // töm textfältet
                    }
                }
            ) {
                Text("Lägg till") // knapptext för att lägga till nytt alternativ
            }
        }
        Spacer(modifier = Modifier.height(16.dp)) // Space mellan textfältet och roulettehjulet

        // visa alternativ som en lista
        LazyColumn {
            items(options) { option ->  // För varje alternativ
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        ///.weight(1f) Vet inte ännu todo
                        .height(30.dp) // Ändra Todo
                        .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(option, color = Color.White)

                    IconButton(
                        onClick = { options = options.filter { it != option } }  // ✅ Fixad hantering
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Ta bort", tint = Color.Red)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp)) // tom tomhet mellan roulettehjulet och resultat

        //ROULETTEHJUL
        RoulettWheel(options) { result -> // visa roulettehjulet
            selectedOption = result // sätt valda alternativ
        }
        Spacer(modifier = Modifier.height(16.dp)) // tom tomhet mellan roulettehjulet och resultat

        selectedOption?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Result: $it", color = Color.White)

        }
    }

    // POPUP FÖR VISA RESULTATET TILL USEREN AFTER SPINNING THE WHEEL
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
    val anglePerSection = 360f / sectionCount.toFloat()
    //val anglePerSection = 360f / sectionCount // vinkel per sektion

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
                // Rita text i varje sektion todo
                val textAngle = startAngle + sweepAngle / 2f // vinkel för texten i varje sektion
                val radius = (size.minDimension / 3.5f) // avstånd från center till texten
                // roterar canvasen för att rita texten i rätt position
                drawContext.canvas.nativeCanvas.apply {
                    save() // spara canvasens nuvarande position
                    // Rotera runt hjulets mittpunkt
                    rotate(
                        textAngle,
                        size.width / 2f,
                        size.height / 2f
                    )

                    // Rita texten i canvasen
                    drawText(
                        options[i], // texten
                        size.width / 2f, // mitten av hjulet
                        size.height / 2f - radius, // flytta uppåt från center
                        Paint().apply {
                            color = AndroidColor.WHITE
                            textSize = 36f
                            textAlign = android.graphics.Paint.Align.CENTER
                            isFakeBoldText = true
                        }
                    )
                    restore() // återställ canvasens position till innan roteringen

                }

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
        if (options.isEmpty()) return@Button // Om tom , Fixat Så den Funkar :)
        coroutineScope.launch {
            val randomRotation = (360 * 5) + (0..360).random() // Snurra 5 varv + slumpvinkel
            rotation.animateTo(
                targetValue = rotation.value + randomRotation,
                animationSpec = tween(durationMillis = 3000, easing = FastOutSlowInEasing)
            )}

            val winningIndex = ((rotation.value % 360) / anglePerSection).toInt() % sectionCount
            onResult(options[winningIndex])

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














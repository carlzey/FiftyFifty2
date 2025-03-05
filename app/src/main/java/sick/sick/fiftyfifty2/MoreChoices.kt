package sick.sick.fiftyfifty2


import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel


/**
 *  ViewModel för att lagra valda alternativ
 *  mer info om vad som händer här
 *  lol lol lol
 */
@Composable
fun moreChoicesView() {

    var choices = remember { mutableStateListOf<String>() } // lagrar valda alternativ i en lista
    var inText = remember { mutableStateOf("") } // används för att lagra inmatade text
    var valdText = remember { mutableStateOf("") } // används för att visa valda text
    val context = LocalContext.current // Nytt::::::::::För att starta ny aktivitet

    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Välj fler alternativ", fontSize = 24.sp)

        TextField(
            value = inText.value,
            onValueChange = { inText.value = it }, // uppdaterar inText varje gång?
            label = { Text("Lägg till fler alternativ") }, // hint
            modifier = Modifier.padding(top = 8.dp) // Lägger till padding ovan todo fel ? padding
            // ska jag ha detta row?
        )
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 8.dp) // Lägger till padding ovan
            ) {
                Button(onClick = {
                    if (inText.value.isNotBlank()) { // om det är infyllt så skickas det vidare till valdText
                        choices.add(inText.value) // Lägger till inText i choices
                        //  choices = choices + inputText??====
                        inText.value = "" // tömmer inText efter varje klick
                        // inputText = "" // Nollställ fältet efter tillägg
                    }
                } ) {
                    Text("Lägg till") // knapptext för att lägga till fler alternativ
                } // slut på row
                Button(onClick = { choices.clear() }) { // tömmer lista
                    Text("Rensa") // knapptext för att ta bort alla alternativ???!!! todo
                }


            }
//        Column( // fix
//            modifier = Modifier.weight(1f),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
            // lista med valda alternativ
            choices.forEach { choice -> // loopa genom valda alternativ och visa dem
                Text(choice, fontSize = 50.sp, color = Red) // todo ???
            }
//        }
        //knapp slumpa och visa valda alternativ
        Button(
            onClick = {
                if (choices.isNotEmpty()) {
                    valdText.value = choices.random() // slumpmässigt valda alternativ
                    val selectedChoice = valdText.value // sparar valdText i selectedChoice
                    //starta aktivitet
                    val intent = Intent(context, Motor::class.java).apply {
                        putExtra("EXTRA_MESSAGE", selectedChoice) // skickar valda option till motor
                        putExtra("SOURCE", "MoreChoices") // SKA MED TILL MOTOR FÖR SKICKA RÄTT TIBLAKA!!!!!!!!!!!!
                    }
                    context.startActivity(intent) // starta motor med valda option som extra

                }
            },
            modifier = Modifier.padding(top = 16.dp) // todo ====????styledpadding

        ) {
            // go
            Text("Slumpa :D")
        }
        if (valdText.value.isNotBlank()) {
            Text("Valt alternativ: ${valdText.value}", fontSize = 24.sp, modifier = Modifier.padding(top = 16.dp))
        }
//        valdText?.let { // kolla om valdText inte är null
//            Text("Valt alternativ: $it", fontSize = 24.sp, modifier = Modifier.padding(top = 16.dp))
//        }
    }
}
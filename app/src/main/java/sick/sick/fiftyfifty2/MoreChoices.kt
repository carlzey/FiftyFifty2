package sick.sick.fiftyfifty2


import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import sick.sick.fiftyfifty2.history.HistoryViewModel


/**
 *  ViewModel för att lagra valda alternativ
 *  mer info om vad som händer här
 *  lol lol lol
 */
@SuppressLint("NewApi")
@Composable
fun moreChoicesView(viewModel: HistoryViewModel) {

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
            }) {
                Text("Lägg till") // knapptext för att lägga till fler alternativ
            } // slut på row
            Button(onClick = { choices.clear() }) { // tömmer lista
                Text("Rensa") // knapptext för att ta bort alla alternativ???!!! todo
            }
        }



        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(10.dp).fillMaxWidth()
                .padding(end = 5.dp)
        ) {
            choices.forEach { choice -> // loopa genom valda alternativ och visa dem
                Box(
                    modifier =
                    Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))

                ) {
                    Text(
                        choice, fontSize = 50.sp, color = Red, modifier = Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))

                    ) // todo ?
                }
            }

        }
//        }
            //knapp slumpa och visa valda alternativ
            Button(
                onClick = {
                    if (choices.isNotEmpty()) {
                        valdText.value = choices.random() // slumpmässigt valda alternativ
                        val selectedChoice = valdText.value // sparar valdText i selectedChoice

                        val currentDate = java.time.LocalDateTime.now().toString() // hämta nuvarande datum
                        viewModel.insertHistory("MoreChoices", choices.toString(), selectedChoice, currentDate) // spara resultat i databas

                        //starta aktivitet
                        val intent = Intent(context, Motor::class.java).apply {
                            putExtra(
                                "EXTRA_MESSAGE",
                                selectedChoice
                            ) // skickar valda option till motor
                            putExtra(
                                "SOURCE",
                                "MoreChoices"
                            ) // SKA MED TILL MOTOR FÖR SKICKA RÄTT TIBLAKA!!!!!!!!!!!!
                        }
                        context.startActivity(intent) // starta motor med valda option som extra

                    }
                },
                modifier = Modifier.padding(top = 16.dp) // todo ====????styledpadding

            ) {
                // go
                Text("Slumpa :D")
            }
        }
    }
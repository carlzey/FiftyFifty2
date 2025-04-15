package sick.sick.fiftyfifty2


import android.annotation.SuppressLint
import android.content.Intent
import android.view.RoundedCorner
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import sick.sick.fiftyfifty2.history.HistoryViewModel
import com.google.accompanist.flowlayout.FlowRow


/**
 *  ViewModel för att lagra valda alternativ
 *  mer info om vad som händer här
 *  lol lol lol
 *
 *  Todo så alternativen blir mindre samt längre blir .....
 *  Todo även så rutorna blir färgade boxes alternativen
 */
@SuppressLint("NewApi")
@Composable
fun moreChoicesView(viewModel: HistoryViewModel) {

    var choices = remember { mutableStateListOf<String>() } // lagrar valda alternativ i en lista
    var inText = remember { mutableStateOf("") } // används för att lagra inmatade text
    var valdText = remember { mutableStateOf("") } // används för att visa valda text
    val context = LocalContext.current // Nytt::::::::::För att starta ny aktivitet

    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp).padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("More Choices", fontSize = 50.sp)

        TextField(
            value = inText.value,
            onValueChange = { inText.value = it }, // uppdaterar inText varje gång?
            keyboardOptions = KeyboardOptions.Default.copy( // Stor bokstav
                capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Sentences
            ),
            label = { Text("Add Choices") }, // hint
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
                Text("Add") // knapptext för att lägga till fler alternativ
            } // slut på row
            Button(onClick = { choices.clear() }) { // tömmer lista
                Text("Clear") // knapptext för att ta bort alla alternativ???!!! todo
            }
        }
            //knapp slumpa och visa valda alternativ
            Button(
                onClick = {
                    if (choices.isNotEmpty()) {
                        valdText.value = choices.random() // slumpmässigt valda alternativ
                        val selectedChoice = valdText.value // sparar valdText i selectedChoice
                        val joined = choices.joinToString(",    ") // Gjort Lite Bredare!
                        val formatter = java.time.format.DateTimeFormatter.ofPattern("EEEE d MMMM yyyy, HH:mm") // formatterar tiden
                        val currentDate = java.time.LocalDateTime.now().format(formatter) // hämta nuvarande datum
                        //val currentDate = java.time.LocalDateTime.now().toString() // hämta nuvarande datum
                        viewModel.insertHistory("MoreChoices", joined, selectedChoice, currentDate) // spara resultat i databas

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
                modifier = Modifier.padding(top = 2.dp) // todo ====????styledpadding

            ) {
                // go
                Text("Randomize!")
            }

        // Testa Flowrow
        FlowRow(
            mainAxisSpacing = 8.dp,
            crossAxisSpacing = 8.dp,
            modifier = Modifier.padding(top = 8.dp) // Lägger till padding ovan
            ,

        ) //({  }

        /**
         * Flyttat så att alternativen ligger under för underlätta
         *
         */
//
//
            {
            choices.forEach { choice -> // loopa genom valda alternativ och visa dem
                Box(
                    modifier =
                    Modifier.background(MaterialTheme.colorScheme.primaryContainer).clip(RoundedCornerShape(12.dp))
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 12.dp)
                    // Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                ) {
                    Text(
                        choice, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary, modifier = Modifier.background(MaterialTheme.colorScheme.background.copy(alpha = 10F))

                    ) // todo ?
                }
            }

        }
        }
    }


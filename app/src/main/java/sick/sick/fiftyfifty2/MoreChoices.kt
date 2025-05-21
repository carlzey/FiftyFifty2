package sick.sick.fiftyfifty2


import android.R.attr.title
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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

    var choices = remember { mutableStateListOf<String>() } // Lagrar valda alternativ i en lista
    var inText = remember { mutableStateOf("") } // Används för att lagra inmatade text
    var valdText = remember { mutableStateOf("") } // Vald Text för att skicka till Motor
    val context = LocalContext.current // Kontext för att starta motor


    Box(modifier = Modifier.fillMaxSize()) {

        val scrollState = rememberScrollState()
        // Få rätt höjd för scrollaren och visa den
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-220).dp)
                .height(130.dp)
                .verticalScroll(scrollState, reverseScrolling = true)
        ) {

            // Flowrow för att visa Design Så det blir snyggt
            FlowRow(
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp,
            )
            {
                choices.forEach { choice -> // Loopa genom valda alternativ och visa dem

                    // För att få snygg design
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp),
                        tonalElevation = 2.dp // Snygghet hur det står
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            ),
                            verticalAlignment = Alignment.CenterVertically // bottom förut
                        ) {
                            Text(
                                choice,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            IconButton(
                                onClick = { choices.remove(choice) },
                                modifier = Modifier.size(16.dp)
                            ) {
                                Icon( // kanske ändra till Icons.Default.Close
                                    imageVector = androidx.compose.material.icons.Icons.Default.Close,
                                    contentDescription = "Remove",
                                    modifier = Modifier.size(12.dp),
                                    tint = Red
                                )
                            }

                        }
                    }
                }
            }
        }
    }
    // Ha den centralt och ligga där hela tiden så den inte försvinner
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(bottom = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = inText.value,
            onValueChange = { inText.value = it }, // Uppdaterar inText varje gång användaren skriver
            keyboardOptions = KeyboardOptions.Default.copy( // Stor bokstav
                capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Sentences
            ),
            label = { Text("Choice") }, // Hint
            modifier = Modifier.padding(top = 8.dp) // Lägger till padding ovan
        )
        Row( // kanske spaced by 8 dp horizontellt
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
                Text("Add Choice") // Lägg till
            } // Slut på row
            Button(onClick = { choices.clear() }) { // Tömmer lista
                Text("Clear")
            }
        }
            //knapp slumpa och skicka till Motor
            Button(
                onClick = {
                    if (choices.isNotEmpty()) {
                        valdText.value = choices.random() // Slumpmässigt valda alternativ
                        val selectedChoice = valdText.value // Sparar valdText i selectedChoice
                        val joined = choices.joinToString(",    ") // Bredd
                        val formatter = java.time.format.DateTimeFormatter.ofPattern("EEEE d MMMM yyyy, HH:mm")
                        val currentDate = java.time.LocalDateTime.now().format(formatter) // Hämta nuvarande datum
                        viewModel.insertHistory("MoreChoices", joined, selectedChoice, currentDate) // Spara resultat i Historik

                        //Starta aktivitet
                        val intent = Intent(context, Motor::class.java).apply {
                            putExtra(
                                "EXTRA_MESSAGE",
                                selectedChoice
                            ) // skickar valda option till motor
                            putExtra(
                                "SOURCE",
                                "MoreChoices"
                            ) // skickar vilken den kommer från till motor
                        }
                        context.startActivity(intent) // Startar motor med valda option som extra

                    }
                },
                modifier = Modifier.padding(top = 2.dp), // kanske ändra till 8
                enabled = choices.isNotEmpty() // Om choices inte är tom så är knappen inaktivt

            ) {
                Text("Randomize!")
            }
        }
    }


package sick.sick.fiftyfifty2

import android.content.Intent
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * setcontent ersätter setcontentview(r.layout.mainactivity)
         * xml
         *
         * vi använder compose istället för xml
         */
        setContent {
            FiftyFiftyView()
        }
    }
}

@Composable // Annotering för att den är en composable funktion
fun FiftyFiftyView() {
    // håller användarens inmatade texter
    var firstOption by remember { mutableStateOf("") } // State för första val
    var secondOption by remember { mutableStateOf("") } // State för andra val
    var errorText by remember { mutableStateOf("") } // används felmeddelande
    var selectedOption by remember { mutableStateOf("") } // lagrar valda alternativet
    // remember mutable state of - gör så att värderna uppdateras i compose
    val errorMessage = stringResource(id = R.string.error_text)
    //errorText = errorMessage

    //todo testa här om det fungerar
    val context = LocalContext.current // lol

    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp), // Lägger till padding runt kolumnen
        horizontalAlignment = Alignment.CenterHorizontally, // Centrerar horizontalt
        verticalArrangement = Arrangement.Center // Centrerar vertikalt
    ) {
        Text(text = stringResource(id = R.string.title_FiftyFifty), // Hämtar text från strings.xml
            fontSize = 50.sp, // storlek text
            modifier = Modifier.padding(top = 30.dp) // Lägger till padding ovan
            )
        /**
         * Två TextField används istället för EditText.
         * onValueChange = { option1 = it } – uppdaterar option1 varje gång användaren skriver.
         * label = { Text(...) } – sätter etiketten för fältet.
         * IME-actions (imeAction = ImeAction.Next/Done) – styr tangentbordets knappar.
         * fillMaxWidth() gör att de fyller bredden.
         */
        TextField(
            value = firstOption, // första valet
            onValueChange = { firstOption = it }, // uppdaterar första valet
            label = { Text(stringResource(id = R.string.first_Hint)) }, // hint
            //keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), // tangentbord
            modifier = Modifier.padding(top = 16.dp) // Lägger till padding ovan
        )
        TextField(
            value = secondOption, // second valet
            onValueChange = { secondOption = it }, // uppdaterar första valet
            label = { Text(stringResource(id = R.string.second_Hint)) }, // hint
            //keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done), // tangentbord
            modifier = Modifier.padding(top = 16.dp) // Lägger till padding ovan
        )
        Button(
            onClick = {
                if (firstOption.isBlank() || secondOption.isBlank()) { // om det är infyllt så skickas det vidare till selectedOption
                    errorText = errorMessage // felmeddelande
                } else {
                    val random1 = Random.nextInt(1, 100) // randomiserar
                    val random2 = Random.nextInt(1, 100)
                    // Det slumpmässiga valet som visas efter knapptryck
                    selectedOption = if (random1 > random2) firstOption else secondOption
                    //todo ändra till motor
                    //starta motor med valda option
                    val intent = Intent(context, Motor::class.java).apply {
                        putExtra("EXTRA_MESSAGE", selectedOption) // skickar valda option till motor
                    }
                    context.startActivity(intent) // starta motor med valda option som extra
                }
            },
            modifier = Modifier.padding(top = 16.dp) // Lägger till padding ovan
        ) {
            Text(text = stringResource(id = R.string.button)) // knapptext
        }

        if (errorText.isNotBlank()) {
            Text(errorText, color = MaterialTheme.colorScheme.error, fontSize = 18.sp)
        }
        if (selectedOption.isNotBlank()) {
            Text(
                text = "Valt alternativ: $selectedOption",
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 16.dp))

    }


}
}


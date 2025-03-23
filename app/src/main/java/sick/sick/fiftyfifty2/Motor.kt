package sick.sick.fiftyfifty2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Motor : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the selected option from the intent
        val selectedOption = intent.getStringExtra("EXTRA_MESSAGE") ?: "" // fixar fel
        val source = intent.getStringExtra("SOURCE") ?: "" // SÅ DEN VET VAR DEN KOMMER FRÅN

        setContent {
            // Set the content of the activity
        MotorView(selectedOption, source) // lagt till source
        }
    }
}

/**
 *
 */
@Composable
fun MotorView(selectedOption: String, source: String) { // Tar emot valda option +även source!
    val context = LocalContext.current


    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).padding(bottom = 100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Valt alternativ:", // Visar valda option
            fontSize = 50.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            selectedOption,
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.primary
        )

        // Dela upp? todo vad?
        Spacer(modifier = Modifier.height(24.dp))
       /**
         * OK
         * lägg till om flera senare
         * else if (source == "FiftyFifty") { // Om den kom från FiftyFifty
         */
        Button(
            onClick = {
                if (source == "MoreChoices") { // Om den kom från MoreChoices
                    val intent = Intent(context, MainActivity::class.java).apply {
                        putExtra("NAVIGATE_TO", "MoreChoices") // Lägger till extra för att gå till MoreChoices
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Tar bort tidigare aktiviteter
                    }
                    context.startActivity(intent) // Starta MoreChoices//tabort??????
                } // if
                else { // Om den kom från FiftyFifty
                    context.startActivity(Intent(context, MainActivity::class.java))
                }
            }
        ) {
            Text("Go back")
        }


    }


}
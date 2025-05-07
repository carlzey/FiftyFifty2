package sick.sick.fiftyfifty2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * MotorActivity för att visa valda alternativ
 * Här Visas valda alternativ samt en knapp för att gå tillbaka till där det kommer
 * ifrån.
 */
class Motor : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hämta valt alternativ från Intent
        val selectedOption = intent.getStringExtra("EXTRA_MESSAGE") ?: ""
        val source = intent.getStringExtra("SOURCE") ?: "" // Vilken activity den kom ifrån

        setContent {
        MotorView(selectedOption, source) // Val och Source skickade till MotorView
        }
    }
}
/**
 * MotorView För att visa valda alternativ
 * @param selectedOption - Valda alternativ
 * @param source - Var den kommer ifrån
 */
@Composable
fun MotorView(selectedOption: String, source: String) {
    val context = LocalContext.current // Kontext

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).padding(bottom = 100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(45.dp), // Runda kanten testar högt värde
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(
                selectedOption,
                fontSize = 35.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                fontFamily = androidx.compose.ui.text.font.FontFamily.SansSerif,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(35.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
       // Tillbaka knapp som kör if else till rätt activity
        Button(
            onClick = {
                if (source == "MoreChoices") { // Morechoices
                    val intent = Intent(context, MainActivity::class.java).apply {
                        putExtra("NAVIGATE_TO", "MoreChoices") // Lägger till extra för att gå till MoreChoices
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // Tar bort tidigare aktiviteter
                    }
                    context.startActivity(intent) // Starta MoreChoices
                }
                else { // Om den kom från FiftyFifty
                    context.startActivity(Intent(context, MainActivity::class.java))
                }
            }
        ) {
            Text(stringResource(R.string.motor_back)) // Tillbaka Knapp
        }
    }
}
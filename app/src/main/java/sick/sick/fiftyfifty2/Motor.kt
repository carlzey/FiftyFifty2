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

        setContent {
            // Set the content of the activity
        MotorView(selectedOption) //todo kan vara fel helt borta :D
        }

    }


}

/**
 *
 */
@Composable
fun MotorView(selectedOption: String) { // Tar emot valda option
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Valt alternativ:", // Visar valda option
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            selectedOption,
            fontSize = 32.sp,
            color = MaterialTheme.colorScheme.primary
        )
        // Dela upp? todo vad?
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Klicka för att gå tillbaka till Start
                context.startActivity(Intent(context, MainActivity::class.java))
            }
        ) {
            Text("Go back")
        }


    }


}
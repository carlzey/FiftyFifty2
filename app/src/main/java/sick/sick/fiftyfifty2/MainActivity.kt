package sick.sick.fiftyfifty2

import TestImeActionNext
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle


import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController // ny
import sick.sick.fiftyfifty2.history.HistoryDatabase
import sick.sick.fiftyfifty2.history.HistoryViewModel
import sick.sick.fiftyfifty2.history.HistoryViewModelFactory
import sick.sick.fiftyfifty2.history.Repository
import kotlin.getValue

import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * setcontent ersätter setcontentview(r.layout.mainactivity)
         * xml
         * todo större textruta så man ser texten vid längre
         * vi använder compose istället för xml
         */
        val navigateTo = intent.getStringExtra("NAVIGATE_TO") ?: "FiftyFifty"
        // 1️⃣ Skapa en instans av Room-databasen
        val database = HistoryDatabase.getDatabase(applicationContext)
        //     // 2️⃣ Skapa Repository som använder DAO från Room
        val repository = Repository(database.historyDao())
        // // 3️⃣ Skapa ViewModelFactory för att hantera ViewModel-instansiering
        val viewModelFactory = HistoryViewModelFactory(repository)
        //  // 4️⃣ Skapa ViewModel med Factory
        val historyViewModel: HistoryViewModel by viewModels { viewModelFactory }
        setContent {
            FiftyFiftyApp(startDestination = navigateTo, historyViewModel) // fixat startdestination
        }
    }
}

/**
 * FiftyFiftyView :
 * Här ser man FiftyFifty
 *  @param navController - Navcontroller för att hantera navigering och state
 *  @param viewModel - ViewModel för att hantera data
 *  @param errorText - Felmeddelande
 *  @param selectedOption - Valda alternativ
 *  @param firstOption - Första val
 *  @param secondOption - Andra val
 *  @param keyboardController - Tangentbordet
 *  @param secondFocusRequester - FocusRequester för andra textfältet
 *  @Composable betyder att den är en composable funktion
 */
@SuppressLint("NewApi")
@Composable // Annotering för composable
fun FiftyFiftyView(navController: NavController, viewModel: HistoryViewModel = viewModel()) {
    // håller användarens inmatade texter
    var firstOption by remember { mutableStateOf("") } // State för första val
    var secondOption by remember { mutableStateOf("") } // State för andra val
    var errorText by remember { mutableStateOf("") } // felmeddelande
    var selectedOption by remember { mutableStateOf("") } // lagrar valda alternativet
    // remember mutable state of - gör så att värderna uppdateras i compose
    val errorMessage = stringResource(id = R.string.error_text)
    //errorText = errorMessage

    val context = LocalContext.current // Kontext för att starta motor

    // TA BORT?
    val secondFocusRequester = remember { FocusRequester() } // gör så att tangentbordet försvinnerzz
    val keyboardController = LocalSoftwareKeyboardController.current // gör så att tangentbordet försvinnerzz

    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp).imePadding().padding(bottom = 100.dp), // Lägger till padding samt gör så att tangentbordet försvinnerzz
        horizontalAlignment = Alignment.CenterHorizontally, // Centrerar horizontalt
         verticalArrangement = Arrangement.Center,
    ) {
        Text(text = stringResource(id = R.string.title_FiftyFifty), // Hämtar text från strings.xml
            fontSize = 50.sp,
            modifier = Modifier.padding(top = 30.dp)
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
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Next,
                capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Sentences),
            modifier = Modifier.padding(top = 16.dp)
        )
        TextField(
            value = secondOption, // second valet
            onValueChange = { secondOption = it }, // uppdaterar andra valet
            label = { Text(stringResource(id = R.string.second_Hint)) }, // hint
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Done,
                capitalization = androidx.compose.ui.text.input.KeyboardCapitalization.Sentences),
            modifier = Modifier.padding(top = 16.dp)
        )
        Button(
            onClick = {
                if (firstOption.isBlank() || secondOption.isBlank()) { // Måste vara ifyllt för att gå vidare
                    errorText = errorMessage // Felmeddelande
                } else {
                    val random1 = Random.nextInt(1, 100) // Random number generator
                    val random2 = Random.nextInt(1, 100) // Random number generator
                    // Det slumpmässiga valet som sparas efter knapptryck
                    selectedOption = if (random1 > random2) firstOption else secondOption

                    // Spara resultat i databas
                    val options = "$firstOption vs $secondOption" // Valda alternativ som en sträng
                    val formatter = java.time.format.DateTimeFormatter.ofPattern("EEEE d MMMM yyyy, HH:mm") // Formatterar tiden
                    val currentdate = java.time.LocalDateTime.now().format(formatter) // Hämta nuvarande datum
                    viewModel.insertHistory("FiftyFifty", options, selectedOption, currentdate) // Spara resultat i databas

                    //Starta motor med valda option
                    val intent = Intent(context, Motor::class.java).apply {
                        putExtra("EXTRA_MESSAGE", selectedOption) // Skickar valda option till motor
                        putExtra("SOURCE", "FiftyFifty") // Ska med till motor för visa vilket den kommer ifån
                    }
                    context.startActivity(intent) // Starta Motor
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.button)) // Knapptext
        }
        // Todo behövs inte?
        if (errorText.isNotBlank()) {
            Text(errorText, color = MaterialTheme.colorScheme.error, fontSize = 18.sp)
        }
        if (selectedOption.isNotBlank()) {
           // Do nothing

    }



}
}

/**
 *  Bottom navigation bar
 *  "@Composable" betyder att det är en composable funktion
 *   @param startDestination - Startdestination för navigering
 *   @param historyViewModel - ViewModel för att hantera data till historik
 */
@Composable
fun FiftyFiftyApp(startDestination: String, historyViewModel: HistoryViewModel) {
    val navController = rememberNavController() // Navcontroller för att hantera navigering och state
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost( // Navhost är en container för navigering
            navController = navController, // Navcontroller för att hantera navigering och state
            startDestination = startDestination, // Start sidan är FiftyFifty
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("FiftyFifty") { FiftyFiftyView(navController, historyViewModel) }
            composable("MoreChoices") { MoreChoices(viewModel = historyViewModel) }
            //composable("Roulette") { Roulette() }
            composable("History") { HistoryScreen(viewModel = historyViewModel) }
            //composable("Inställningar") { SettingsScreen() }
        }
    }
}

/**
 *  Bottom navigation bar för att navigera mellan sidorna i appen med Material Design
 *  @param navController - Navcontroller för att hantera navigering och state
 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar { // Navigering Alternativ
        NavigationBarItem(
            icon = { Icon(Icons.Default.Balance, contentDescription = "FiftyFifty") }, // Ikon
            label = { Text("FiftyFifty") }, // Namn
            selected = navController.currentDestination?.route == "FiftyFifty", // Destinationen
            onClick = { navController.navigate("FiftyFifty") } // Navigerar till
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Tune, contentDescription = "MoreChoices") },
            label = { Text("MoreChoices") },
            selected = navController.currentDestination?.route == "MoreChoices",
            onClick = { navController.navigate("MoreChoices") }
        )
//        NavigationBarItem( Inaktiv!
//            icon = { Icon(painterResource(id = android.R.drawable.arrow_down_float), contentDescription = "Roulette") },
//            label = { Text("Roulette") },
//            selected = navController.currentDestination?.route == "Roulette",
//            onClick = { navController.navigate("Roulette") }
//        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.History, contentDescription = "History") },
            label = { Text("History") },
            selected = navController.currentDestination?.route == "History",
            onClick = { navController.navigate("History") }
        )
        /** Inaktiv!
        NavigationBarItem(
            icon = { Icon(painterResource(id = android.R.drawable.arrow_down_float), contentDescription = "Inställningar") },
            label = { Text("Inställningar") },
            selected = navController.currentDestination?.route == "Inställningar",
            onClick = { navController.navigate("Inställningar") }
        )
        */
    }
}
@Composable
fun MoreChoices(viewModel: HistoryViewModel) {
    Text(text = "MoreanChoices") // Todo Ha kvar?
    moreChoicesView(viewModel)
}
//@Composable Inaktiv!
//fun Roulette() {
//    Text(text = "Roulette")
//    RouletteScreen() // visa roulette i motor
//}
@Composable
fun HistoryScreen(viewModel: HistoryViewModel) {
    History(viewModel)
}
//@Composable Inaktiv!
//fun SettingsScreen() {
//    Text(text = "Roulette")
//    //SocialWindow() -  För socialfunktionen ...
//    //TestImeActionNext() // för imeaction
//}


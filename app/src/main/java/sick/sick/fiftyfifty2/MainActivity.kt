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
 *  @Composable betyder att den är en composable funktion
 *  fixat fel här navcontroller
 *  mer ? för composable funktion och navcontroller
 *  Kaffe?!
 */
@SuppressLint("NewApi")
@Composable // Annotering för att den är en composable funktion
fun FiftyFiftyView(navController: NavController, viewModel: HistoryViewModel = viewModel()) {
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

    // Behövs detta? todo kolla om det fungerar
    val secondFocusRequester = remember { FocusRequester() } // gör så att tangentbordet försvinnerzz
    val keyboardController = LocalSoftwareKeyboardController.current // gör så att tangentbordet försvinnerzz

    Column (
        modifier = Modifier.fillMaxSize().padding(16.dp).imePadding().padding(bottom = 100.dp), // Lägger till padding samt gör så att tangentbordet försvinnerzz
        horizontalAlignment = Alignment.CenterHorizontally, // Centrerar horizontalt
        //verticalArrangement = Arrangement.Center // Centrerar vertikalt
         verticalArrangement = Arrangement.Center,

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
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Next), // tangentbord
            modifier = Modifier.padding(top = 16.dp) // Lägger till padding ovan
        )
        TextField(
            value = secondOption, // second valet
            onValueChange = { secondOption = it }, // uppdaterar första valet
            label = { Text(stringResource(id = R.string.second_Hint)) }, // hint
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = androidx.compose.ui.text.input.ImeAction.Done), // tangentbord
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

                    // !!!Spara resultat i databas
                    val options = "$firstOption vs $secondOption" // valda alternativ som en sträng
                    val currentdate = java.time.LocalDateTime.now().toString() // hämta nuvarande datum
                    viewModel.insertHistory("FiftyFifty", options, selectedOption, currentdate) // spara resultat i databas


                    //starta motor med valda option
                    val intent = Intent(context, Motor::class.java).apply {
                        putExtra("EXTRA_MESSAGE", selectedOption) // skickar valda option till motor
                        putExtra("SOURCE", "FiftyFifty") // SKA MED TILL MOTOR FÖR SKICKA om man vill ha source
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

/**
 *  bottom navigation bar
 *  "@Composable" betyder att det är en composable funktion
 *   @param navController - Navcontroller för att hantera navigering och state
 */
@Composable
fun FiftyFiftyApp(startDestination: String, historyViewModel: HistoryViewModel) { // fixat ävenn viewmodel nu
    val navController = rememberNavController() // navigering  todo kolla om det fungerar
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost( // gör navigering
            navController = navController, // navcontroller för att hantera navigering och state
            startDestination = startDestination, // start sida är FiftyFifty
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("FiftyFifty") { FiftyFiftyView(navController, historyViewModel) }
            composable("MoreChoices") { MoreChoices(viewModel = historyViewModel) } // nya sida för val av alternativ i motor?
            composable("Roulette") { Roulette() } // roulette är en ny sida
            composable("History") { HistoryScreen(viewModel = historyViewModel) } //FIXAT! historik är en ny sida
            composable("Inställningar") { SettingsScreen() } // inställningar är en ny sida
        }
    }
}

/**
 *  bottom navigation bar för att navigera mellan sidorna i appen med Material Design
 *
 *
 *  Ändrat till NavigationbarItem istället för NavigationBarItem för att få det att fungera
 */
@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar { // navigering 1-3
        NavigationBarItem( // item 1
            icon = { Icon(painterResource(id = android.R.drawable.arrow_up_float), contentDescription = "FiftyFifty") }, // ikon
            label = { Text("FiftyFifty") }, // namn på sida
            selected = navController.currentDestination?.route == "FiftyFifty", // kolla om sidan är valda
            onClick = { navController.navigate("FiftyFifty") } // navigerar till FiftyFifty
        )
        // lägg till fler navigeringar här om det behövs
        NavigationBarItem(
            icon = { Icon(painterResource(id = android.R.drawable.arrow_down_float), contentDescription = "MoreChoices") },
            label = { Text("MoreChoices") },
            selected = navController.currentDestination?.route == "MoreChoices",
            onClick = { navController.navigate("MoreChoices") }
        )
        // lägg till fler navigeringar här om det behövs
        /**
        NavigationBarItem(
            icon = { Icon(painterResource(id = android.R.drawable.arrow_down_float), contentDescription = "Roulette") },
            label = { Text("Roulette") },
            selected = navController.currentDestination?.route == "Roulette",
            onClick = { navController.navigate("Roulette") }
        ) */
        NavigationBarItem(
            icon = { Icon(painterResource(id = android.R.drawable.arrow_down_float), contentDescription = "History") },
            label = { Text("History") },
            selected = navController.currentDestination?.route == "History",
            onClick = { navController.navigate("History") }
        )
        /**
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
    Text(text = "MoreanChoices")
    moreChoicesView(viewModel) // visa nya alternativ i motor
    /**
     * morechoices är en ny sida som visar alternativ som användaren kan välja fler gånger
     * med mer och mer alternativ som visas i en lista eller en grid.
     * Bra att skriva mer info om vad som händer här
     * Tack
     */
}

@Composable
fun Roulette() {
    Text(text = "Roulette")
    RouletteScreen() // visa roulette i motor
}

@Composable
fun HistoryScreen(viewModel: HistoryViewModel) {
    // todo skriv historik
    History(viewModel) // historik är en ny sida som visar resultat från körningar i en lista eller en grid.
}

@Composable
fun SettingsScreen() {
    Text(text = "Roulette")
    //SocialWindow() -  För socialfunktionen ...
    //TestImeActionNext() // för imeaction
}


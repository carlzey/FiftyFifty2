package sick.sick.fiftyfifty2

import android.R.attr.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items // Nytt
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import sick.sick.fiftyfifty2.history.HistoryEntity
import sick.sick.fiftyfifty2.history.HistoryViewModel

/**
 * Historia log där gamla resultat visas här och sparas i en databas
 * Rooms för att spara resultat och datum i en databas
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun History(viewModel: HistoryViewModel = viewModel()) {

    // fixade fel här collectasstate för att fånga värdet
    val historyList = viewModel.allHistory.collectAsState(initial = emptyList()).value

//    //val historyList = listOf(
//        HistoryItem("FiftyFifty", "1 vs 2", "1", "2023-05-15"),
//        HistoryItem("FiftyFifty2", "1 vs 2", "2", "2023-05-15"),
//        HistoryItem("MoreChoices", "1,2,3,4,5", "3", "2023-05-15"),
//        HistoryItem("Roulette", "sol,vatten,snö,gröt,katt", "katt", "2023-05-15")
//    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("History")}
                )

        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.clearHistory() }
            ) {
                Text("Rensa")
            }}

    ) { padding -> // Lägg till padding values
        LazyColumn( modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {
            items(historyList) { item -> // Loopa igenom historiklista och visa varje element
                HistoryItemView(item)
            }
        }

        }
    }

@Composable
fun HistoryItemView(item: HistoryEntity) { //
    Card (
        modifier = Modifier.padding(8.dp) // Lägg till padding mellan element
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = item.game, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(text = "Alternativ: ${item.options}")
            Text(text = "Valt: ${item.chosenOption}", fontWeight = FontWeight.Bold)
            Text(text = "Tidpunkt: ${item.date}", style = MaterialTheme.typography.labelSmall) // fix?
        }

         }
}

/**
 *  data class för att spara resultat och datum i en databas
 *  @sample HistoryItem("FiftyFifty", "1 vs 2", 1,  "2023-05-15")
 */
data class HistoryItem(val game: String, val options: String, val chosenOption: String, val date: String)


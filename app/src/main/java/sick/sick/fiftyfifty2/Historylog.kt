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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    // Kolla om vi har några resultat i databasen
    val historyList = viewModel.allHistory.collectAsState(initial = emptyList()).value

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
                Text("Clear")
            }}

    ) { padding ->
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

/**
 * HistoryItemView visar en rad i historiklista
 * Vi kör Card för att få en snygg Design
 *
 */
@Composable
fun HistoryItemView(item: HistoryEntity) {
    Card (
        modifier = Modifier.padding(8.dp) // Lägg till padding mellan element
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) { // Alternativ och Vald
            Text(text = item.game, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(text = item.options)

            Text(buildAnnotatedString {
                append("Fate chose ---> ") // Texten innan valda alternativet
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(item.chosenOption)
                }
            })
            Text(text = "Date: ${item.date}", style = MaterialTheme.typography.labelSmall) // Datum
        }

         }
}

/**
 *  Data class för att spara resultat och datum i en databas
 */
data class HistoryItem(val game: String, val options: String, val chosenOption: String, val date: String)


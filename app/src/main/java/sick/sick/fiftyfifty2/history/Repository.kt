package sick.sick.fiftyfifty2.history

import kotlinx.coroutines.flow.Flow

/**
 * Repository är en klass som hanterar kommunikation mellan Room-databasen och ViewModel.
 * 🔹 Repository är ett lager mellan databasen och UI.
 */
class Repository(private val historyDao: HistoryDao) {
    val allHistory: Flow<List<HistoryEntity>> = historyDao.getAllHistory() // Hämtar all Historik

    // Lägger till
    suspend fun insert(historyEntity: HistoryEntity) {
        historyDao.insert(historyEntity) // Insert
    }
    // Rensar databasen och tar bort i historiken
    suspend fun clearHistory() {
        historyDao.clearHistory()
    }
}
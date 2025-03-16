package sick.sick.fiftyfifty2.history

import kotlinx.coroutines.flow.Flow

/**
 * Repository är en klass som hanterar kommunikation mellan Room-databasen och ViewModel.
 * 🔹 Repository är ett lager mellan databasen och UI.
 */
class Repository(private val historyDao: HistoryDao) {
    val allHistory: Flow<List<HistoryEntity>> = historyDao.getAllHistory() // hämtar alla post från databasen i ordning

    // infogar  en ny post till databasen
    suspend fun insert(historyEntity: HistoryEntity) {
        historyDao.insert(historyEntity) // infogar  en ny post till databasen
    }

    // rensar databasen och tar bort alla post
    suspend fun clearHistory() {
        historyDao.clearHistory() // rensar databasen och tar bort alla post
    }
}
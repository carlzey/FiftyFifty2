package sick.sick.fiftyfifty2.history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


/**
 * Dao är en Data Access Object som används för att interagera med Room-databasen.
 * Mer info om Dao och Room här: https://developer.android.com/training/data-storage/room/accessing-data
 *
 * 🔹 Flow gör att UI uppdateras automatiskt när databasen ändras.
 * 🔹 clearHistory() rensar hela databasen.
 */
@Dao
interface HistoryDao {
    // Lägger till en historyEntity i databasen
    @Insert
    suspend fun insert(historyEntity: HistoryEntity)

    // Hämtar alla historyEntity från databasen och sorterar dem efter id i fallande ordning
    @Query("SELECT * FROM history_table ORDER BY id DESC")
    fun getAllHistory(): Flow<List<HistoryEntity>>

    // Rensar hela databasen
    @Query ("DELETE FROM history_table")
    suspend fun clearHistory()
}
package sick.sick.fiftyfifty2.vote

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


// DAO för att hantera röster i databasen
@Dao
interface VoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vote: Vote) // Lägger till en röst i databasen

    // hämtar alla röster  per alternativ  för att specifikt val todo kolla choiceid om de ska vara :
    @Query("SELECT option, COUNT(*) as VoteCount FROM Vote WHERE choiceID = :choiceID GROUP BY option")
    fun getVotesResults(choiceID: Int): Flow<List<VoteResult>> // Hämtar alla röster per alternativ för ett val
}

// Dataklass för att returnera resultaten av röstningen
data class VoteResult(val option: String, val VoteCount: Int)
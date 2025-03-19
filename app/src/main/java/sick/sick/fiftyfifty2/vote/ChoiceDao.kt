package sick.sick.fiftyfifty2.vote

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Dao för att hantera val i databasen för att kunna spara val
// choice = question class
// todo Ändra till Question namn i fil???
@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: Question) // lägger till en val i databasen

    @Query ("SELECT * FROM Question ORDER BY createdAt DESC") // Lägger till ett val i databasen
    fun getAllQuestions(): Flow<List<Question>> // hämtar alla val från databasen fallande efter skapande
}
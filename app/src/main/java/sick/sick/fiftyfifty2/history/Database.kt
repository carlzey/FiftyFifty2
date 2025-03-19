package sick.sick.fiftyfifty2.history

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sick.sick.fiftyfifty2.vote.Converters
import sick.sick.fiftyfifty2.vote.Question
import sick.sick.fiftyfifty2.vote.VoteDao // Importerar VoteDao
import sick.sick.fiftyfifty2.vote.QuestionDao // Importerar QuestionDao
import sick.sick.fiftyfifty2.vote.Vote

/**
 * HistoryDatabase är en abstrakt klass som ärver från RoomDatabase.
 * Den används för att skapa en singleton-instans av Room-databasen.
 * och vad mer info om databasen och Room här: https://developer.android.com/training/data-storage/room
 *
 * Nu lägger vi till Vote och choice i databasen. också för röstningsfunktioner
 */
//@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
@Database(entities = [HistoryEntity::class, Question::class, Vote::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class) // gör det möjligt att lagra List<String> i Room databasen
abstract class HistoryDatabase : RoomDatabase() { // abstrakt klass som ärver från RoomDatabase
    abstract fun historyDao(): HistoryDao // Dao för historik
    abstract fun voteDao(): VoteDao // Dao för röster
    abstract fun questionDao(): QuestionDao // Dao för val Community Choices


    companion object {
        @Volatile
        private var INSTANCE: HistoryDatabase? = null // en instans av HistoryDatabase

        /**
         * Funktion som returnerar en instans av databasen.
         * @param context Applikationskontexten.
         * @return En singleton-instans av databasen.
         */
        fun getDatabase(context: Context): HistoryDatabase { // FIX: Lagt till returtyp
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryDatabase::class.java,
                    "history_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
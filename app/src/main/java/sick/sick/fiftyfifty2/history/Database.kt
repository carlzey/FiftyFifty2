package sick.sick.fiftyfifty2.history

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * HistoryDatabase är en abstrakt klass som ärver från RoomDatabase.
 * Den används för att skapa en singleton-instans av Room-databasen.
 * och vad mer info om databasen och Room här: https://developer.android.com/training/data-storage/room
 */
@Database(entities = [HistoryEntity::class], version = 1, exportSchema = false)
abstract class HistoryDatabase : RoomDatabase() { // abstrakt klass som ärver från RoomDatabase
    abstract fun historyDao(): HistoryDao // abstrakt funktion som returnerar en HistoryDao-instans

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
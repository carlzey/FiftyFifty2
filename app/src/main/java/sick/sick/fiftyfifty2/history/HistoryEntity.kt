package sick.sick.fiftyfifty2.history

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * En Entity är en datamodell som representerar en tabell i Room-databasen.
 * Definerar en klass som ärver från RoomEntity och representerar en tabell i Room-databasen.
 *
 * history_table är namnet på tabellen i Room-databasen.
 * mer info om @Entity och @PrimaryKey här: https://developer.android.com/training/data-storage/room/defining-data
 *
 * 🔹 @Entity säger åt Room att detta är en tabell.
 * 🔹 @PrimaryKey(autoGenerate = true) skapar ett unikt ID automatiskt för varje rad.
 */
@Entity(tableName = "history_table")
data class HistoryEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // autoincrementPrimaryKey för att generera unika id för varje rad i tabellen.
    val game: String, // game är varifrån spelet hämtats från (FiftyFifty, FiftyFifty2, MoreChoices, Roulette)
    val options: String, // options är alternativen som spelaren kan välja
    val chosenOption: String, // chosenOption är det som genererades av AI-modellen
    val date: String // date är tiden spelet valdes och genererades
)

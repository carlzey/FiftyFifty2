package sick.sick.fiftyfifty2.vote

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Röstning
 * Vi kan börja med Room Database (lokal databas) för testning, och om du vill kan vi sen koppla den till en online-databas (Firebase eller MySQL).
 */
@Entity  // Entity som representerar en användares röst på ett alternativ
data class Vote(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // AutoincrementPrimaryKey för att generera unika id för varje rad i tabellen.
    val choiceID: Int, // kopplar rösten till en val genom dess id
    val option: String, // Alternativet användaren röstat på
    val votedAt: Long = System.currentTimeMillis() // Tid när rösten registrerades
)
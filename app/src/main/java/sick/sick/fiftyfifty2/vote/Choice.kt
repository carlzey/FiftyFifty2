package sick.sick.fiftyfifty2.vote

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


/**
 * Databasklass för att representera en fråga.
 * Detta representerar en val fråga
 * Vi kan börja med Room Database (lokal databas) för testning, och om du vill kan vi sen koppla den till en online-databas (Firebase eller MySQL).
 */
@Entity             // en entitet i databasen som betyder
// Entity som representerar ett val med en fråga och flera alternativ
                    // att den är en tabell i databasen
@TypeConverters(Converters::class) // Gör det möjligt att lagra List<String> i Room databasen
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, // Primärsnyckeln för raden i databasen som autogenereras
    val question: String, // Frågan "Vad ska jag äta"
    val options: List<String>, // Svaralternativen i lista
    val createdAt: Long = System.currentTimeMillis() // Tid när frågan skapades
)
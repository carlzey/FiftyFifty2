package sick.sick.fiftyfifty2.vote

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 *  Converterar för att omvandla
 *  till en json-sträng och tvärtom
 */
class Converters {
    @TypeConverter

    fun fromList(value: List<String>): String {
        // Konverterar listan till en JSON-sträng
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toList(value: String): List<String> { // Konverterar JSON-strängen till en lista
        val listType = object : TypeToken<List<String>>() {}.type // gör en ny typ
        return Gson().fromJson(value, listType) // konverterar json-strängen tillbaka till en lista
    }

}

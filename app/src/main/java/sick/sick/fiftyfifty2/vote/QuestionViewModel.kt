package sick.sick.fiftyfifty2.vote

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


// Todo kanske ändrar till @Hilt senare

// ViewModel för att hantera val och röstning i Community Choices
class QuestionViewModel(
    private val questionDao: QuestionDao, // Dao för val
    private val voteDao: VoteDao) // Dao för röster
    : ViewModel() {


    init { // kör när ViewModel skapas
        viewModelScope.launch {
            val existingQuestions = questionDao.getAllQuestions().firstOrNull() ?: emptyList()
            if (existingQuestions.isEmpty()) {
                questionDao.insert(
                    Question(
                        question = "Vad ska vi äta?",
                        options = listOf("Pizza", "Sushi", "Burgare")
                    )
                )

                android.util.Log.d("DEBUG", "En testfråga har lagts till i databasen.")
            } else {
                android.util.Log.d("DEBUG", "Databasen innehåller redan frågor.")
            }
        }
    }


        // hämtar alla val och lagrar som StateFlow
        // Flow används för att UI automatiskt uppdateras när val läggs till
        val questions: StateFlow<List<Question>> = questionDao.getAllQuestions().
                stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // funktion för att lägga till ett nytt val i databasen
    // Question Options(lista) är en val med en fråga och en lista med alternativ
    fun addChoice(question: String, options: List<String>) {
        viewModelScope.launch { // startar en ny coroutine i bakgrunden
            // Så inte de är tomma fält !options.all är för ta med alla i listan! ifyllt
            if (question.isNotBlank() && options.all { it.isNotBlank() }) {
                // lägger till frågan i databasen (questionDao) och val i databasen (voteDao)
                questionDao.insert(Question(question = question, options = options))
            }


            //questionDao.insert(choice)
        }
    }
    // funktion för att lägga till en röst på alternativet i databasen
    fun vote(choiceID: Int, option: String) {
        viewModelScope.launch { // kör databasanropet i en coroutine
            voteDao.insert(Vote(choiceID = choiceID, option = option)) // lägger till röst på valda alternativet
        }
    }

    /**
     * Funktion för att hämta resultaten av en röstning
     * Returnerar en Flow av List<VoteResult> ett map med alternativ som nycklar
     * och antal röster som värden
     */
    fun getVotesResults(choiceID: Int): Flow<Map<String, Int>> {
        return voteDao.getVotesResults(choiceID).map { // Hämtar röstningsresultat från databasen
            results -> results.associateBy({ it.option }, { it.VoteCount }) } // Gör om listan till en map
    }
} // slut på class

/**
 * Factory behövs för att skapa en instans av ViewModel
 * Då vi skippar hilt gör vi det manuellt här
 * Todo Kanske lägger till Hilt senare så det blir enklare (Ska underlätta)
 *
 */
class QuestionViewModelFactory(
    private val questionDao: QuestionDao, // Dao för val
    private val voteDao: VoteDao // Dao för röster
) : ViewModelProvider.Factory { // Får aldrig vara i samma som ViewModel() så ny klass såklart

    override fun <T : ViewModel> create(modelClass: Class<T>): T { // metoden som skapar en instans av ViewModel
        if (modelClass.isAssignableFrom(QuestionViewModel::class.java)) { // kollar om vi försöker skapa QuestionViewModel
            @Suppress("UNCHECKED_CAST") // Suppressar warning om unchecked cast
            return QuestionViewModel(questionDao, voteDao) as T // skapar en instans av QuestionViewModel
        }
        throw IllegalArgumentException("Unknown ViewModel class") //Om fel klass begärs, kasta ett undantag
    }
}

package sick.sick.fiftyfifty2.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 *  🔹 ViewModel håller kvar data vid rotation av skärmen.
 *  Vad den gör ? https://developer.android.com/topic/libraries/architecture/viewmodel
 *
 *  info om viewModelScope och coroutines här: https://developer.android.com/kotlin/coroutines/coroutines-adv
 *
 *  så det är en viewModel som ärver från ViewModel
 */
class HistoryViewModel(private val repository: Repository) : ViewModel() {
    val allHistory = repository.allHistory // All historik från databasen

    /**
     * InsertHistory skapar en HistoryEntity och sparar den i Room-databasen.
     * Lägger till
     *
     * Den körs i en viewModelScope.launch så att den inte blockerar UI.
     * inskick history till databasen och körs i en coroutine
     */
    fun insertHistory(game: String, options: String, chosenOption: String, date: String){
        val historyEntity = HistoryEntity(
            game = game,
            options = options,
            chosenOption = chosenOption,
            date = date)
        // Launch
        viewModelScope.launch {
            repository.insert(historyEntity)
        }
    }
    // Infogar  till databasen och körs i en coroutine
    fun insert(historyEntity: HistoryEntity) = viewModelScope.launch {
        repository.insert(historyEntity)
    }
    // Rensar databasen
    fun clearHistory() = viewModelScope.launch {
        repository.clearHistory()
    }
    }

/**
 * HistoryViewModelFactory skapar en HistoryViewModel.
 * Factory för HistoryViewModel
 * Den gör det så att vi kan skicka in repository till viewModel
 */
class HistoryViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
package com.example.alabi_toluwaleke_mad411_assignmentss



import android.app.Application
import kotlinx.coroutines.withContext
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class HabitViewModel(application: Application) : AndroidViewModel(application) {


    private val prefs = application.getSharedPreferences("habit_prefs", Context.MODE_PRIVATE)

    private val gson = Gson()
    private val FILE_NAME = "habits.json"


    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits

    private var nextId = 0

    private val _displayName = MutableStateFlow(prefs.getString("user_name", "") ?: "")
    val displayName: StateFlow<String> = _displayName


    private val _quoteUiState = MutableStateFlow<QuoteUiState>(QuoteUiState.Loading)
    val quoteUiState: StateFlow<QuoteUiState> = _quoteUiState

    init {
        viewModelScope.launch {
            val cached = withContext(Dispatchers.IO) {
                loadHabitsFromFile()
            }
            if (cached.isNotEmpty()) {
                _habits.value = cached
                nextId = (cached.maxOfOrNull { it.id } ?: 0) + 1
            }
        }
        fetchQuote()
    }




    // adds a new habit to the list
    fun addHabit(name: String) {
        _habits.value = _habits.value + Habit(id = nextId, name = name)
        nextId++
        saveHabitsToFile()
    }

    // toggles a habit between complete and not complete
    fun toggleComplete(habitId: Int) {
        _habits.value = _habits.value.map { habit ->
            if (habit.id == habitId) habit.copy(isCompleted = !habit.isCompleted)
            else habit
        }
        saveHabitsToFile()
    }

    // removes a habit from the list
    fun deleteHabit(habitId: Int) {
        _habits.value = _habits.value.filter { it.id != habitId }
        saveHabitsToFile()
    }


    fun fetchQuote() {
        viewModelScope.launch {
            _quoteUiState.value = QuoteUiState.Loading
            try {
                val result = RetrofitInstance.api.getRandomQuote()
                if (result.isNotEmpty()) {
                    _quoteUiState.value = QuoteUiState.Success(result[0])
                } else {
                    _quoteUiState.value = QuoteUiState.Error("Empty response from server")
                }
            } catch (e: Exception) {
                _quoteUiState.value = QuoteUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    fun saveName(name: String) {
        prefs.edit().putString("user_name", name).apply()
        _displayName.value = name
    }


    private fun saveHabitsToFile() {
        viewModelScope.launch(Dispatchers.IO) {
            val json = gson.toJson(_habits.value)
            File(getApplication<Application>().filesDir, FILE_NAME).writeText(json)
        }
    }

    private fun loadHabitsFromFile(): List<Habit> {
        val file = File(getApplication<Application>().filesDir, FILE_NAME)
        if (!file.exists()) return emptyList()

        val json = file.readText()
        val type = object : TypeToken<List<Habit>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

}



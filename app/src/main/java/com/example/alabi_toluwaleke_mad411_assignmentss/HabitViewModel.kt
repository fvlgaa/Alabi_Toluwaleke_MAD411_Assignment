package com.example.alabi_toluwaleke_mad411_assignmentss



import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HabitViewModel : ViewModel() {


    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits

    private var nextId = 0

    // adds a new habit to the list
    fun addHabit(name: String) {
        _habits.value = _habits.value + Habit(id = nextId, name = name)
        nextId++
    }

    // toggles a habit between complete and not complete
    fun toggleComplete(habitId: Int) {
        _habits.value = _habits.value.map { habit ->
            if (habit.id == habitId) habit.copy(isCompleted = !habit.isCompleted)
            else habit
        }
    }

    // removes a habit from the list
    fun deleteHabit(habitId: Int) {
        _habits.value = _habits.value.filter { it.id != habitId }
    }
}
package com.example.alabi_toluwaleke_mad411_assignmentss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.alabi_toluwaleke_mad411_assignmentss.ui.theme.Alabi_Toluwaleke_MAD411_AssignmentssTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Alabi_Toluwaleke_MAD411_AssignmentssTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HabitTrackerApp()
                }
            }
        }
    }
}

// data class for habits
data class Habit(
    val id: Int,
    val name: String,
    val isCompleted: Boolean = false
)

@Composable
fun HabitTrackerApp() {
    // state variables
    val habitList = remember { mutableStateListOf<Habit>() }
    var inputText by remember { mutableStateOf("") }
    var nextId by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // header section
        Text(
            text = "Student Habit Tracker",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Divider(
            thickness = 2.dp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // input section
        HabitInputSection(
            text = inputText,
            onTextChange = { inputText = it },
            onAddClick = {
                if (inputText.isNotBlank()) {
                    habitList.add(Habit(id = nextId, name = inputText))
                    nextId++
                    inputText = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // list section
        if (habitList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No habits yet. Add one above!",
                    color = Color.Gray
                )
            }
        } else {
            HabitListSection(
                habits = habitList,
                onCompleteClick = { habitId ->
                    val index = habitList.indexOfFirst { it.id == habitId }
                    if (index != -1) {
                        val currentHabit = habitList[index]
                        habitList[index] = currentHabit.copy(isCompleted = !currentHabit.isCompleted)
                    }
                }
            )
        }
    }
}

@Composable
fun HabitInputSection(
    text: String,
    onTextChange: (String) -> Unit,
    onAddClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text("Enter a new habit") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("e.g., Drink water") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onAddClick,
            modifier = Modifier.align(Alignment.End),
            enabled = text.isNotBlank()
        ) {
            Text("Add Habit")
        }
    }
}

@Composable
fun HabitListSection(
    habits: List<Habit>,
    onCompleteClick: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(habits) { habit ->
            HabitItemRow(
                habit = habit,
                onButtonClick = { onCompleteClick(habit.id) }
            )
        }
    }
}

@Composable
fun HabitItemRow(
    habit: Habit,
    onButtonClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = habit.name,
                fontSize = 18.sp,
                textDecoration = if (habit.isCompleted) {
                    TextDecoration.LineThrough
                } else {
                    TextDecoration.None
                },
                color = if (habit.isCompleted) Color.Gray else Color.Black
            )

            Button(
                onClick = onButtonClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (habit.isCompleted)
                        Color(0xFF4CAF50)
                    else
                        Color(0xFF2196F3)
                )
            ) {
                Text(
                    text = if (habit.isCompleted) "Done!" else "Complete",
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Alabi_Toluwaleke_MAD411_AssignmentssTheme {
        HabitTrackerApp()
    }
}
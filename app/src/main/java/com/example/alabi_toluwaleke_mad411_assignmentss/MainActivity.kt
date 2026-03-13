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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.alabi_toluwaleke_mad411_assignmentss.ui.theme.Alabi_Toluwaleke_MAD411_AssignmentssTheme
import kotlinx.coroutines.launch


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
fun HabitTrackerApp(viewModel: HabitViewModel = viewModel()) {
    // grab the habit list from the viewmodel
    val habitList by viewModel.habits.collectAsStateWithLifecycle()
    var inputText by remember { mutableStateOf("") }

    // needed for the snackbar
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        // fab replaces the old add button
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.addHabit(inputText)
                        inputText = ""
                        scope.launch {
                            snackbarHostState.showSnackbar("Habit added!")
                        }
                    }
                }
            ) {
                Text("+")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                onTextChange = { inputText = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // list section
            if (habitList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No habits yet. Add one above!", color = Color.Gray)
                }
            } else {
                HabitListSection(
                    habits = habitList,
                    onCompleteClick = { viewModel.toggleComplete(it) },
                    onDeleteClick = { viewModel.deleteHabit(it) }
                )
            }
        }
    }
}

@Composable
fun HabitInputSection(
    text: String,
    onTextChange: (String) -> Unit
) {
    // if its blank its an error simple as that
    val isError = text.isBlank()

    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text("Enter a new habit") },
        placeholder = { Text("e.g., Drink water") },
        isError = isError,
        modifier = Modifier.fillMaxWidth(),
        supportingText = {
            if (isError) Text("Habit name cannot be empty", color = Color.Red)
        }
    )
}

@Composable
fun HabitListSection(
    habits: List<Habit>,
    onCompleteClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit
) {
    // lazy column so it only loads what's on screen
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(habits, key = { it.id }) { habit ->
            HabitItemRow(
                habit = habit,
                onButtonClick = { onCompleteClick(habit.id) },
                onDeleteClick = { onDeleteClick(habit.id) }
            )
        }
    }
}

@Composable
fun HabitItemRow(
    habit: Habit,
    onButtonClick: () -> Unit,
    onDeleteClick: () -> Unit
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
            // strike through and grey it out when done
            Text(
                text = habit.name,
                fontSize = 18.sp,
                textDecoration = if (habit.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                color = if (habit.isCompleted) Color.Gray else Color.Black,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // complete button changes color when done
            Button(
                onClick = onButtonClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (habit.isCompleted) Color(0xFF4CAF50) else Color(0xFF2196F3)
                )
            ) {
                Text(
                    text = if (habit.isCompleted) "Done!" else "Complete",
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // delete button removes habit from list
            Button(
                onClick = onDeleteClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
            ) {
                Text(text = "Delete", color = Color.White)
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
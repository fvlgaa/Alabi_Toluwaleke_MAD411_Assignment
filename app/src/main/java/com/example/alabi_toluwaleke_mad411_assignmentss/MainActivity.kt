package com.example.alabi_toluwaleke_mad411_assignmentss

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Alabi_Toluwaleke_MAD411_AssignmentssTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HabitTrackerApp()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("HabitTracker", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d("HabitTracker", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("HabitTracker", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("HabitTracker", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("HabitTracker", "onDestroy called")
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

    // nav controller handles moving between screens
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "habitList"
    ) {
        // main screen route
        composable("habitList") {
            HabitListScreen(viewModel = viewModel, navController = navController)
        }

        // detail screen route - habit name gets passed through the route
        composable("habitDetail/{habitName}/{isCompleted}") { backStackEntry ->
            val habitName = backStackEntry.arguments?.getString("habitName") ?: ""
            val isCompleted = backStackEntry.arguments?.getString("isCompleted") ?: "false"
            HabitDetailScreen(
                habitName = habitName,
                isCompleted = isCompleted.toBoolean(),
                navController = navController
            )
        }
    }
}

@Composable
fun HabitInputSection(
    text: String,
    onTextChange: (String) -> Unit
) {
    // if its blank its an error simple as
    // that
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
    Spacer(modifier = Modifier.height(8.dp))

}

@Composable
fun HabitListSection(
    habits: List<Habit>,
    onCompleteClick: (Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onViewDetails: (Habit) -> Unit
) {
    // lazy column so it only loads what's on screen
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(habits, key = { it.id }) { habit ->
            HabitItemRow(
                habit = habit,
                onButtonClick = { onCompleteClick(habit.id) },
                onDeleteClick = { onDeleteClick(habit.id) },
                onViewDetails = { onViewDetails(habit) }
            )
        }
    }
}

@Composable
fun HabitItemRow(
    habit: Habit,
    onButtonClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onViewDetails: () -> Unit
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
                textDecoration = if (habit.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                color = if (habit.isCompleted) Color.Gray else Color.Black,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // view details button
            Button(
                onClick = onViewDetails,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
            ) {
                Text(text = "Details", color = Color.White)
            }

            Spacer(modifier = Modifier.width(8.dp))

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

            Button(
                onClick = onDeleteClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935))
            ) {
                Text(text = "Delete", color = Color.White)
            }
        }
    }
}


@Composable
fun HabitDetailScreen(
    habitName: String,
    isCompleted: Boolean,
    navController: androidx.navigation.NavController
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // header
        Text(
            text = "Habit Details",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Divider(
            thickness = 2.dp,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // habit info
        Text(text = "Habit: $habitName", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (isCompleted) "Status: Completed " else "Status: Not Completed ",
            fontSize = 18.sp,
            color = if (isCompleted) Color(0xFF4CAF50) else Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // open a webpage
        Button(
            onClick = {
                val intent = android.content.Intent(
                    android.content.Intent.ACTION_VIEW,
                    android.net.Uri.parse("https://www.stclaircollege.ca")
                )
                context.startActivity(intent)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text(text = "Visit St. Clair College", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // back button
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text(text = "Back", color = Color.White)
        }
    }
}


@Composable
fun HabitListScreen(viewModel: HabitViewModel, navController: androidx.navigation.NavController) {
    val habitList by viewModel.habits.collectAsStateWithLifecycle()
    var inputText by rememberSaveable { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            // header
            Text(
                text = "Student Habit Tracker",
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Divider(
                thickness = 2.dp,
                modifier = Modifier.padding(bottom = 20.dp)
            )

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
                    onDeleteClick = { viewModel.deleteHabit(it) },
                    onViewDetails = { habit -> navController.navigate("habitDetail/${habit.name}/${habit.isCompleted}")
                    }
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
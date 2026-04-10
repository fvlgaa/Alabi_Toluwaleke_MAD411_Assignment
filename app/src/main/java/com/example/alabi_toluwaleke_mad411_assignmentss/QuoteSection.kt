package com.example.alabi_toluwaleke_mad411_assignmentss

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

// Quote UI section (kept in its own file as required)
@Composable
fun QuoteSection(viewModel: HabitViewModel) {

    // collect quote state from ViewModel
    val quoteState by viewModel.quoteUiState.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Daily Inspiration",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6A1B9A),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // show UI based on state
            when (val state = quoteState) {

                is QuoteUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = Color(0xFF9C27B0)
                    )
                }

                is QuoteUiState.Success -> {
                    Text(
                        text = "\"${state.quote.q}\"",
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFF4A148C)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "— ${state.quote.a}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF6A1B9A)
                    )
                }

                is QuoteUiState.Error -> {
                    Text(
                        text = "Error loading quote: ${state.message}",
                        fontSize = 13.sp,
                        color = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // refresh button for new quote
            Button(
                onClick = { viewModel.fetchQuote() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
            ) {
                Text(text = "Refresh Quote", color = Color.White, fontSize = 13.sp)
            }
        }
    }
}

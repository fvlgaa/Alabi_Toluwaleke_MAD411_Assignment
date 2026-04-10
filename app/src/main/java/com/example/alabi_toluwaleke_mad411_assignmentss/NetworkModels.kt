package com.example.alabi_toluwaleke_mad411_assignmentss

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


data class Quote(
    val q: String,
    val a: String
)

// handles the API call
interface QuoteApiService {
    @GET("api/random")
    suspend fun getRandomQuote(): List<Quote>
}

// retrofit setup
object RetrofitInstance {
    val api: QuoteApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://zenquotes.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuoteApiService::class.java)
    }
}


sealed class QuoteUiState {
    object Loading : QuoteUiState() // show loading
    data class Success(val quote: Quote) : QuoteUiState() // show quote
    data class Error(val message: String) : QuoteUiState() // show error
}
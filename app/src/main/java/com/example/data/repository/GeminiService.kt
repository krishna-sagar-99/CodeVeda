package com.example.data.repository

import com.example.BuildConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@Serializable
data class GenerateContentRequest(
    val contents: List<Content>
)

@Serializable
data class Content(
    val parts: List<Part>
)

@Serializable
data class Part(
    val text: String
)

@Serializable
data class GenerateContentResponse(
    val candidates: List<Candidate>? = null
)

@Serializable
data class Candidate(
    val content: Content? = null
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

class GeminiService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://generativelanguage.googleapis.com/")
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val api = retrofit.create(GeminiApiService::class.java)

    suspend fun getChatResponse(message: String, role: String, context: String): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") return "AI Assistant is currently offline. Please configure API key."

        val systemPrompt = """
            You are CodeVeda AI Assistant, a helpful and safe guide for a Cooperative Service Marketplace.
            Current User Role: $role
            Context: $context
            
            Guidelines:
            - Help customers find services, understand categories, guide bookings, and provide price estimates.
            - Help workers with job info, earnings, and profile completion.
            - NEVER make unsafe professional decisions (e.g., don't give electrical repair advice).
            - For emergencies, clearly direct users toward appropriate emergency services.
            - Be polite, concise, and professional.
        """.trimIndent()

        val prompt = "$message"
        
        val request = GenerateContentRequest(
            contents = listOf(
                Content(parts = listOf(Part(text = systemPrompt))),
                Content(parts = listOf(Part(text = prompt)))
            )
        )

        return try {
            val response = api.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "I'm sorry, I couldn't process that."
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    suspend fun getRecommendations(userContext: String): String {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") return ""

        val prompt = "Based on the following user context, recommend 3 cooperative services from CodeVeda. Context: $userContext. Return the recommendations as a friendly short paragraph."
        
        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt))))
        )

        return try {
            val response = api.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}

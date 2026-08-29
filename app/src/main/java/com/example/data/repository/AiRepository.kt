package com.example.data.repository

import com.example.data.local.AiDao
import com.example.data.model.DemandForecast
import kotlinx.coroutines.flow.Flow
import java.util.*

class AiRepository(
    private val aiDao: AiDao,
    private val geminiService: GeminiService
) {
    val allForecasts: Flow<List<DemandForecast>> = aiDao.getAllForecasts()

    fun getForecastsByCategory(categoryId: String): Flow<List<DemandForecast>> = 
        aiDao.getForecastsByCategory(categoryId)

    suspend fun generateDemandForecast() {
        // This would call Gemini to analyze historical data and predict demand
        // For now, we simulate by inserting some forecasts
        val forecast = DemandForecast(
            id = UUID.randomUUID().toString(),
            categoryId = "cat_elec",
            location = "Sector X",
            predictedDemand = 85,
            confidence = 0.92f,
            timeSlot = "2026-08-30 18:00"
        )
        aiDao.insertForecast(forecast)
    }

    suspend fun getChatResponse(message: String, role: String, context: String): String {
        return geminiService.getChatResponse(message, role, context)
    }

    suspend fun getPriceRange(serviceContext: String): String {
        // AI pricing insights simulation
        return "₹300 - ₹450"
    }
}

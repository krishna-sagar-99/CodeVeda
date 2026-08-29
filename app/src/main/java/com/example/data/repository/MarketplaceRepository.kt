package com.example.data.repository

import com.example.data.local.MarketplaceDao
import com.example.data.local.WorkerDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.*

class MarketplaceRepository(
    private val marketplaceDao: MarketplaceDao,
    private val workerDao: WorkerDao
) {
    val categories: Flow<List<ServiceCategory>> = marketplaceDao.getAllCategories()

    fun getSkillsByCategory(categoryId: String): Flow<List<Skill>> = 
        marketplaceDao.getSkillsByCategory(categoryId)

    fun getBookingsForCustomer(customerId: String): Flow<List<Booking>> = 
        marketplaceDao.getBookingsForCustomer(customerId)

    fun getBookingsForWorker(workerId: String): Flow<List<Booking>> = 
        marketplaceDao.getBookingsForWorker(workerId)

    suspend fun createBooking(booking: Booking) {
        marketplaceDao.insertBooking(booking)
    }

    suspend fun updateBookingStatus(bookingId: String, status: BookingStatus) {
        marketplaceDao.getBookingById(bookingId)?.let {
            marketplaceDao.updateBooking(it.copy(status = status))
        }
    }

    // AI Smart Worker Matching Simulation
    suspend fun getBestMatches(
        serviceId: String,
        skillId: String,
        latitude: Double,
        longitude: Double,
        isEmergency: Boolean
    ): List<WorkerMatch> {
        // In a real app, this would be a network call to a backend AI service
        // For this demo, we return the seeded "Verified" workers
        return listOf(
            WorkerMatch("w_v1", "Amit Kumar (Verified)", 4.8f, 1.2, 350.0, 95),
            WorkerMatch("w_v2", "Suresh Raina (Verified)", 4.5f, 2.5, 300.0, 88)
        )
    }

    suspend fun submitReview(review: Review) {
        marketplaceDao.insertReview(review)
    }

    suspend fun processPayment(payment: Payment) {
        marketplaceDao.insertPayment(payment)
    }

    suspend fun seedInitialData() {
        val currentCategories = marketplaceDao.getAllCategories().first()
        if (currentCategories.isEmpty()) {
            val categories = listOf(
                ServiceCategory("cat_elec", "Electrician", "FlashOn", "Electrical repairs and installations"),
                ServiceCategory("cat_plum", "Plumber", "WaterDrop", "Plumbing and leakage fixes"),
                ServiceCategory("cat_carp", "Carpenter", "Construction", "Woodwork and furniture repair"),
                ServiceCategory("cat_pain", "Painter", "FormatPaint", "Home and office painting"),
                ServiceCategory("cat_clea", "Cleaner", "CleaningServices", "Professional cleaning services"),
                ServiceCategory("cat_gard", "Gardener", "Park", "Lawn and garden maintenance"),
                ServiceCategory("cat_driv", "Driver", "DirectionsCar", "Professional drivers for hire"),
                ServiceCategory("cat_repa", "Repair", "Handyman", "General appliance and home repair")
            )
            
            categories.forEach { marketplaceDao.insertCategory(it) }

            // Add some skills
            val skills = listOf(
                Skill("s1", "Fan Installation", "cat_elec", 250.0),
                Skill("s2", "Appliance Repair", "cat_elec", 300.0),
                Skill("s3", "Tap Leakage", "cat_plum", 200.0),
                Skill("s4", "Pipe Replacement", "cat_plum", 400.0)
            )
            skills.forEach { marketplaceDao.insertSkill(it) }
        }
    }
}

data class WorkerMatch(
    val id: String,
    val name: String,
    val rating: Float,
    val distanceKm: Double,
    val estimatedPrice: Double,
    val matchScore: Int
)

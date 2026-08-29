package com.example.data.local

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketplaceDao {
    @Query("SELECT * FROM service_categories")
    fun getAllCategories(): Flow<List<ServiceCategory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: ServiceCategory)

    @Query("SELECT * FROM skills WHERE categoryId = :categoryId")
    fun getSkillsByCategory(categoryId: String): Flow<List<Skill>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkill(skill: Skill)

    @Query("SELECT * FROM bookings WHERE customerId = :customerId ORDER BY scheduledTime DESC")
    fun getBookingsForCustomer(customerId: String): Flow<List<Booking>>

    @Query("SELECT * FROM bookings WHERE workerId = :workerId ORDER BY scheduledTime DESC")
    fun getBookingsForWorker(workerId: String): Flow<List<Booking>>

    @Query("SELECT * FROM bookings WHERE id = :bookingId")
    suspend fun getBookingById(bookingId: String): Booking?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)

    @Update
    suspend fun updateBooking(booking: Booking)

    @Query("SELECT * FROM reviews WHERE targetId = :targetId")
    fun getReviewsForTarget(targetId: String): Flow<List<Review>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: Review)

    @Query("SELECT * FROM payments WHERE bookingId = :bookingId")
    fun getPaymentForBooking(bookingId: String): Flow<Payment?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: Payment)
}

@Dao
interface WorkerDao {
    @Query("SELECT * FROM worker_profiles WHERE userId = :userId")
    suspend fun getWorkerProfileByUserId(userId: String): WorkerProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkerProfile(profile: WorkerProfile)

    @Update
    suspend fun updateWorkerProfile(profile: WorkerProfile)

    @Query("SELECT * FROM worker_skills WHERE workerId = :workerId")
    fun getWorkerSkills(workerId: String): Flow<List<WorkerSkill>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkerSkill(workerSkill: WorkerSkill)

    @Query("SELECT * FROM worker_availability WHERE workerId = :workerId")
    fun getWorkerAvailability(workerId: String): Flow<List<WorkerAvailability>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAvailability(availability: WorkerAvailability)

    @Query("DELETE FROM worker_availability WHERE workerId = :workerId")
    suspend fun clearAvailability(workerId: String)
}

@Dao
interface CommunicationDao {
    @Query("SELECT * FROM messages WHERE bookingId = :bookingId ORDER BY timestamp ASC")
    fun getMessagesForBooking(bookingId: String): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC")
    fun getNotificationsForUser(userId: String): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllAsRead(userId: String)
}

@Dao
interface AdminDao {
    @Query("SELECT * FROM cooperatives")
    fun getAllCooperatives(): Flow<List<Cooperative>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCooperative(cooperative: Cooperative)

    @Update
    suspend fun updateCooperative(cooperative: Cooperative)

    @Query("SELECT * FROM support_tickets ORDER BY createdAt DESC")
    fun getAllTickets(): Flow<List<SupportTicket>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: SupportTicket)

    @Update
    suspend fun updateTicket(ticket: SupportTicket)

    @Query("SELECT * FROM audit_logs ORDER BY timestamp DESC")
    fun getAllAuditLogs(): Flow<List<AuditLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: AuditLog)

    @Query("SELECT * FROM welfare_enrollments WHERE workerId = :workerId")
    fun getWelfareForWorker(workerId: String): Flow<List<WelfareEnrollment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWelfare(welfare: WelfareEnrollment)

    @Query("SELECT * FROM announcements ORDER BY scheduledAt DESC")
    fun getAllAnnouncements(): Flow<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: Announcement)
    
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM worker_profiles")
    fun getAllWorkerProfiles(): Flow<List<WorkerProfile>>

    @Query("SELECT * FROM bookings")
    fun getAllBookings(): Flow<List<Booking>>
}

@Dao
interface AiDao {
    @Query("SELECT * FROM demand_forecasts WHERE categoryId = :categoryId")
    fun getForecastsByCategory(categoryId: String): Flow<List<DemandForecast>>

    @Query("SELECT * FROM demand_forecasts")
    fun getAllForecasts(): Flow<List<DemandForecast>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(forecast: DemandForecast)
}

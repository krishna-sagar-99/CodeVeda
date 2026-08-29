package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

enum class UserRole {
    CUSTOMER,
    WORKER,
    COOPERATIVE_ADMIN,
    SUPER_ADMIN
}

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val fullName: String,
    val email: String,
    val mobileNumber: String,
    val profilePhotoUrl: String? = null,
    val currentRole: UserRole = UserRole.CUSTOMER,
    val roles: Set<UserRole> = setOf(UserRole.CUSTOMER),
    val location: String? = null,
    val address: String? = null,
    val preferredLanguage: String = "English",
    val isVerified: Boolean = false,
    val verificationStatus: VerificationStatus = VerificationStatus.PENDING,
    val cooperativeId: String? = null
)

enum class VerificationStatus {
    PENDING,
    UNDER_REVIEW,
    VERIFIED,
    REJECTED,
    SUSPENDED
}

@Entity(
    tableName = "worker_profiles",
    indices = [Index("userId")],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WorkerProfile(
    @PrimaryKey val id: String,
    val userId: String,
    val bio: String? = null,
    val experienceYears: Int = 0,
    val serviceRadiusKm: Int = 10,
    val rating: Float = 0f,
    val completedJobs: Int = 0,
    val isOnline: Boolean = false,
    val emergencyAvailability: Boolean = false,
    val idProofUrl: String? = null,
    val bankAccountDetails: String? = null,
    val emergencyContact: String? = null
)

@Entity(tableName = "skills")
data class Skill(
    @PrimaryKey val id: String,
    val name: String,
    val categoryId: String,
    val basePrice: Double = 0.0,
    val pricingModel: PricingModel = PricingModel.HOURLY
)

enum class PricingModel {
    FIXED,
    HOURLY,
    ESTIMATED,
    CUSTOM
}

@Entity(
    tableName = "worker_skills",
    primaryKeys = ["workerId", "skillId"]
)
data class WorkerSkill(
    val workerId: String,
    val skillId: String,
    val experienceLevel: String,
    val certificationUrl: String? = null,
    val isVerified: Boolean = false
)

@Entity(tableName = "service_categories")
data class ServiceCategory(
    @PrimaryKey val id: String,
    val name: String,
    val iconName: String,
    val description: String
)

@Entity(
    tableName = "bookings",
    indices = [Index("customerId"), Index("workerId"), Index("serviceId")]
)
data class Booking(
    @PrimaryKey val id: String,
    val customerId: String,
    val workerId: String?,
    val serviceId: String,
    val skillId: String,
    val status: BookingStatus,
    val scheduledTime: Long,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val description: String? = null,
    val isEmergency: Boolean = false,
    val baseAmount: Double,
    val platformFee: Double,
    val totalAmount: Double,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

enum class BookingStatus {
    REQUESTED,
    ACCEPTED,
    ON_THE_WAY,
    ARRIVED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    DISPUTED
}

@Entity(tableName = "payments")
data class Payment(
    @PrimaryKey val id: String,
    val bookingId: String,
    val amount: Double,
    val status: PaymentStatus,
    val method: String,
    val transactionId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class PaymentStatus {
    PENDING,
    AUTHORIZED,
    PAID,
    FAILED,
    REFUNDED
}

@Entity(tableName = "reviews")
data class Review(
    @PrimaryKey val id: String,
    val bookingId: String,
    val reviewerId: String,
    val targetId: String,
    val rating: Int,
    val comment: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey val id: String,
    val bookingId: String,
    val senderId: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val body: String,
    val type: NotificationType,
    val relatedId: String? = null,
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

enum class NotificationType {
    NEW_BOOKING,
    BOOKING_UPDATE,
    PAYMENT,
    MESSAGE,
    SYSTEM,
    VERIFICATION
}

@Entity(tableName = "cooperatives")
data class Cooperative(
    @PrimaryKey val id: String,
    val name: String,
    val registrationNumber: String,
    val location: String,
    val contactEmail: String,
    val contactPhone: String,
    val status: CooperativeStatus = CooperativeStatus.PENDING,
    val createdAt: Long = System.currentTimeMillis()
)

enum class CooperativeStatus {
    ACTIVE,
    PENDING,
    SUSPENDED
}

@Entity(tableName = "support_tickets")
data class SupportTicket(
    @PrimaryKey val id: String,
    val creatorId: String,
    val bookingId: String? = null,
    val category: TicketCategory,
    val subject: String,
    val description: String,
    val status: TicketStatus = TicketStatus.OPEN,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class TicketCategory {
    PAYMENT,
    BOOKING,
    WORKER_ISSUE,
    CUSTOMER_ISSUE,
    TECHNICAL_PROBLEM,
    SAFETY_CONCERN,
    OTHER
}

enum class TicketStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSED
}

@Entity(tableName = "audit_logs")
data class AuditLog(
    @PrimaryKey val id: String,
    val adminId: String,
    val action: String,
    val targetEntity: String,
    val targetId: String,
    val details: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "demand_forecasts")
data class DemandForecast(
    @PrimaryKey val id: String,
    val categoryId: String,
    val location: String,
    val predictedDemand: Int,
    val confidence: Float,
    val timeSlot: String, // e.g., "2026-08-30 18:00"
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "welfare_enrollments")
data class WelfareEnrollment(
    @PrimaryKey val id: String,
    val workerId: String,
    val planName: String,
    val status: String, // e.g., "Active", "Pending"
    val enrollmentDate: Long,
    val expiryDate: Long? = null,
    val documentUrl: String? = null
)

@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    val target: AnnouncementTarget,
    val cooperativeId: String? = null,
    val scheduledAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "worker_availability")
data class WorkerAvailability(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val workerId: String,
    val dayOfWeek: Int, // 1-7
    val startTime: String, // "08:00"
    val endTime: String
)

enum class AnnouncementTarget {
    ALL,
    CUSTOMERS,
    WORKERS,
    SPECIFIC_COOPERATIVE
}

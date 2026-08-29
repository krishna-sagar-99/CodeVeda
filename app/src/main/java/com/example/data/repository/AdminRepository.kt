package com.example.data.repository

import com.example.data.local.AdminDao
import com.example.data.local.UserDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.*

class AdminRepository(
    private val adminDao: AdminDao,
    private val userDao: UserDao
) {
    val cooperatives: Flow<List<Cooperative>> = adminDao.getAllCooperatives()
    val allTickets: Flow<List<SupportTicket>> = adminDao.getAllTickets()
    val auditLogs: Flow<List<AuditLog>> = adminDao.getAllAuditLogs()
    val announcements: Flow<List<Announcement>> = adminDao.getAllAnnouncements()
    val allUsers: Flow<List<User>> = adminDao.getAllUsers()
    val allWorkerProfiles: Flow<List<WorkerProfile>> = adminDao.getAllWorkerProfiles()
    val allBookings: Flow<List<Booking>> = adminDao.getAllBookings()

    suspend fun createCooperative(cooperative: Cooperative) {
        adminDao.insertCooperative(cooperative)
    }

    suspend fun updateCooperativeStatus(id: String, status: CooperativeStatus) {
        // Fetch and update logic
    }

    suspend fun verifyWorker(userId: String) {
        userDao.getUserById(userId)?.let { user ->
            userDao.saveUser(user.copy(verificationStatus = VerificationStatus.VERIFIED, isVerified = true))
        }
    }

    suspend fun rejectWorker(userId: String) {
        userDao.getUserById(userId)?.let { user ->
            userDao.saveUser(user.copy(verificationStatus = VerificationStatus.REJECTED, isVerified = false))
        }
    }

    suspend fun suspendWorker(userId: String) {
        userDao.getUserById(userId)?.let { user ->
            userDao.saveUser(user.copy(verificationStatus = VerificationStatus.SUSPENDED, isVerified = false))
        }
    }

    suspend fun createTicket(ticket: SupportTicket) {
        adminDao.insertTicket(ticket)
    }

    suspend fun updateTicketStatus(ticketId: String, status: TicketStatus) {
        // Fetch and update logic
    }

    suspend fun createAnnouncement(announcement: Announcement) {
        adminDao.insertAnnouncement(announcement)
    }

    suspend fun logAction(adminId: String, action: String, targetEntity: String, targetId: String, details: String? = null) {
        val log = AuditLog(
            id = UUID.randomUUID().toString(),
            adminId = adminId,
            action = action,
            targetEntity = targetEntity,
            targetId = targetId,
            details = details
        )
        adminDao.insertAuditLog(log)
    }

    fun getWelfareForWorker(workerId: String): Flow<List<WelfareEnrollment>> = 
        adminDao.getWelfareForWorker(workerId)

    suspend fun enrollInWelfare(enrollment: WelfareEnrollment) {
        adminDao.insertWelfare(enrollment)
    }
}

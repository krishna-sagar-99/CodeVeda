package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.AdminRepository
import com.example.data.repository.AiRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class AdminViewModel(
    private val adminRepository: AdminRepository,
    private val aiRepository: AiRepository
) : ViewModel() {

    val cooperatives: StateFlow<List<Cooperative>> = adminRepository.cooperatives
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allTickets: StateFlow<List<SupportTicket>> = adminRepository.allTickets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val auditLogs: StateFlow<List<AuditLog>> = adminRepository.auditLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val demandForecasts: StateFlow<List<DemandForecast>> = aiRepository.allForecasts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allUsers: StateFlow<List<User>> = adminRepository.allUsers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allBookings: StateFlow<List<Booking>> = adminRepository.allBookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun verifyWorker(userId: String) {
        viewModelScope.launch {
            adminRepository.verifyWorker(userId)
            adminRepository.logAction("system", "VERIFY_WORKER", "User", userId, "Worker verified by admin")
        }
    }

    fun rejectWorker(userId: String) {
        viewModelScope.launch {
            adminRepository.rejectWorker(userId)
            adminRepository.logAction("system", "REJECT_WORKER", "User", userId, "Worker rejected by admin")
        }
    }

    fun generateForecast() {
        viewModelScope.launch {
            aiRepository.generateDemandForecast()
        }
    }

    fun resolveTicket(ticketId: String) {
        viewModelScope.launch {
            adminRepository.updateTicketStatus(ticketId, TicketStatus.RESOLVED)
        }
    }
}

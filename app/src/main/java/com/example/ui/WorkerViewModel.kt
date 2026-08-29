package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.MarketplaceRepository
import com.example.data.repository.WorkerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WorkerViewModel(
    private val workerRepository: WorkerRepository,
    private val marketplaceRepository: MarketplaceRepository
) : ViewModel() {

    private val _workerProfile = MutableStateFlow<WorkerProfile?>(null)
    val workerProfile = _workerProfile.asStateFlow()

    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    val bookings = _bookings.asStateFlow()

    fun loadWorkerData(userId: String) {
        viewModelScope.launch {
            _workerProfile.value = workerRepository.getProfile(userId)
            _workerProfile.value?.let { profile ->
                marketplaceRepository.getBookingsForWorker(profile.id).collect {
                    _bookings.value = it
                }
            }
        }
    }

    fun updateBookingStatus(bookingId: String, status: BookingStatus) {
        viewModelScope.launch {
            marketplaceRepository.updateBookingStatus(bookingId, status)
        }
    }

    fun toggleOnline(userId: String, isOnline: Boolean) {
        viewModelScope.launch {
            workerRepository.toggleOnlineStatus(userId, isOnline)
            _workerProfile.value = workerRepository.getProfile(userId)
        }
    }

    fun saveProfile(profile: WorkerProfile) {
        viewModelScope.launch {
            workerRepository.saveProfile(profile)
            _workerProfile.value = profile
        }
    }
}

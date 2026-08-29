package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.MarketplaceRepository
import com.example.data.repository.WorkerMatch
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class CustomerViewModel(
    private val marketplaceRepository: MarketplaceRepository
) : ViewModel() {

    private val _categories = marketplaceRepository.categories
    val categories: StateFlow<List<ServiceCategory>> = _categories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedCategory = MutableStateFlow<ServiceCategory?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    private val _skills = MutableStateFlow<List<Skill>>(emptyList())
    val skills = _skills.asStateFlow()

    private val _workerMatches = MutableStateFlow<List<WorkerMatch>>(emptyList())
    val workerMatches = _workerMatches.asStateFlow()

    fun selectCategory(category: ServiceCategory) {
        _selectedCategory.value = category
        viewModelScope.launch {
            marketplaceRepository.getSkillsByCategory(category.id).collect {
                _skills.value = it
            }
        }
    }

    fun loadSkillsForCategory(categoryId: String) {
        viewModelScope.launch {
            marketplaceRepository.getSkillsByCategory(categoryId).collect {
                _skills.value = it
            }
        }
    }

    fun findWorkers(skillId: String, lat: Double, lon: Double, isEmergency: Boolean = false) {
        viewModelScope.launch {
            val matches = marketplaceRepository.getBestMatches(
                serviceId = _selectedCategory.value?.id ?: "",
                skillId = skillId,
                latitude = lat,
                longitude = lon,
                isEmergency = isEmergency
            )
            _workerMatches.value = matches
        }
    }

    fun bookWorker(
        customerId: String,
        workerMatch: WorkerMatch,
        skillId: String,
        address: String,
        lat: Double,
        lon: Double,
        description: String,
        isEmergency: Boolean
    ) {
        viewModelScope.launch {
            val booking = Booking(
                id = UUID.randomUUID().toString(),
                customerId = customerId,
                workerId = workerMatch.id,
                serviceId = _selectedCategory.value?.id ?: "",
                skillId = skillId,
                status = BookingStatus.REQUESTED,
                scheduledTime = System.currentTimeMillis(),
                address = address,
                latitude = lat,
                longitude = lon,
                description = description,
                isEmergency = isEmergency,
                baseAmount = workerMatch.estimatedPrice,
                platformFee = workerMatch.estimatedPrice * 0.1,
                totalAmount = workerMatch.estimatedPrice * 1.1
            )
            marketplaceRepository.createBooking(booking)
        }
    }
}

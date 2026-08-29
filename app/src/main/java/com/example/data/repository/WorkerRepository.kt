package com.example.data.repository

import com.example.data.local.WorkerDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

class WorkerRepository(private val workerDao: WorkerDao) {

    suspend fun getProfile(userId: String): WorkerProfile? = 
        workerDao.getWorkerProfileByUserId(userId)

    suspend fun saveProfile(profile: WorkerProfile) {
        workerDao.insertWorkerProfile(profile)
    }

    fun getSkills(workerId: String): Flow<List<WorkerSkill>> = 
        workerDao.getWorkerSkills(workerId)

    suspend fun addSkill(skill: WorkerSkill) {
        workerDao.insertWorkerSkill(skill)
    }

    fun getAvailability(workerId: String): Flow<List<WorkerAvailability>> = 
        workerDao.getWorkerAvailability(workerId)

    suspend fun updateAvailability(workerId: String, slots: List<WorkerAvailability>) {
        workerDao.clearAvailability(workerId)
        slots.forEach { workerDao.insertAvailability(it) }
    }

    suspend fun toggleOnlineStatus(userId: String, isOnline: Boolean) {
        workerDao.getWorkerProfileByUserId(userId)?.let {
            workerDao.updateWorkerProfile(it.copy(isOnline = isOnline))
        }
    }
}

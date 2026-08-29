package com.example.data.repository

import com.example.data.local.UserDao
import com.example.data.model.User
import com.example.data.model.VerificationStatus
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {
    val loggedInUser: Flow<User?> = userDao.getLoggedInUser()

    suspend fun getLoggedInUserOnce(): User? {
        return userDao.getLoggedInUserOnce()
    }

    suspend fun saveUser(user: User) {
        userDao.saveUser(user)
    }

    suspend fun getUserById(userId: String): User? {
        return userDao.getUserById(userId)
    }

    suspend fun updateVerificationStatus(userId: String, status: VerificationStatus) {
        userDao.getLoggedInUserOnce()?.let {
            if (it.id == userId) {
                userDao.saveUser(it.copy(verificationStatus = status, isVerified = status == VerificationStatus.VERIFIED))
            }
        }
    }

    suspend fun updateProfile(user: User) {
        userDao.saveUser(user)
    }

    suspend fun logout() {
        userDao.clearUser()
    }
}

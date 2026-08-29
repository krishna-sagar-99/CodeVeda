package com.example.data.repository

import com.example.data.local.CommunicationDao
import com.example.data.model.Message
import com.example.data.model.Notification
import kotlinx.coroutines.flow.Flow

class CommunicationRepository(private val communicationDao: CommunicationDao) {
    fun getMessages(bookingId: String): Flow<List<Message>> = 
        communicationDao.getMessagesForBooking(bookingId)

    suspend fun sendMessage(message: Message) {
        communicationDao.insertMessage(message)
    }

    fun getNotifications(userId: String): Flow<List<Notification>> = 
        communicationDao.getNotificationsForUser(userId)

    suspend fun sendNotification(notification: Notification) {
        communicationDao.insertNotification(notification)
    }

    suspend fun markNotificationsRead(userId: String) {
        communicationDao.markAllAsRead(userId)
    }
}

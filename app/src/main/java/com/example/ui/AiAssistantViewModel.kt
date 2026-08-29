package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.AiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class AiAssistantViewModel(
    private val aiRepository: AiRepository
) : ViewModel() {

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(listOf(
        ChatMessage(text = "Hello! I am CodeVeda AI. How can I help you today?", isFromUser = false)
    ))
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping.asStateFlow()

    fun sendMessage(message: String, userRole: String, context: String) {
        if (message.isBlank()) return

        val userMsg = ChatMessage(text = message, isFromUser = true)
        _chatMessages.value = _chatMessages.value + userMsg

        viewModelScope.launch {
            _isTyping.value = true
            val response = aiRepository.getChatResponse(message, userRole, context)
            _isTyping.value = false
            
            val aiMsg = ChatMessage(text = response, isFromUser = false)
            _chatMessages.value = _chatMessages.value + aiMsg
        }
    }
}

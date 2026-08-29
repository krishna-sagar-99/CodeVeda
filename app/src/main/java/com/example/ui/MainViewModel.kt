package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.User
import com.example.data.model.UserRole
import com.example.data.repository.UserRepository
import com.example.data.repository.MarketplaceRepository
import com.example.util.PreferenceManager
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.example.data.repository.GeminiService

import com.example.auth.AuthService
import com.google.firebase.auth.FirebaseUser

class MainViewModel(
    private val userRepository: UserRepository,
    private val marketplaceRepository: MarketplaceRepository,
    private val preferenceManager: PreferenceManager,
    private val authService: AuthService,
    private val geminiService: GeminiService = GeminiService()
) : ViewModel() {

    private val _authState = MutableStateFlow<FirebaseUser?>(authService.currentUser)
    val authState: StateFlow<FirebaseUser?> = _authState.asStateFlow()

    init {
        viewModelScope.launch {
            marketplaceRepository.seedInitialData()
        }
    }

    private val _recommendations = MutableStateFlow<String?>(null)
    val recommendations: StateFlow<String?> = _recommendations.asStateFlow()

    fun fetchRecommendations(context: String) {
        viewModelScope.launch {
            _recommendations.value = geminiService.getRecommendations(context)
        }
    }

    val user: StateFlow<User?> = userRepository.loggedInUser.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val isDarkMode: StateFlow<Boolean?> = preferenceManager.isDarkMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val language: StateFlow<String> = preferenceManager.appLanguage.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "English"
    )

    fun setTheme(isDark: Boolean) {
        viewModelScope.launch {
            preferenceManager.setTheme(isDark)
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            preferenceManager.setLanguage(language)
        }
    }

    fun loginWithGoogle(selectedRole: UserRole? = null) {
        viewModelScope.launch {
            val uid = authService.signInWithGoogle()
            if (uid != null) {
                _authState.value = authService.currentUser
                // Sync with local DB
                val firebaseUser = authService.currentUser
                if (firebaseUser != null) {
                    val existingUser = userRepository.getUserById(firebaseUser.uid)
                    if (existingUser == null) {
                        // New user from social login
                        val newUser = User(
                            id = firebaseUser.uid,
                            fullName = firebaseUser.displayName ?: "Google User",
                            email = firebaseUser.email ?: "",
                            mobileNumber = "",
                            roles = setOf(selectedRole ?: UserRole.CUSTOMER),
                            currentRole = selectedRole ?: UserRole.CUSTOMER
                        )
                        userRepository.saveUser(newUser)
                    } else {
                        // Just update current logged in pointer if necessary
                        userRepository.saveUser(existingUser)
                    }
                }
            } else {
                // Fallback for emulator / local development without Play Services configured
                val fallbackUser = User(
                    id = UUID.randomUUID().toString(),
                    fullName = "Google Member",
                    email = "member@gmail.com",
                    mobileNumber = "+91 98765 43210",
                    roles = setOf(selectedRole ?: UserRole.CUSTOMER),
                    currentRole = selectedRole ?: UserRole.CUSTOMER
                )
                userRepository.saveUser(fallbackUser)
            }
        }
    }

    fun register(fullName: String, email: String, mobile: String, role: UserRole) {
        viewModelScope.launch {
            val uid = UUID.randomUUID().toString()
            userRepository.saveUser(
                User(
                    id = uid,
                    fullName = fullName.ifBlank { "User" },
                    email = email.ifBlank { "user@codeveda.coop" },
                    mobileNumber = mobile,
                    roles = setOf(role),
                    currentRole = role
                )
            )
        }
    }

    fun login(email: String = "", password: String = "") {
        viewModelScope.launch {
            val currentUser = userRepository.getLoggedInUserOnce()
            if (currentUser == null) {
                val name = if (email.isNotBlank()) email.substringBefore("@").replaceFirstChar { it.uppercase() } else "Demo User"
                val demoUser = User(
                    id = UUID.randomUUID().toString(),
                    fullName = name,
                    email = if (email.isNotBlank()) email else "user@codeveda.coop",
                    mobileNumber = "+91 98765 43210",
                    roles = setOf(UserRole.CUSTOMER, UserRole.WORKER),
                    currentRole = UserRole.CUSTOMER
                )
                userRepository.saveUser(demoUser)
            }
        }
    }

    fun switchRole(newRole: UserRole) {
        viewModelScope.launch {
            user.value?.let { currentUser ->
                if (currentUser.roles.contains(newRole)) {
                    userRepository.saveUser(currentUser.copy(currentRole = newRole))
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authService.logout()
            _authState.value = null
            userRepository.logout()
        }
    }
}

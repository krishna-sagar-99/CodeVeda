package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.data.local.AppDatabase
import com.example.data.model.User
import com.example.data.model.UserRole
import com.example.data.repository.*
import com.example.navigation.*
import com.example.ui.MainViewModel
import com.example.ui.CustomerViewModel
import com.example.ui.WorkerViewModel
import com.example.ui.AdminViewModel
import com.example.ui.AiAssistantViewModel
import com.example.ui.screens.*
import com.example.ui.theme.CodeVedaTheme
import com.example.util.PreferenceManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(this)
        val userRepository = UserRepository(db.userDao())
        val marketplaceRepository = MarketplaceRepository(db.marketplaceDao(), db.workerDao())
        val workerRepository = WorkerRepository(db.workerDao())
        val communicationRepository = CommunicationRepository(db.communicationDao())
        val adminRepository = AdminRepository(db.adminDao(), db.userDao())
        val geminiService = GeminiService()
        val aiRepository = AiRepository(db.aiDao(), geminiService)
        val preferenceManager = PreferenceManager(this)
        val authService = com.example.auth.AuthService(this)

        setContent {
            val mainViewModel: MainViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return MainViewModel(userRepository, marketplaceRepository, preferenceManager, authService) as T
                    }
                }
            )

            val customerViewModel: CustomerViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return CustomerViewModel(marketplaceRepository) as T
                    }
                }
            )

            val workerViewModel: WorkerViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return WorkerViewModel(workerRepository, marketplaceRepository) as T
                    }
                }
            )

            val adminViewModel: AdminViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return AdminViewModel(adminRepository, aiRepository) as T
                    }
                }
            )

            val aiAssistantViewModel: AiAssistantViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return AiAssistantViewModel(aiRepository) as T
                    }
                }
            )

            val isDarkMode by mainViewModel.isDarkMode.collectAsStateWithLifecycle()
            val language by mainViewModel.language.collectAsStateWithLifecycle()
            val user by mainViewModel.user.collectAsStateWithLifecycle()

            CodeVedaTheme(darkTheme = isDarkMode ?: isSystemInDarkTheme()) {
                CodeVedaApp(mainViewModel, customerViewModel, workerViewModel, adminViewModel, aiAssistantViewModel, user)
            }
        }
    }
}

@Composable
fun CodeVedaApp(
    viewModel: MainViewModel,
    customerViewModel: CustomerViewModel,
    workerViewModel: WorkerViewModel,
    adminViewModel: AdminViewModel,
    aiAssistantViewModel: AiAssistantViewModel,
    user: User?
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (user != null && currentRoute != null && !currentRoute.contains("Booking") && !currentRoute.contains("Chat") && !currentRoute.contains("Payment")) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute.contains("Home"),
                        onClick = {
                            if (user.currentRole == UserRole.CUSTOMER) {
                                navController.navigate(CustomerHomeRoute)
                            } else {
                                navController.navigate(WorkerHomeRoute)
                            }
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = currentRoute.contains("History"),
                        onClick = { navController.navigate(BookingHistoryRoute) },
                        icon = { Icon(Icons.Default.History, contentDescription = "History") },
                        label = { Text("History") }
                    )
                    NavigationBarItem(
                        selected = currentRoute.contains("Notifications"),
                        onClick = { navController.navigate(NotificationsRoute) },
                        icon = { Icon(Icons.Default.Notifications, contentDescription = "Notifications") },
                        label = { Text("Alerts") }
                    )
                    NavigationBarItem(
                        selected = currentRoute.contains("Settings") || currentRoute.contains("Profile"),
                        onClick = { navController.navigate(ProfileRoute) },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (user == null) LandingRoute else {
                if (user.currentRole == UserRole.CUSTOMER) CustomerHomeRoute else WorkerHomeRoute
            },
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<LandingRoute> {
                LandingScreen(
                    onBookService = { navController.navigate(LoginRoute) },
                    onBecomeWorker = { navController.navigate(RegisterRoute) }
                )
            }
            composable<LoginRoute> {
                LoginScreen(
                    onLoginSuccess = { email, pass -> 
                        viewModel.login(email, pass)
                        navController.navigate(CustomerHomeRoute) {
                            popUpTo(LandingRoute) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = { navController.navigate(RegisterRoute) },
                    onSocialLogin = { provider -> 
                        viewModel.loginWithGoogle()
                        navController.navigate(CustomerHomeRoute) {
                            popUpTo(LandingRoute) { inclusive = true }
                        }
                    }
                )
            }
            composable<RegisterRoute> {
                RegisterScreen(
                    onRegisterSuccess = { name, email, mobile, role -> 
                        viewModel.register(name, email, mobile, role)
                        val target = if (role == UserRole.CUSTOMER) CustomerHomeRoute else WorkerHomeRoute
                        navController.navigate(target) {
                            popUpTo(LandingRoute) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.navigate(LoginRoute) },
                    onSocialLogin = { provider, role -> 
                        viewModel.loginWithGoogle(role)
                        val target = if (role == UserRole.CUSTOMER) CustomerHomeRoute else WorkerHomeRoute
                        navController.navigate(target) {
                            popUpTo(LandingRoute) { inclusive = true }
                        }
                    }
                )
            }
            composable<CustomerHomeRoute> {
                CustomerHomeScreen(
                    viewModel = customerViewModel,
                    onCategoryClick = { navController.navigate(CategoryServicesRoute(it)) },
                    onEmergencyClick = { navController.navigate(EmergencyBookingRoute) },
                    onAiAssistantClick = { navController.navigate(AiAssistantRoute) }
                )
            }
            composable<CategoryServicesRoute> { backStackEntry ->
                val route: CategoryServicesRoute = backStackEntry.toRoute()
                CategoryServicesScreen(
                    categoryId = route.categoryId,
                    viewModel = customerViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onSkillSelected = { skillId ->
                        navController.navigate(BookingDetailRoute(skillId))
                    }
                )
            }
            composable<BookingDetailRoute> { backStackEntry ->
                val route: BookingDetailRoute = backStackEntry.toRoute()
                BookingWorkflowScreen(
                    skillId = route.bookingId,
                    viewModel = customerViewModel,
                    onBookingConfirmed = { bookingId ->
                        navController.navigate(PaymentRoute(bookingId))
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
            composable<PaymentRoute> { backStackEntry ->
                val route: PaymentRoute = backStackEntry.toRoute()
                PaymentScreen(
                    bookingId = route.bookingId,
                    amount = 385.0,
                    onPaymentSuccess = {
                        navController.navigate(CustomerHomeRoute) {
                            popUpTo(CustomerHomeRoute) { inclusive = true }
                        }
                    }
                )
            }
            composable<EmergencyBookingRoute> {
                EmergencyBookingScreen(
                    onFindEmergencyWorker = { category ->
                        navController.navigate(BookingTrackingScreenRoute("b-emergency"))
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
            composable<WorkerHomeRoute> {
                WorkerDashboardScreen(
                    onAiAssistantClick = { navController.navigate(AiAssistantRoute) },
                    onWelfareClick = { navController.navigate(WorkerWelfareRoute) },
                    onEarningsClick = { navController.navigate(EarningsRoute) }
                )
            }
            composable<EarningsRoute> {
                EarningsDashboardScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable<BookingHistoryRoute> {
                BookingHistoryScreen(
                    onBookingClick = { bookingId ->
                        // Navigate to detail or tracking
                    }
                )
            }
            composable<NotificationsRoute> {
                NotificationCenterScreen(onNavigateBack = { navController.popBackStack() })
            }
            composable<ProfileRoute> {
                ProfileScreen(
                    currentRole = user?.currentRole ?: UserRole.CUSTOMER,
                    onSwitchRole = { viewModel.switchRole(it) },
                    onLogout = { 
                        viewModel.logout()
                        navController.navigate(LandingRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onHistoryClick = { navController.navigate(BookingHistoryRoute) },
                    onRegisterWorker = { navController.navigate(WorkerRegistrationRoute) },
                    onNavigateToAdmin = { navController.navigate(CooperativeAdminRoute) }
                )
            }
            composable<WorkerRegistrationRoute> {
                WorkerRegistrationScreen(
                    userId = user?.id ?: "",
                    onRegistrationSuccess = {
                        navController.navigate(WorkerHomeRoute) {
                            popUpTo(ProfileRoute) { inclusive = false }
                        }
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
            composable<BookingTrackingScreenRoute> { backStackEntry ->
                val route: BookingTrackingScreenRoute = backStackEntry.toRoute()
                BookingTrackingScreen(
                    bookingId = route.bookingId,
                    onNavigateBack = { navController.popBackStack() },
                    onChatOpen = { navController.navigate(ChatRoute(route.bookingId)) }
                )
            }
            composable<ChatRoute> { backStackEntry ->
                val route: ChatRoute = backStackEntry.toRoute()
                ChatScreen(
                    bookingId = route.bookingId,
                    currentUserId = user?.id ?: "",
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable<AiAssistantRoute> {
                AiAssistantScreen(
                    viewModel = aiAssistantViewModel,
                    userRole = user?.currentRole?.name ?: "UNKNOWN",
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable<CooperativeAdminRoute> {
                CooperativeAdminDashboard(
                    viewModel = adminViewModel,
                    onNavigateToWorkers = { navController.navigate(AdminWorkerManagementRoute) },
                    onNavigateToBookings = { navController.navigate(AdminBookingManagementRoute) },
                    onNavigateToAnalytics = { navController.navigate(AdminAnalyticsRoute) },
                    onNavigateToSupport = { navController.navigate(AdminSupportRoute) },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable<SuperAdminRoute> {
                SuperAdminDashboard(
                    viewModel = adminViewModel,
                    onNavigateToCooperatives = { /* Implement if needed */ },
                    onNavigateToGlobalSettings = { /* Implement if needed */ }
                )
            }
            composable<AdminWorkerManagementRoute> {
                WorkerManagementScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
            }
            composable<AdminBookingManagementRoute> {
                BookingManagementScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
            }
            composable<AdminSupportRoute> {
                SupportCenterScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
            }
            composable<AdminAnalyticsRoute> {
                AnalyticsDashboardScreen(viewModel = adminViewModel, onNavigateBack = { navController.popBackStack() })
            }
            composable<WorkerWelfareRoute> {
                WorkerWelfareModule(onNavigateBack = { navController.popBackStack() })
            }
            composable<SettingsRoute> {
                SettingsScreen(
                    isDark = viewModel.isDarkMode.collectAsStateWithLifecycle().value ?: isSystemInDarkTheme(),
                    onThemeToggle = { viewModel.setTheme(it) },
                    language = viewModel.language.collectAsStateWithLifecycle().value,
                    onLanguageChange = { viewModel.setLanguage(it) }
                )
            }
        }
    }
}

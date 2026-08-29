package com.example.navigation

import kotlinx.serialization.Serializable

@Serializable
object LandingRoute

@Serializable
object LoginRoute

@Serializable
object RegisterRoute

@Serializable
object OnboardingRoute

@Serializable
object CustomerHomeRoute

@Serializable
object WorkerHomeRoute

@Serializable
object AdminHomeRoute

@Serializable
data class ServiceDetailRoute(val serviceId: String)

@Serializable
object ProfileRoute

@Serializable
object SettingsRoute

@Serializable
object BookingsRoute

@Serializable
object MessagesRoute

@Serializable
object NotificationsRoute

@Serializable
object AvailabilityRoute

@Serializable
object EarningsRoute

@Serializable
data class CategoryServicesRoute(val categoryId: String)

@Serializable
data class BookingDetailRoute(val bookingId: String)

@Serializable
data class ChatRoute(val bookingId: String)

@Serializable
data class PaymentRoute(val bookingId: String)

@Serializable
object WorkerRegistrationRoute

@Serializable
object VerificationStatusRoute

@Serializable
data class BookingTrackingScreenRoute(val bookingId: String)

@Serializable
object EmergencyBookingRoute

@Serializable
object BookingHistoryRoute

@Serializable
object AiAssistantRoute

@Serializable
object CooperativeAdminRoute

@Serializable
object SuperAdminRoute

@Serializable
object AdminWorkerManagementRoute

@Serializable
object AdminBookingManagementRoute

@Serializable
object AdminSupportRoute

@Serializable
object AdminAnalyticsRoute

@Serializable
object WorkerWelfareRoute

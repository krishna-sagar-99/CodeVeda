package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.data.model.*

@Database(
    entities = [
        User::class,
        WorkerProfile::class,
        Skill::class,
        WorkerSkill::class,
        ServiceCategory::class,
        Booking::class,
        Payment::class,
        Review::class,
        Message::class,
        Notification::class,
        WorkerAvailability::class,
        Cooperative::class,
        SupportTicket::class,
        AuditLog::class,
        DemandForecast::class,
        WelfareEnrollment::class,
        Announcement::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun marketplaceDao(): MarketplaceDao
    abstract fun workerDao(): WorkerDao
    abstract fun communicationDao(): CommunicationDao
    abstract fun adminDao(): AdminDao
    abstract fun aiDao(): AiDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "codeveda_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

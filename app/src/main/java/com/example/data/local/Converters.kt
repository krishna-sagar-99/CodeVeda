package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.UserRole
import com.example.data.model.VerificationStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromUserRole(value: UserRole): String = value.name

    @TypeConverter
    fun toUserRole(value: String): UserRole = UserRole.valueOf(value)

    @TypeConverter
    fun fromVerificationStatus(value: VerificationStatus): String = value.name

    @TypeConverter
    fun toVerificationStatus(value: String): VerificationStatus = VerificationStatus.valueOf(value)

    @TypeConverter
    fun fromRolesSet(value: Set<UserRole>): String = Gson().toJson(value)

    @TypeConverter
    fun toRolesSet(value: String): Set<UserRole> {
        val type = object : TypeToken<Set<UserRole>>() {}.type
        return Gson().fromJson(value, type)
    }
}

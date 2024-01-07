package com.woojun.emoji.util

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.parcel.Parcelize

class TypeConverter {
    @TypeConverter
    fun fromUser(user: User): String {
        return Gson().toJson(user)
    }

    @TypeConverter
    fun toUser(user: String): User {
        val userType = object : TypeToken<User>() {}.type
        return Gson().fromJson(user, userType)
    }

    @TypeConverter
    fun fromDiary(bookInfo: Diary): String {
        return Gson().toJson(bookInfo)
    }

    @TypeConverter
    fun toDiary(bookInfoString: String): Diary {
        return Gson().fromJson(bookInfoString, Diary::class.java)
    }

    @TypeConverter
    fun fromDiaryList(bookInfoList: List<Diary>): String {
        val gson = Gson()
        return gson.toJson(bookInfoList)
    }

    @TypeConverter
    fun toDiaryList(bookInfoString: String): List<Diary> {
        val gson = Gson()
        val type = object : TypeToken<List<Diary>>() {}.type
        return gson.fromJson(bookInfoString, type)
    }
}

data class DiaryPost(
    val user: String,
    val emotion: String,
    val diary: String
)


@Parcelize
data class Diary(
    var image: String,
    val title: String,
    val context: String,
    val solution: String
): Parcelable

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var list: MutableList<Diary> = mutableListOf()
)

data class Chat(
    val text: String,
    val isAi: Boolean,
    val isCamera: Boolean = false,
    val isOnButton: Boolean = false
)

data class GptSolution(
    val solution: String
)

data class ImageData(val face: String)
data class EmotionResult(val emotion: String)
package com.woojun.emoji.util

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface RetrofitAPI {
    @POST("api/emotion")
    fun uploadImage(@Body imageData: ImageData): Call<EmotionResult>

    @POST("api/save/diary")
    fun postDiary(
        @Body diary: DiaryPost
    ): Call<GptSolution>
}
package com.woojun.emoji.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EmotionViewModel : ViewModel() {
    private val _dataItem = MutableLiveData<Diary>()
    val dataItem: LiveData<Diary> = _dataItem
    private val _emotion = MutableLiveData<String>("")
    val emotion: LiveData<String> = _emotion


    fun setEmotion(newImage: String) {
        _emotion.value = newImage
    }

    fun getEmotion(): String {
        return _emotion.value.toString()
    }

    fun updateData(diary: Diary) {
        _dataItem.value = diary
    }

    fun getData(): Diary? {
        return _dataItem.value
    }

    fun resetData() {
        _emotion.value = ""
        _dataItem.value = Diary("", "", "", "")
    }

}

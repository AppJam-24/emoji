package com.woojun.emoji.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _isPhotoTaken = MutableLiveData(false)
    private val _isAllowChat = MutableLiveData(false)

    val isPhotoTaken: LiveData<Boolean> = _isPhotoTaken
    val isAllowChat: LiveData<Boolean> = _isAllowChat

    fun onPhotoTaken() {
        _isPhotoTaken.value = true
    }
    fun onAllowChat() {
        _isAllowChat.value = true
    }

    fun offAllowChat() {
        _isAllowChat.value = false
    }

    fun resetData() {
        _isPhotoTaken.value = false
        _isAllowChat.value = false
    }
}

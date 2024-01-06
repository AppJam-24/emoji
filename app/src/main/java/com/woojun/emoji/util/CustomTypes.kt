package com.woojun.emoji.util

import java.util.Base64

data class Diary(
    val image: Base64,
    val title: String,
    val context: String,
    val solution: String
)
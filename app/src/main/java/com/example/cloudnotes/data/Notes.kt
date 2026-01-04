package com.example.cloudnotes.data

import com.google.firebase.Timestamp

data class Note(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
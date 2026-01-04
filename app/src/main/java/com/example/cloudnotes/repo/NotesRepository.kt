package com.example.cloudnotes.repository

import com.example.cloudnotes.data.Note
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NoteRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("notes")

    fun addNote(userId: String, title: String, description: String, onResult: (Boolean) -> Unit) {
        val newNote = Note(
            userId = userId,
            title = title,
            description = description,
            timestamp = Timestamp.now()
        )
        collection.add(newNote)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }


    fun getUserNotes(userId: String): Flow<List<Note>> = callbackFlow {
        val query = collection
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                close(error)
                return@addSnapshotListener
            }



            val notes = snapshot.documents.mapNotNull { doc ->
                val note = doc.toObject(Note::class.java)
                note?.copy(id = doc.id)
            }

            trySend(notes)
        }

        awaitClose { listener.remove() }
    }


    fun updateNote(noteId: String, title: String, description: String, onResult: (Boolean) -> Unit) {
        val updates = mapOf(
            "title" to title,
            "description" to description,
            "timestamp" to Timestamp.now()
        )

        collection.document(noteId).update(updates)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }


    fun deleteNote(noteId: String, onResult: (Boolean) -> Unit) {
        collection.document(noteId).delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }
}
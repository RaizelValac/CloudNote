package com.example.cloudnotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudnotes.data.Note
import com.example.cloudnotes.repository.NoteRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {

    private val repository = NoteRepository()
    private val auth = FirebaseAuth.getInstance()


    private val _user = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    val user: StateFlow<FirebaseUser?> = _user

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    init {
        auth.currentUser?.let { loadNotes(it.uid) }
    }

    fun login(email: String, pass: String, onError: (String) -> Unit) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                _user.value = it.user
                loadNotes(it.user!!.uid)
            }
            .addOnFailureListener { onError(it.message ?: "Login failed") }
    }

    fun signUp(email: String, pass: String, onError: (String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                _user.value = it.user
                loadNotes(it.user!!.uid)
            }
            .addOnFailureListener { onError(it.message ?: "Sign up failed") }
    }

    fun logout() {
        auth.signOut()
        _user.value = null
        _notes.value = emptyList()
    }


    private fun loadNotes(userId: String) {
        viewModelScope.launch {
            repository.getUserNotes(userId).collect { list ->
                _notes.value = list
            }
        }
    }

    fun addNote(title: String, desc: String) {
        val uid = _user.value?.uid ?: return
        repository.addNote(uid, title, desc) { success ->
            //
        }
    }

    fun updateNote(note: Note) {
        repository.updateNote(note.id, note.title, note.description) { success ->
            //
        }
    }

    fun deleteNote(noteId: String) {
        repository.deleteNote(noteId) { success ->
            //
        }
    }
}
package com.example.cloudnotes.screens



import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cloudnotes.data.Note
import com.example.cloudnotes.viewmodel.NotesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: NotesViewModel = viewModel(),
    onLogout: () -> Unit
) {
    val notes by viewModel.notes.collectAsState()

    // Dialog State
    var showDialog by remember { mutableStateOf(false) }
    var currentNote by remember { mutableStateOf<Note?>(null) } // Null = New Note, Not Null = Editing

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Notes") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, "Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                currentNote = null // Reset to "New Note" mode
                showDialog = true
            }) {
                Icon(Icons.Default.Add, "Add Note")
            }
        }
    ) { padding ->
        // READ: Display the List
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes) { note ->
                NoteItem(
                    note = note,
                    onEditClick = {
                        currentNote = note // Set "Edit Mode"
                        showDialog = true
                    },
                    onDeleteClick = { viewModel.deleteNote(note.id) }
                )
            }
        }

        // CREATE & UPDATE: The Dialog
        if (showDialog) {
            NoteDialog(
                noteToEdit = currentNote,
                onDismiss = { showDialog = false },
                onSave = { title, desc ->
                    if (currentNote == null) {
                        viewModel.addNote(title, desc) // Create
                    } else {
                        val updated = currentNote!!.copy(title = title, description = desc)
                        viewModel.updateNote(updated) // Update
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun NoteItem(note: Note, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    Card(elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(note.title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                IconButton(onClick = onEditClick) { Icon(Icons.Default.Edit, "Edit") }
                IconButton(onClick = onDeleteClick) { Icon(Icons.Default.Delete, "Delete") }
            }
            HorizontalDivider()
            Spacer(modifier = Modifier.height(8.dp))
            Text(note.description)
        }
    }
}

@Composable
fun NoteDialog(
    noteToEdit: Note?,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(noteToEdit?.title ?: "") }
    var desc by remember { mutableStateOf(noteToEdit?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (noteToEdit == null) "New Note" else "Edit Note") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") })
            }
        },
        confirmButton = {
            Button(onClick = { onSave(title, desc) }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

/**
 * Course: MAD204-LAB4
 * Name: Ashish Prajapati
 * Student ID : A00194842
 * Date: DECEMBER 6, 2025
 * Description: Main Activity of the Notes + Reminder App. Handles UI, CRUD operations
 * via ViewModel, requests notification permission, and starts the ReminderService.
 */

package com.example.lab4notesreminderapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4notesreminderapp.database.Note
import com.example.lab4notesreminderapp.utility.ReminderService
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    private lateinit var titleInput: EditText
    private lateinit var contentInput: EditText
    private lateinit var addButton: Button
    private lateinit var recyclerView: RecyclerView

    private val CHANNEL_ID = "reminder_channel"
    private val PERMISSION_REQUEST_CODE = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        titleInput = findViewById(R.id.edit_text_title)
        contentInput = findViewById(R.id.edit_text_content)
        addButton = findViewById(R.id.button_add_note)
        recyclerView = findViewById(R.id.recycler_view_notes)

        // 1. Setup Database/ViewModel
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        // 2. Setup RecyclerView Adapter with interaction handlers
        noteAdapter = NoteAdapter(
            onItemClick = { note ->

                showUpdateDialog(note)
            },
            onItemLongClick = { note ->

                deleteNoteWithUndo(note)
            }
        )
        recyclerView.adapter = noteAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        noteViewModel.allNotes.observe(this) { notes ->
            noteAdapter.submitList(notes)
        }
        addButton.setOnClickListener {
            addNote()
        }


        createNotificationChannel()
        requestNotificationPermission()
    }

    /** Adds a new note to the database from the EditText fields. */
    private fun addNote() {
        val title = titleInput.text.toString().trim()
        val content = contentInput.text.toString().trim()

        if (title.isNotEmpty() && content.isNotEmpty()) {
            val newNote = Note(title = title, content = content)
            noteViewModel.insert(newNote)


            titleInput.setText("")
            contentInput.setText("")


            startReminderService()
        } else {
            Snackbar.make(recyclerView, "Please fill in both title and content.", Snackbar.LENGTH_SHORT).show()
        }
    }

    /** Handles note deletion (long press) and shows a Snackbar with an Undo action. (Part D) */
    private fun deleteNoteWithUndo(note: Note) {
        noteViewModel.delete(note)

        Snackbar.make(recyclerView, "Note deleted.", Snackbar.LENGTH_LONG)
            .setAction("Undo") {

                noteViewModel.insert(note)
            }.show()
    }


    private fun showUpdateDialog(note: Note) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_note, null)

        val titleEditText = dialogView.findViewById<EditText>(R.id.dialog_edit_title)
        val contentEditText = dialogView.findViewById<EditText>(R.id.dialog_edit_content)

        titleEditText.setText(note.title)
        contentEditText.setText(note.content)

        AlertDialog.Builder(this)
            .setTitle("Edit Note")
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val newTitle = titleEditText.text.toString().trim()
                val newContent = contentEditText.text.toString().trim()

                if (newTitle.isNotEmpty() && newContent.isNotEmpty()) {
                    val updatedNote = Note(
                        id = note.id,
                        title = newTitle,
                        content = newContent
                    )
                    noteViewModel.update(updatedNote)
                    Snackbar.make(recyclerView, "Note updated!", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(recyclerView, "Title and content cannot be empty.", Snackbar.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /** Creates the required notification channel for Android O and above. (Part E) */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Note Reminders"
            val descriptionText = "Channel for Note Reminder notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /** Requests POST_NOTIFICATIONS permission for Android 13 (API 33) and above. */
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), PERMISSION_REQUEST_CODE)
            }
        }
    }

    private fun startReminderService() {
        val serviceIntent = Intent(this, ReminderService::class.java)
        startService(serviceIntent)
    }
}

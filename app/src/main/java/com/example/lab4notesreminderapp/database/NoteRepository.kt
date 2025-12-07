/**
 * Course: MAD204-LAB4
 * Name: Ashish Prajapati
 * Student ID : A00194842
 * Date: DECEMBER 6, 2025
 */
package com.example.lab4notesreminderapp.database

import androidx.lifecycle.LiveData


class NoteRepository(private val noteDao: NoteDao) {

    // Retrieves all notes via LiveData
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    // Suspend functions for background database operations
    suspend fun insert(note: Note) {
        noteDao.insert(note)
    }

    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }
}
/**
 *
// * * Course: MAD204-LAB4
* Name: Ashish Prajapati
* Student ID : A00194842
* Date: DECEMBER 6, 2025

**/
package com.example.lab4notesreminderapp.database

import androidx.room.*
import androidx.lifecycle.LiveData

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)


    @Update
    suspend fun update(note: Note)


    @Delete
    suspend fun delete(note: Note)


    @Query("SELECT * FROM note_table ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<Note>>
}
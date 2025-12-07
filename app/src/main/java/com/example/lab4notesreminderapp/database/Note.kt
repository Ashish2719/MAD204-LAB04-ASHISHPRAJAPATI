/**
 * Course: MAD204-LAB4
 * Name: Ashish Prajapati
 * Student ID : A00194842
 * Date: DECEMBER 6, 2025
 */
package com.example.lab4notesreminderapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity class for storing notes. Fields: id, title, content[cite: 29].
 */
@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "note_title")
    val title: String,
    @ColumnInfo(name = "note_content")
    val content: String
)
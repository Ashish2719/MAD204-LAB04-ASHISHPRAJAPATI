/**
 * Course: MAD204-LAB4
 * Name: Ashish Prajapati
 * Student ID : A00194842
 * Date: DECEMBER 6, 2025
 */

package com.example.lab4notesreminderapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4notesreminderapp.database.Note

/**
 * Adapter for displaying notes in RecyclerView.
 * Handles click (update)  and long press (delete) [cite: 54] events.
 */
class NoteAdapter(
    private val onItemClick: (Note) -> Unit,
    private val onItemLongClick: (Note) -> Unit
) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NotesComparator()) {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
        private val contentTextView: TextView = itemView.findViewById(R.id.text_view_content)

        fun bind(note: Note) {
            titleTextView.text = note.title
            contentTextView.text = note.content


            itemView.setOnClickListener { onItemClick(note) }

            itemView.setOnLongClickListener {
                onItemLongClick(note)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false) // Uses item_note.xml [cite: 48]
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NotesComparator : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem
    }
}

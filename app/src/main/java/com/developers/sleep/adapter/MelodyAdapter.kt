package com.developers.sleep.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.developers.sleep.Melody
import com.developers.sleep.databinding.MelodyItemBinding

class MelodyAdapter(
    private val onMelodyClickListener: OnMelodyClickListener
) :
    ListAdapter<Melody, MelodyAdapter.MelodyViewHolder>(MelodyDiffCallback()) {

    interface OnMelodyClickListener {
        fun onMelodyClick(melody: Melody)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MelodyViewHolder {
        val binding =
            MelodyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MelodyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MelodyViewHolder, position: Int) {
        val melody = getItem(position)
        holder.bind(melody)
    }

    inner class MelodyViewHolder(private val binding: MelodyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val melody = getItem(position)
                    onMelodyClickListener.onMelodyClick(melody)
                }
            }
        }

        fun bind(melody: Melody) {
            binding.melodyName.text = melody.name
            binding.melodyPosition.text = (adapterPosition + 1).toString()
        }
    }
}

class MelodyDiffCallback : DiffUtil.ItemCallback<Melody>() {
    override fun areItemsTheSame(oldItem: Melody, newItem: Melody): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Melody, newItem: Melody): Boolean {
        return oldItem == newItem
    }
}

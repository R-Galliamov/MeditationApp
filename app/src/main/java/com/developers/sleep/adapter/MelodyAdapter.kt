package com.developers.sleep.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.developers.sleep.AlarmSound
import com.developers.sleep.R
import com.developers.sleep.databinding.AlarmSoundItemBinding

class MelodyAdapter(
    private val onMelodyClickListener: OnMelodyClickListener, private var selectedMelodyIndex : Int
) :
    ListAdapter<AlarmSound, MelodyAdapter.MelodyViewHolder>(MelodyDiffCallback()) {

    interface OnMelodyClickListener {
        fun onMelodyClick(alarmSound: AlarmSound, position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MelodyViewHolder {
        val binding =
            AlarmSoundItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MelodyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MelodyViewHolder, position: Int) {
        val melody = getItem(position)
        holder.bind(melody)
    }

    inner class MelodyViewHolder(private val binding: AlarmSoundItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val melody = getItem(position)
                    onMelodyClickListener.onMelodyClick(melody, position)
                    selectMelody(position)
                }
            }
        }

        fun bind(alarmSound: AlarmSound) {
            binding.melodyName.text = alarmSound.name

            if (adapterPosition == selectedMelodyIndex) {
                binding.radioImage.setImageResource(R.drawable.radio_selected)
            } else {
                binding.radioImage.setImageResource(R.drawable.radio_unselected)
            }

        }

        private fun selectMelody(position: Int) {
            val previousSelectedIndex = selectedMelodyIndex
            selectedMelodyIndex = position
            if (previousSelectedIndex != RecyclerView.NO_POSITION) {
                notifyItemChanged(previousSelectedIndex)
            }
            notifyItemChanged(selectedMelodyIndex)
        }
    }
}

class MelodyDiffCallback : DiffUtil.ItemCallback<AlarmSound>() {
    override fun areItemsTheSame(oldItem: AlarmSound, newItem: AlarmSound): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: AlarmSound, newItem: AlarmSound): Boolean {
        return oldItem == newItem
    }
}

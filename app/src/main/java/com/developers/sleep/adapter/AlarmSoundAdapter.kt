package com.developers.sleep.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.developers.sleep.dataModels.Melody
import com.developers.sleep.R
import com.developers.sleep.databinding.AlarmSoundItemBinding

class AlarmSoundAdapter(
    private val onAlarmSoundClickListener: OnAlarmSoundClickListener, private var selectedMelodyIndex : Int
) :
    ListAdapter<Melody, AlarmSoundAdapter.AlarmSoundViewHolder>(AlarmSoundDiffCallback()) {

    interface OnAlarmSoundClickListener {
        fun onMelodyClick(alarmSound: Melody, position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmSoundViewHolder {
        val binding =
            AlarmSoundItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmSoundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmSoundViewHolder, position: Int) {
        val melody = getItem(position)
        holder.bind(melody)
    }

    inner class AlarmSoundViewHolder(private val binding: AlarmSoundItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val melody = getItem(position)
                    onAlarmSoundClickListener.onMelodyClick(melody, position)
                    selectMelody(position)
                }
            }
        }

        fun bind(alarmSound: Melody) {
            binding.melodyName.text = alarmSound.name

            if (adapterPosition == selectedMelodyIndex) {
                binding.radioImage.setImageResource(R.drawable.radio_selected)
            } else {
                binding.radioImage.setImageResource(R.drawable.radio_unselected)
            }

        }

        private fun selectMelody(position: Int) {
            //TODO rewrite with melody name
            val previousSelectedIndex = selectedMelodyIndex
            selectedMelodyIndex = position
            if (previousSelectedIndex != RecyclerView.NO_POSITION) {
                notifyItemChanged(previousSelectedIndex)
            }
            notifyItemChanged(selectedMelodyIndex)
        }
    }
}

class AlarmSoundDiffCallback : DiffUtil.ItemCallback<Melody>() {
    override fun areItemsTheSame(oldItem: Melody, newItem: Melody): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Melody, newItem: Melody): Boolean {
        return oldItem == newItem
    }
}

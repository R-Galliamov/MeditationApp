package com.developers.sleep.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.developers.sleep.dataModels.Melody
import com.developers.sleep.R
import com.developers.sleep.dataModels.AlarmSound
import com.developers.sleep.databinding.AlarmSoundItemBinding

class AlarmSoundAdapter(
    private val onAlarmSoundClickListener: OnAlarmSoundClickListener, private var selectedAlarmIndex : Int
) :
    ListAdapter<AlarmSound, AlarmSoundAdapter.AlarmSoundViewHolder>(AlarmSoundDiffCallback()) {

    interface OnAlarmSoundClickListener {
        fun onMelodyClick(alarmSound: AlarmSound, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmSoundViewHolder {
        val binding =
            AlarmSoundItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmSoundViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmSoundViewHolder, position: Int) {
        val alarmSound = getItem(position)
        holder.bind(alarmSound)
    }

    inner class AlarmSoundViewHolder(private val binding: AlarmSoundItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val alarmSound = getItem(position)
                    onAlarmSoundClickListener.onMelodyClick(alarmSound, position)
                    selectAlarm(position)
                }
            }
        }

        fun bind(alarmSound: AlarmSound) {
            binding.melodyName.text = alarmSound.name

            if (adapterPosition == selectedAlarmIndex) {
                binding.radioImage.setImageResource(R.drawable.radio_selected)
            } else {
                binding.radioImage.setImageResource(R.drawable.radio_unselected)
            }

        }

        private fun selectAlarm(position: Int) {
            //TODO rewrite with melody name
            val previousSelectedIndex = selectedAlarmIndex
            selectedAlarmIndex = position
            if (previousSelectedIndex != RecyclerView.NO_POSITION) {
                notifyItemChanged(previousSelectedIndex)
            }
            notifyItemChanged(selectedAlarmIndex)
        }
    }
}

class AlarmSoundDiffCallback : DiffUtil.ItemCallback<AlarmSound>() {
    override fun areItemsTheSame(oldItem: AlarmSound, newItem: AlarmSound): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: AlarmSound, newItem: AlarmSound): Boolean {
        return oldItem == newItem
    }
}

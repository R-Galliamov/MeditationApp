package com.developers.sleep.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.developers.sleep.R
import com.developers.sleep.dataModels.AlarmSound
import com.developers.sleep.databinding.AlarmSoundItemBinding

class AlarmSoundAdapter(
    private val onAlarmSoundClickListener: OnAlarmSoundClickListener, private var selectedAlarmSound : AlarmSound
) :
    ListAdapter<AlarmSound, AlarmSoundAdapter.AlarmSoundViewHolder>(AlarmSoundDiffCallback()) {

    interface OnAlarmSoundClickListener {
        fun onMelodyClick(alarmSound: AlarmSound)
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
                    onAlarmSoundClickListener.onMelodyClick(alarmSound)
                    selectedAlarmSound = alarmSound
                    notifyDataSetChanged()
                }
            }
        }

        fun bind(alarmSound: AlarmSound) {
            binding.melodyName.text = alarmSound.name
            binding.radioImage.setImageResource(R.drawable.radio_unselected)
            if (alarmSound.name == selectedAlarmSound.name) {
                binding.radioImage.setImageResource(R.drawable.radio_selected)
            }
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

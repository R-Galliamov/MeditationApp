package com.developers.sleep.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.developers.sleep.dataModels.PlayList
import com.developers.sleep.R
import com.developers.sleep.databinding.PlaylistTagItemBinding

class PlayListAdapter(
    private val onTagClickListener: OnTagClickListener, private var selectedPlayList: PlayList
) :
    ListAdapter<PlayList, PlayListAdapter.PlayListViewHolder>(PlayListDiffCallback()) {

    interface OnTagClickListener {
        fun onTagClick(playList: PlayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val binding =
            PlaylistTagItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PlayListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        val playList = getItem(position)
        holder.bind(playList)
    }

    inner class PlayListViewHolder(private val binding: PlaylistTagItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    selectedPlayList = getItem(position)
                    notifyDataSetChanged()
                    onTagClickListener.onTagClick(selectedPlayList)
                }
            }
        }

        fun bind(playList: PlayList) {
            binding.playListNameText.text = playList.name
            binding.itemBackground.background = null
            if (playList.name == selectedPlayList.name) {
                binding.itemBackground.setBackgroundResource(R.drawable.rounded_button_blue)
            }
        }
    }
}

class PlayListDiffCallback : DiffUtil.ItemCallback<PlayList>() {
    override fun areItemsTheSame(oldItem: PlayList, newItem: PlayList): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: PlayList, newItem: PlayList): Boolean {
        return oldItem == newItem
    }
}

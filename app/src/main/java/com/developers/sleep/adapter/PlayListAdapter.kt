package com.developers.sleep.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.developers.sleep.dataModels.Playlist
import com.developers.sleep.R
import com.developers.sleep.databinding.PlaylistTagItemBinding

class PlayListAdapter(
    private val onTagClickListener: OnTagClickListener, private var selectedPlaylist: Playlist
) :
    ListAdapter<Playlist, PlayListAdapter.PlayListViewHolder>(PlayListDiffCallback()) {

    interface OnTagClickListener {
        fun onTagClick(playList: Playlist)
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
                    selectedPlaylist = getItem(position)
                    notifyDataSetChanged()
                    onTagClickListener.onTagClick(selectedPlaylist)
                }
            }
        }

        fun bind(playList: Playlist) {
            binding.playListNameText.text = playList.name
            binding.itemBackground.background = null
            if (playList.name == selectedPlaylist.name) {
                binding.itemBackground.setBackgroundResource(R.drawable.rounded_button_blue)
            }
        }
    }
}

class PlayListDiffCallback : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem == newItem
    }
}

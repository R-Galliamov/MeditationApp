package com.developers.sleep.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.developers.sleep.dataModels.Melody
import com.developers.sleep.PACKAGE_NAME
import com.developers.sleep.PLAYLIST_LIST
import com.developers.sleep.dataModels.Playlist
import com.developers.sleep.R
import com.developers.sleep.adapter.MelodyAdapter
import com.developers.sleep.adapter.PlayListAdapter
import com.developers.sleep.databinding.FragmentMelodiesBinding
import com.developers.sleep.viewModel.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MelodiesFragment : Fragment() {
    private var _binding: FragmentMelodiesBinding? = null
    private val binding: FragmentMelodiesBinding
        get() = _binding!!

    private lateinit var melodiesRecyclerView: RecyclerView
    private lateinit var melodyAdapter: MelodyAdapter
    private val playerViewModel: PlayerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMelodiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistsList = PLAYLIST_LIST
        val playListAdapter = PlayListAdapter(object : PlayListAdapter.OnTagClickListener {
            override fun onTagClick(playlist: Playlist) {
                showPlaylist(playlist)
                playerViewModel.setCurrentPlaylist(playlist)
            }
        }, playlistsList[0])


        melodiesRecyclerView = binding.recyclerViewMelodies
        melodyAdapter = MelodyAdapter(object : MelodyAdapter.OnMelodyClickListener {
            override fun onMelodyClick(melody: Melody) {
                playerViewModel.setCurrentMelody(melody)
                findNavController().navigate(R.id.action_mainFragment_to_playerFragment)
            }
        })

        melodiesRecyclerView.adapter = melodyAdapter

        showPlaylist(playlistsList.first { it.name == "Top" })

        val tagRecyclerView = binding.recyclerViewTags
        tagRecyclerView.adapter = playListAdapter
        playListAdapter.submitList(playlistsList)
    }

    private fun getDrawableResourceByName(name: String): Int {
        val formattedName = name.replace(" ", "_").lowercase()
        val drawableResId = resources.getIdentifier(formattedName, "drawable", PACKAGE_NAME)
        return if (drawableResId != 0) {
            drawableResId
        } else {
            R.drawable.sleepwaves
        }
    }

    private fun showPlaylist(playList: Playlist) {
        melodyAdapter.submitList(playList.melodiesList)
        binding.playListImage.setBackgroundResource(getDrawableResourceByName(playList.name))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

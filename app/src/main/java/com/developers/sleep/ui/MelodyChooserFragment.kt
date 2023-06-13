package com.developers.sleep.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.developers.sleep.PACKAGE_NAME
import com.developers.sleep.PLAYLIST_LIST
import com.developers.sleep.R
import com.developers.sleep.adapter.MelodyAdapter
import com.developers.sleep.adapter.PlayListAdapter
import com.developers.sleep.dataModels.Melody
import com.developers.sleep.dataModels.Playlist
import com.developers.sleep.databinding.FragmentMelodyChooserBinding
import com.developers.sleep.viewModel.AlarmPlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MelodyChooserFragment : Fragment() {
    private var _binding: FragmentMelodyChooserBinding? = null
    private val binding: FragmentMelodyChooserBinding
        get() = _binding!!

    private lateinit var melodiesRecyclerView: RecyclerView
    private lateinit var melodyAdapter: MelodyAdapter
    private val alarmPlayerViewModel: AlarmPlayerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMelodyChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistsList = PLAYLIST_LIST

        val playlistAdapter = PlayListAdapter(object : PlayListAdapter.OnTagClickListener {
            override fun onTagClick(playlist: Playlist) {
                alarmPlayerViewModel.setCurrentPlaylist(playlist)
            }
        }, alarmPlayerViewModel.currentPlaylist.value!!)

        melodiesRecyclerView = binding.recyclerViewMelodies
        melodyAdapter = MelodyAdapter(object : MelodyAdapter.OnMelodyClickListener {
            override fun onMelodyClick(melody: Melody) {
                if (!melody.isPremium) {
                    alarmPlayerViewModel.setCurrentMelody(melody)
                    findNavController().navigateUp()
                } else {
                    findNavController().navigate(R.id.action_melodyChooserFragment_to_paywallFragment)
                }
            }
        })
        melodiesRecyclerView.adapter = melodyAdapter

        val tagRecyclerView = binding.recyclerViewTags
        tagRecyclerView.adapter = playlistAdapter
        playlistAdapter.submitList(playlistsList)

        alarmPlayerViewModel.currentPlaylist.observe(viewLifecycleOwner){
            showPlaylist(it)
        }


        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

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

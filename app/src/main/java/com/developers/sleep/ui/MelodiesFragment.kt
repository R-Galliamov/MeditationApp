package com.developers.sleep.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.developers.sleep.dataModels.Melody
import com.developers.sleep.PACKAGE_NAME
import com.developers.sleep.dataModels.PlayList
import com.developers.sleep.R
import com.developers.sleep.adapter.MelodyAdapter
import com.developers.sleep.adapter.PlayListAdapter
import com.developers.sleep.databinding.FragmentMelodiesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MelodiesFragment : Fragment() {
    private var _binding: FragmentMelodiesBinding? = null
    private val binding: FragmentMelodiesBinding
        get() = _binding!!

    private lateinit var melodiesRecyclerView: RecyclerView
    private lateinit var melodyAdapter: MelodyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMelodiesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val melody1 = Melody("Song 1", "file1.mp3")
        val melody2 = Melody("Song 2", "file2.mp3")
        val playlist2 = PlayList("Relaxation", listOf(melody2))
        val melody3 = Melody("Nature", "file3.mp3")
        val playlist3 = PlayList("Nature", listOf(melody3))
        val playlist4 = PlayList("Delta waves", listOf(melody3))
        val playlist5 = PlayList("Bedtime stories", listOf(melody3))
        val playlist6 = PlayList("For kids", listOf(melody3))
        val playlist7 = PlayList("ASMR", listOf(melody3))
        val playlist8 = PlayList("Sleep", listOf(melody3))
        val playlist1 = PlayList("Top", listOf(melody1, melody2, melody3))

        val playlistsList = listOf(
            playlist1,
            playlist2,
            playlist3,
            playlist4,
            playlist5,
            playlist6,
            playlist7,
            playlist8
        )

        val playListAdapter = PlayListAdapter(object : PlayListAdapter.OnTagClickListener {
            override fun onTagClick(playList: PlayList) {
                showPlaylist(playList)
            }
        }, playlist1)


        melodiesRecyclerView = binding.recyclerViewMelodies
        melodyAdapter = MelodyAdapter(object : MelodyAdapter.OnMelodyClickListener {
            override fun onMelodyClick(melody: Melody) {
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


    private fun showPlaylist(playList: PlayList) {
        melodyAdapter.submitList(playList.melodiesList)
        binding.playListImage.setBackgroundResource(getDrawableResourceByName(playList.name))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

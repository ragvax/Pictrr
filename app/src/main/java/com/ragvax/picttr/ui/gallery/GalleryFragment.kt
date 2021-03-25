package com.ragvax.picttr.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ragvax.picttr.R
import com.ragvax.picttr.data.photo.model.Photo
import com.ragvax.picttr.data.topic.model.Topic
import com.ragvax.picttr.databinding.FragmentGalleryBinding
import com.ragvax.picttr.ui.gallery.adapter.GalleryAdapter
import com.ragvax.picttr.ui.gallery.adapter.GalleryLoadStateAdapter
import com.ragvax.picttr.ui.gallery.adapter.GalleryTopicsAdapter
import com.ragvax.picttr.utils.collectWhileStarted
import com.ragvax.picttr.utils.dpToPixels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery),
    GalleryTopicsAdapter.OnItemClickListener, GalleryAdapter.OnItemClickListener {

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private lateinit var photosAdapter: GalleryAdapter
    private lateinit var topicsAdapter: GalleryTopicsAdapter

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGalleryBinding.bind(view)

        setupPhotosAdapter()
        observeViewModel()
        photosLoadStateListener()
    }

    private fun setupPhotosAdapter() {
        photosAdapter = GalleryAdapter(this)
        val headerAdapter = GalleryLoadStateAdapter{ photosAdapter.retry() }
        val footerAdapter = GalleryLoadStateAdapter{ photosAdapter.retry() }
        val concatAdapter = photosAdapter.withLoadStateHeaderAndFooter(
            header = headerAdapter,
            footer = footerAdapter,
        )
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE

        binding.apply {
            rvGallery.layoutManager = staggeredGridLayoutManager
            rvGallery.setHasFixedSize(true)
            rvGallery.adapter = concatAdapter
            rvGallery.addItemDecoration(GalleryGridSpacingItemDecoration(16.dpToPixels(requireContext())))
        }
    }

    private fun setupTopicsAdapter(topics: List<Topic>) {
        topicsAdapter = GalleryTopicsAdapter(topics, this)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        binding.apply {
            rvGalleryTags.layoutManager = layoutManager
            rvGalleryTags.setHasFixedSize(true)
            rvGalleryTags.adapter = topicsAdapter
        }
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.photosFlow.collectLatest {
                Log.i("Gallery", "$it")
                photosAdapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated    {
            viewModel.topicsFlow.collectLatest {
                when (it) {
                    is GalleryViewModel.TopicsEvent.Success -> {
                        setupTopicsAdapter(it.topics)
                        Toast.makeText(requireContext(), "Topics is Loaded", Toast.LENGTH_SHORT).show()
                    }
                    is GalleryViewModel.TopicsEvent.Empty -> {
                        Toast.makeText(requireContext(), "Topics is Empty", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        viewModel.galleryEvent.collectWhileStarted(viewLifecycleOwner) { event ->
            when (event) {
                is GalleryViewModel.GalleryEvent.NavigateToPhotoDetailsFragment -> {
                    val action = GalleryFragmentDirections.actionGalleryFragmentToPhotoDetailsFragment(event.photo)
                    findNavController().navigate(action)

                }
            }
        }
    }

    private fun photosLoadStateListener() {
        photosAdapter.addLoadStateListener { combinedLoadStates ->
            binding.apply {
                rvGallery.isVisible = combinedLoadStates.source.refresh is LoadState.NotLoading
                progressCircularLoading.isVisible = combinedLoadStates.source.refresh is LoadState.Loading
            }
        }
    }

    override fun onItemClick(id: String) {
        viewModel.onTopicSelected(id)
        (binding.rvGallery.layoutManager as StaggeredGridLayoutManager)
            .scrollToPositionWithOffset(0, 0)
    }

    override fun onPhotoClick(photo: Photo) {
        viewModel.onPhotoSelected(photo)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
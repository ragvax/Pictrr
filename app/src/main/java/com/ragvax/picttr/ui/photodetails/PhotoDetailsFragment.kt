package com.ragvax.picttr.ui.photodetails

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ragvax.picttr.R
import com.ragvax.picttr.data.photo.model.Photo
import com.ragvax.picttr.databinding.FragmentPhotoDetailsBinding
import com.ragvax.picttr.utils.collectWhileStarted
import com.ragvax.picttr.utils.loadPhotoUrlWithThumbnail
import com.ragvax.picttr.utils.toPrettyString
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PhotoDetailsFragment : Fragment(R.layout.fragment_photo_details) {
    private val viewModel by viewModels<PhotoDetailsViewModel>()
    private val args: PhotoDetailsFragmentArgs by navArgs()

    private var _binding: FragmentPhotoDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPhotoDetailsBinding.bind(view)
        val photo = args.photo
        // TODO: REFACTOR METHOD!
        viewModel.getPhotoDetails(photo.id)

        initView(photo)
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.photoDetails.collectLatest {
                when (it) {
                    is PhotoDetailsViewModel.PhotoDetails.Success -> {
                        bindDetails(it.photoDetails)
                        setDetailsVisibility(true)
                    }
                    is PhotoDetailsViewModel.PhotoDetails.Empty -> {
                        setDetailsVisibility(false)
                    }
                }
            }
        }

        viewModel.photoDetailsEvent.collectWhileStarted(viewLifecycleOwner) { event ->
            when (event) {
                is PhotoDetailsViewModel.PhotoDetailsEvent.NavigateToPhotoZoom -> {
                    val action = PhotoDetailsFragmentDirections.actionDetailsFragmentToPhotoZoomFragment(event.photo)
                    findNavController().navigate(action)
                }
            }
        }
    }

    private fun initView(photo: Photo) = with(binding) {
        ivPhoto.loadPhotoUrlWithThumbnail(photo.urls.full, photo.urls.small, photo.color)
        ivPhoto.setOnClickListener { viewModel.onPhotoClick(photo) }
        tvUserUsername.text = photo.user?.name
        tvImageDescription.text = photo.description ?: "No description"
        tvLocation.text = if (photo.location != null)
            photo.location.city + ", " + photo.location.country
        else
            "No location"
    }

    private fun bindDetails(photo: Photo) = with(binding) {
            tvItemLikes.text = (photo.likes ?: 0).toPrettyString()
            tvItemDownloads.text = (photo.downloads ?: 0).toPrettyString()
            tvItemViews.text = (photo.views ?: 0).toPrettyString()

            tvItemCamera.text = photo.exif?.model ?: "Unknown"
            tvItemAperture.text = photo.exif?.aperture ?: "Unknown"
            tvItemFocalLength.text = photo.exif?.focal_length ?: "Unknown"
            tvItemShutter.text = photo.exif?.exposure_time ?: "Unknown"
            tvItemIso.text = photo.exif?.iso?.toString() ?: "Unknown"
            tvItemDimension.text = "${photo.width} x ${photo.height}"
    }

    private fun setDetailsVisibility(boolean: Boolean) = with(binding) {
            tvTitleLikes.isVisible = boolean
            tvTitleDownloads.isVisible = boolean
            tvTitleViews.isVisible = boolean
            tvItemLikes.isVisible = boolean
            tvItemDownloads.isVisible = boolean
            tvItemViews.isVisible = boolean

            tvTitleCamera.isVisible = boolean
            tvTitleAperture.isVisible = boolean
            tvTitleFocalLength.isVisible = boolean
            tvTitleShutter.isVisible = boolean
            tvTitleIso.isVisible = boolean
            tvTitleDimension.isVisible = boolean
            tvItemCamera.isVisible = boolean
            tvItemAperture.isVisible = boolean
            tvItemFocalLength.isVisible = boolean
            tvItemShutter.isVisible = boolean
            tvItemIso.isVisible = boolean
            tvItemDimension.isVisible = boolean
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
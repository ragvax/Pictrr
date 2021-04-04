package com.ragvax.picttr.ui.photozoom

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ragvax.picttr.R
import com.ragvax.picttr.data.photo.model.Photo
import com.ragvax.picttr.databinding.FragmentPhotoZoomBinding
import com.ragvax.picttr.utils.collectWhileStarted
import com.ragvax.picttr.utils.loadPhotoUrlWithThumbnail
import com.ragvax.picttr.utils.loadProfilePicture
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoZoomFragment : Fragment(R.layout.fragment_photo_zoom) {
    private val viewModel: PhotoZoomViewModel by viewModels()
    private val args: PhotoZoomFragmentArgs by navArgs()

    private var _binding: FragmentPhotoZoomBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPhotoZoomBinding.bind(view)
        val photo = args.photo

        initView(photo)
        observeViewModel()
    }

    private fun initView(
        photo: Photo,
    ) {
        binding.apply {
            ivPhoto.loadPhotoUrlWithThumbnail(photo.urls.full, photo.urls.regular, photo.color,false)
            ivProfilePicture.loadProfilePicture(photo.user!!)
            tvUserUsername.text = photo.user.name
            tvUserUsername.setOnClickListener {
                viewModel.onUserClick(photo.user)
            }
            tvImageSize.text = getString(
                R.string.image_size_template,
                photo.width.toString(),
                photo.height.toString()
            )
        }
    }

    private fun observeViewModel() {
        viewModel.photoZoomEvent.collectWhileStarted(viewLifecycleOwner) { event ->
            when (event) {
                is PhotoZoomViewModel.PhotoZoomEvent.NavigateToUserProfile -> {
                    val action = PhotoZoomFragmentDirections.actionPhotoZoomFragmentToProfileFragment(event.user!!)
                    findNavController().navigate(action)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.ragvax.picttr.ui.photodetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ragvax.picttr.R
import com.ragvax.picttr.databinding.FragmentPhotoZoomBinding
import com.ragvax.picttr.utils.loadPhotoUrlWithThumbnail

class PhotoZoomFragment : Fragment(R.layout.fragment_photo_zoom) {
    private val args: PhotoZoomFragmentArgs by navArgs()

    private var _binding: FragmentPhotoZoomBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPhotoZoomBinding.bind(view)

        val photo = args.photo
        binding.ivPhoto.loadPhotoUrlWithThumbnail(photo.urls.full, photo.urls.regular, photo.color, false)
    }
}
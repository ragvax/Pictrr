package com.ragvax.picttr.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import com.ragvax.picttr.GlideApp

fun ImageView.loadPhotoUrlWithThumbnail(
    url: String,
    thumbnailUrl: String,
    color: String?,
    centerCrop: Boolean = true,
) {
    color?.let { background = ColorDrawable(Color.parseColor(it)) }
    GlideApp.with(context)
        .load(url)
        .thumbnail(
            if (centerCrop) {
                GlideApp.with(context).load(thumbnailUrl).centerCrop()
            } else {
                GlideApp.with(context).load(thumbnailUrl).thumbnail(0.05f)
            }
        )
        .into(this)
}
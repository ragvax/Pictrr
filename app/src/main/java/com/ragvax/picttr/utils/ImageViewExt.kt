package com.ragvax.picttr.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ragvax.picttr.GlideApp
import com.ragvax.picttr.data.user.model.User

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

fun ImageView.loadGridPhotoUrl(
    url: String,
    color: String?,
) {
    color?.let { background = ColorDrawable(Color.parseColor(it)) }
    GlideApp.with(context)
        .load(url)
        .thumbnail(0.05f)
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun ImageView.loadProfilePicture(user: User) {
    loadProfilePicture(user.profile_image?.large)
}

fun ImageView.loadProfilePicture(url: String?) {
    GlideApp.with(context)
        .load(url)
        .circleCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}
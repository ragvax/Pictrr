package com.ragvax.picttr.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.ragvax.picttr.data.user.model.Links

fun Context.openLocationInMaps(location: String?) {
    val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(location)}")
    val intent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
    val mapsPackageName = "com.google.android.apps.maps"
    val mapsIsInstalled = try {
        packageManager.getApplicationInfo(mapsPackageName, 0).enabled
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
    if (mapsIsInstalled) intent.setPackage(mapsPackageName)
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

fun Context.openProfileInBrowser(links: Links?) {
    val uri = Uri.parse("${links?.html}?utm_source=Picttr&utm_medium=referral")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    startActivity(intent)
}
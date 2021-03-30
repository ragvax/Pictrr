package com.ragvax.picttr.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri

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
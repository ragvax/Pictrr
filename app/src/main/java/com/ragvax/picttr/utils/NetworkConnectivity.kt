package com.ragvax.picttr.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.annotation.RequiresApi
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class Network @Inject constructor(
    context: Context
) : NetworkConnectivity,
    ConnectivityManager.NetworkCallback() {

    private val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var isOnline = false

    init {
        if (Build.VERSION.SDK_INT >= 24) {
            cm.registerDefaultNetworkCallback(this)
        }
    }

    override fun getNetworkInfo(): NetworkInfo? {
        return cm.activeNetworkInfo
    }

    override fun isConnected(): Boolean {
        if (Build.VERSION.SDK_INT >= 24) {
            return isOnline
        }
        val info = getNetworkInfo()
        return info != null && info.isConnected
    }

    override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
        isOnline = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onLost(network: Network) {
        isOnline = false
    }
}

interface NetworkConnectivity {
    fun getNetworkInfo(): NetworkInfo?
    fun isConnected(): Boolean
}
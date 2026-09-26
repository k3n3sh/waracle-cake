package com.waracle.cakes.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class AndroidNetworkMonitor(private val context: Context) : NetworkMonitor {

    override suspend fun isOnline(): Boolean {
        // can't tell, so let the request try
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java) ?: return true
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            ?: return false
        // VALIDATED = Android actually reached the internet on this network
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}

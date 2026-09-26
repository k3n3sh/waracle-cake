package com.waracle.cakes.data.network

interface NetworkMonitor {
    suspend fun isOnline(): Boolean
}

package com.will.busnotification.repository

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /** Returns a best-effort last known Location (lat/lng) or null if unavailable. Caller must ensure permissions. */
    fun getLastKnownLocation(): Location? {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return null
        val providers = listOf(
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER,
            LocationManager.PASSIVE_PROVIDER
        )

        var best: Location? = null
        for (p in providers) {
            try {
                val loc = lm.getLastKnownLocation(p)
                if (loc != null) {
                    if (best == null || loc.accuracy < best.accuracy) {
                        best = loc
                    }
                }
            } catch (_: SecurityException) {
                // Permission not granted — caller should handle permissions. Continue to next provider.
            } catch (_: Exception) {
                // Ignore other provider-specific errors and continue
            }
        }
        return best
    }

    /**
     * Geocode a textual address into latitude/longitude using Android Geocoder.
     * Returns Pair(latitude, longitude) or null if not found or if Geocoder fails.
     */
    fun geocodeAddress(address: String): Pair<Double, Double>? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val list = geocoder.getFromLocationName(address, 1)
            val res = list?.firstOrNull() ?: return null
            Pair(res.latitude, res.longitude)
        } catch (_: Exception) {
            null
        }
    }
}

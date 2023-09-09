package com.udacity.project4.locationreminders.geofence

import java.util.concurrent.TimeUnit

internal object GeofenceConstants {
    /**
     * Used to set an expiration time for a geofence. After this amount of time, Location services
     * stops tracking the geofence. For this sample, geofences expire after one hour.
     */
    val GEOFENCE_EXPIRATION_IN_MILLISECONDS: Long = TimeUnit.HOURS.toMillis(1)

    const val GEOFENCE_RADIUS_IN_METERS = 100f
}
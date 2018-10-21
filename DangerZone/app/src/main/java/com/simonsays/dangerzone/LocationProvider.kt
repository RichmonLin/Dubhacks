package com.simonsays.dangerzone

import com.google.android.gms.maps.model.LatLng

interface LocationProvider {
    fun getUserLocation(): LatLng
}
package com.coinz.app

import java.text.SimpleDateFormat
import java.util.*

/**
 * Map URL utility.
 *
 * This class handles the computation of the map URL and can be used to retrieve it.
 *
 * @property date Date for which to construct map URL.
 * @constructor Create map URL from date.
 */
class MapURL(val date: Date) {

    // TODO: Should this be part of the constructor? I.e. have string constants passed in here?
    companion object {
        val baseAddress = "http://homepages.inf.ed.ac.uk/stg/coinz"
        val dateFormat = "yyyy/MM/dd"
        val mapFilename = "coinzmap.geojson"
    }

    var url: String = ""

    init {
        url = buildUrl()
    }

    /**
     * Build map URL from given date.
     * @return Map URL for given date.
     */
    private fun buildUrl(): String {
        val dateStr = SimpleDateFormat(dateFormat).format(date)
        return "$baseAddress/$dateStr/$mapFilename"
    }

}
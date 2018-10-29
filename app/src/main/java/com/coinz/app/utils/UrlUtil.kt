package com.coinz.app.utils

import java.util.*

/**
 * URL utility.
 *
 * This class provides convenience functions around application relevant URLs.
 */
object UrlUtil {

    /**
     * Return Map URL.
     *
     * @param baseUrl Base URL of map, see AppStrings.mapBaseUrl for default value.
     * @param date Date the map is generated for, default is current date.
     * @param mapFilename Filename of map file on server, see AppStrings.mapFilename for default.
     */
    fun mapUrl(baseUrl: String = AppStrings.mapBaseUrl,
               date: Date = Calendar.getInstance().time,
               mapFilename: String = AppStrings.mapFilename): String {
        return "$baseUrl/${DateUtil.formatDate(date)}/$mapFilename"
    }

}
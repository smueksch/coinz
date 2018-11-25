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
     * @param baseUrl Base URL of map, see AppConsts.mapBaseUrl for default value.
     * @param date Date the map is generated for, default is current date.
     * @param mapFilename Filename of map file on server, see AppConsts.mapFilename for default.
     */
    fun mapUrl(baseUrl: String = AppConsts.mapBaseUrl,
               date: Date = Calendar.getInstance().time,
               mapFilename: String = AppConsts.mapFilename): String {
        return "$baseUrl/${DateUtil.formatDate(date)}/$mapFilename"
    }

}
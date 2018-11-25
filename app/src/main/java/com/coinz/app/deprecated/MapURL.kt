package com.coinz.app.deprecated

import com.coinz.app.utils.AppStrings
import com.coinz.app.utils.DateUtil
import java.util.*

// TODO: Might need complete re-doing with new database code.

/**
 * Map URL utility.
 *
 * This class handles the computation of the map URL and can be used to retrieve it.
 *
 * @constructor Create map URL from base address, date and filename.
 */
// TODO: Remove as soon as doesn't break code anymore.
@Deprecated("Use UrlUtil.mapUrl() instead.")
class MapURL(baseAddress: String = AppStrings.mapBaseUrl,
             date: Date = Calendar.getInstance().time,
             mapFilename: String = AppStrings.mapFilename) {

    /**
     * Map URL constructed from base address, date and filename.
     */
    var url: String = "$baseAddress/${DateUtil.formatDate(date)}/$mapFilename"

}
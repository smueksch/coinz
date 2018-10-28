package com.coinz.app.utils

import java.text.SimpleDateFormat
import java.util.*

/*
 * Date utility object.
 *
 * Includes specialized methods for the need of this application, such as getting the current
 * date in a standard app format.
 */
object DateUtil {

    /**
     * Standard app date formatter.
     */
    private val formatter = SimpleDateFormat(AppStrings.dateFormat)

    /**
     * Get current date in app format.
     *
     * @return Current date in app format as string.
     */
    fun currentDate() = formatter.format(Calendar.getInstance().time)

}
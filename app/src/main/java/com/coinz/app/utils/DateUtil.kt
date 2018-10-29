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
     * Get given date as string in standard app format.
     *
     * @param date Date to be formatted.
     * @return Date as string formatted in standard app format.
     */
    fun formatDate(date: Date): String = formatter.format(date)

    /**
     * Get current date in app format.
     *
     * @return Current date in app format as string.
     */
    fun currentDate() = formatDate(Calendar.getInstance().time)

}
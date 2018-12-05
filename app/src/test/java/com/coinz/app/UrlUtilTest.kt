package com.coinz.app

import com.coinz.app.utils.UrlUtil
import org.junit.Test
import java.util.*

import org.junit.Assert.*
import org.junit.Before

/**
 * UrlUtil functionality test.
 *
 * Check whether the UrlUtil class provides correct results for given situations.
 */
class UrlUtilTest {

    // Default date values used to set the date so the tests can run in a consistent way.
    private val defaultYear = 2018
    // Default month is December, but months start at 0 in Calendar class.
    private val defaultMonth = 11
    private val defaultDate = 1

    // Calendar instance used to change date so we can test UrlUtil in a consistent way.
    private val cal = Calendar.getInstance()

    init {
        // Set the date to the default one for testing purposes.
        cal.set(defaultYear, defaultMonth, defaultDate)
    }

    @Before
    fun setUp() {
        cal.set(defaultYear, defaultMonth, defaultDate)
    }

    /**
     * Check whether UrlUtil.mapUrl returns correct URL if using the default parameters.
     */
    @Test
    fun assertDefaultUrl() {
        // Expected default URL is the coin map URL for the current day (set above)
        val expectedUrl = "http://homepages.inf.ed.ac.uk/stg/coinz/2018/12/01/coinzmap.geojson"

        // Need to inject a non-default date value so we can consistently test the function even
        // if actual date changes.
        assertEquals(expectedUrl, UrlUtil.mapUrl(date = cal.time))
    }

    /**
     * Check whether UrlUtil.mapUrl returns correct URL if base URL is changed.
     */
    @Test
    fun assertChangedBaseUrl() {
        // Pick an arbitrary new base URL not related to the default one.
        val newBaseUrl = "path.is.ed"
        // Returned URL should now start with the new base URL, still for the date set above.
        val expectedUrl = "path.is.ed/2018/12/01/coinzmap.geojson"

        // Need to inject a non-default date value so we can consistently test the function even
        // if actual date changes.
        assertEquals(expectedUrl, UrlUtil.mapUrl(newBaseUrl, date = cal.time))
    }

    /**
     * Check whether UrlUtil.mapUrl returns correct URL if map filename is changed.
     */
    @Test
    fun assertChangedFilenameUrl() {
        // Pick an arbitrary new fielname not related to the default one.
        val newFilename = "path.is.ed"
        // Returned URL should now start with the new base URL, still for the date set above.
        val expectedUrl = "http://homepages.inf.ed.ac.uk/stg/coinz/2018/12/01/path.is.ed"

        // Need to inject a non-default date value so we can consistently test the function even
        // if actual date changes.
        assertEquals(expectedUrl, UrlUtil.mapUrl(date = cal.time, mapFilename = newFilename))
    }

    /**
     * Check whether UrlUtil.mapUrl returns different URLs for different dates.
     */
    @Test
    fun assertDifferentDatesUrlDifferent() {
        // Get the URL for the current testing date.
        // Need to inject a non-default date value so we can consistently test the function even
        // if actual date changes.
        val currentDateUrl = UrlUtil.mapUrl(date = cal.time)

        // Set the current date to a new one to hopefully generate a new map URL.
        cal.set(defaultYear + 1, defaultMonth, defaultDate)

        val newDateUrl = UrlUtil.mapUrl(date = cal.time)

        assertNotEquals(currentDateUrl, newDateUrl)
    }
}
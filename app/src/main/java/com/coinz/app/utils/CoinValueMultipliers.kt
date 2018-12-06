package com.coinz.app.utils

import com.coinz.app.database.entities.Coin
import java.util.*

/**
 * Object handling the coin value multipliers used for the bonus features.
 *
 * A coin value multiplier is a number which is multiplied by the actual value of the coin, i.e.
 * the value given in the map file, to provide the value of the coin when collected, a stored value.
 * The use cases are the two bonus features, which both provide a higher stored value of a coin when
 * it is collected under certain conditions, e.g. a certain marker symbol on a certain day of month.
 *
 * There are two ways to use this object to apply all relevant multipliers to the coin value:
 *
 * Usage 1:
 * <pre>
 *     appliedMultipliersCoin = CoinValueMultipliers.applyMultipliers(coin)
 * </pre>
 *
 * Usage 2:
 * <pre>
 *     appliedMultipliersCoin = CoinValueMultipliers * coin
 * </pre>
 *
 * Note that the following will not work:
 * <pre>
 *     appliedMultipliersCoin = coin * CoinValueMultipliers
 * </pre>
 */
object CoinValueMultipliers {

    // Default multiplier, used when a certain multiplier is not applicable.
    private const val defaultMultiplier = 1.0

    /**
     * Convenience method to make applying multipliers visually neater.
     *
     * This functions essentially just wraps {@code applyMultipliers} to allow for the following
     * application of the multipliers:
     *
     * <pre>
     *     appliedMultipliersCoin = CoinValueMultipliers * coin
     * </pre>
     *
     * @param coin Coin for which to apply all relevant multipliers.
     *
     * @return Coin with relevant multipliers applied to it, i.e. correct stored value.
     *
     * @see applyMultipliers
     */
    operator fun times(coin: Coin): Coin {
        return applyMultipliers(coin)
    }

    /**
     * Return coin with all relevant multipliers applied to it
     *
     * @param coin Coin for which to apply all relevant multipliers.
     *
     * @return Coin with relevant multipliers applied to it, i.e. correct stored value.
     */
    fun applyMultipliers(coin: Coin): Coin {
        // Multiplier which will be set according to the relevance of the coin's marker color.
        var multiplier = defaultMultiplier

        // Check if coin has relevant marker color for the given time of day.
        if (hasRelevantColor(coin)) {
            // Coin has relevant color, multiply in the color multiplier.
            multiplier *= AppConsts.colorMultiplier
        }

        // Check if coin has relevant marker symbol for the day of the month.
        if (hasRelevantSymbol(coin)) {
            // Coin has relevant marker symbol, multiply in the symbol multiplier
            multiplier *= AppConsts.symbolMultiplier
        }

        // Apply multiplier computed earlier to the coin value and store it in the coin.
        coin.storedValue = coin.originalValue * multiplier

        // Return modified coin.
        return coin
    }

    /**
     * Check if coin has relevant marker color for given time of day.
     *
     * A color is said to be relevant for a given time of day if the color matches the predefined
     * color for that time of day (see AppConsts).
     *
     * @param coin Coin for which the marker color should be checked.
     *
     * @return True if coin has currently relevant marker color, false otherwise.
     *
     * @see AppConsts
     */
    private fun hasRelevantColor(coin: Coin): Boolean {
        // Flag that will be returned, set to true if coin has relevant color.
        var hasRelevant = false

        // Get the coin's marker color to test for relevance below.
        val markerColor = coin.markerColor

        // Get the current hour of the day in 24hr format so we can test for color relevance with it.
        val currHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        // Check if the marker color is relevant by seeing if the color coincides with the time of
        // day.
        if ((TimeOfDay.isMorning(currHour)   && markerColor == AppConsts.morningColor)   ||
            (TimeOfDay.isNoon(currHour)      && markerColor == AppConsts.noonColor)      ||
            (TimeOfDay.isAfternoon(currHour) && markerColor == AppConsts.afternoonColor) ||
            (TimeOfDay.isNight(currHour)     && markerColor == AppConsts.nightColor)) {
            hasRelevant = true
        }

        return hasRelevant
    }

    /**
     * Check if coin has relevant marker symbol for current day of month
     *
     * A coin is said to have a relevant marker symbol if the marker symbol matches the last digit
     * of the current day of month. For example:
     *
     * Marker symbol is 1, day of month is 11th => Marker symbol is relevant.
     * Marker symbol is 1, day of month is 17th => Marker symbol is not relevant.
     *
     * @param coin Coin for which the marker symbol should be checked.
     *
     * @return True if coin has currently relevant marker symbol, false otherwise.
     */
    private fun hasRelevantSymbol(coin: Coin): Boolean {
        // Get the current day of month so we can extract its last digit.
        val currDayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        // String representation of last digit of day of month, will be compared to marker symbol.
        val dayOfMonthLastDigit = "${currDayOfMonth % 10}"

        // Return whether marker symbol coincides with last digit of current day of month.
        return coin.markerSymbol == dayOfMonthLastDigit
    }

}
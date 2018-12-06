package com.coinz.app.utils

/**
 * Utility class to define times of the day for the marker color multiplier bonus feature.
 *
 * A time of day is determined by a start time given as an integer, representing the hour of the day
 * in on a 24hr clock and the end time represented in an identical way.
 *
 * @param start Start of the particular time of day.
 * @param end End of the particular time of day.
 */
enum class TimeOfDay(val start: Int, val end: Int) {
    // Note: 24hr day split arbitrarily into four parts of the day which don't overlap given that
    // start is taken inclusively and end exclusively.
    /**
     * Morning, defined to be from 5am to 10am.
     */
    Morning(5,10),
    /**
     * Noon, defined to be from 10am to 2pm.
     */
    Noon(10,14),
    /**
     * Afternoon, define to be from 2pm to 9pm.
     */
    Afternoon(14,21),
    /**
     * Night, defined to be from 9pm to 5am.
     */
    Night(21,5);

    companion object {

        /**
         * Check if given hour of day classifies as morning.
         *
         * @param hourOfDay Given hour of day.
         *
         * @return True if given hour of day classifies as morning by definition in this class.
         */
        fun isMorning(hourOfDay: Int): Boolean {
            return (Morning.start <= hourOfDay &&
                    hourOfDay < Morning.end)
        }

        /**
         * Check if given hour of day classifies as noon.
         *
         * @param hourOfDay Given hour of day.
         *
         * @return True if given hour of day classifies as noon by definition in this class.
         */
        fun isNoon(hourOfDay: Int): Boolean {
            return (Noon.start <= hourOfDay &&
                    hourOfDay < Noon.end)
        }

        /**
         * Check if given hour of day classifies as afternoon.
         *
         * @param hourOfDay Given hour of day.
         *
         * @return True if given hour of day classifies as afternoon by definition in this class.
         */
        fun isAfternoon(hourOfDay: Int): Boolean {
            return (Afternoon.start <= hourOfDay &&
                    hourOfDay < Afternoon.end)
        }

        /**
         * Check if given hour of day classifies as night.
         *
         * @param hourOfDay Given hour of day.
         *
         * @return True if given hour of day classifies as night by definition in this class.
         */
        fun isNight(hourOfDay: Int): Boolean {
            return (Night.start <= hourOfDay &&
                    hourOfDay < Night.end)
        }

    }
}
package com.coinz.app.utils

/**
 * Icon index calculator.
 *
 * Object to compute the index for the drawable icon resource in arrays.xml based on the marker
 * symbol and color.
 *
 * Currently supported:
 *
 * Symbols: 0 to 9
 * Colors: #0000ff, #008000, #ff0000, #ffdf00
 *
 * Usage:
 *      var iconIndex: Int = IconIndex("0", "#ff0000")
 */
object IconIndex {

    // Number of colors currently supported.
    const val numColors = 4

    /**
     * Compute icon index.
     *
     * @param symbol Marker symbol for which to select icon, see supported symbols.
     * @param color Marker color for which to select icon, see supported colors.
     *
     * @return Index of marker drawable corresponding to given input.
     */
    operator fun invoke(symbol: String, color: String): Int {
        // Symbol will be 0 to 9 as string, convert that to its numerical value. Need to account for
        // character encoding offset by subtracting the character code for "0".
        val symbolIndex: Int = symbol.toInt() - "0".toInt()

        val colorOffset = when(color) {
            "#0000ff" -> 0
            "#008000" -> 1
            "#ff0000" -> 2
            "#ffdf00" -> 3
            else -> 0
        }

        // Markers are grouped by symbols and then ordered by colors in arrays.xml, with color
        // ordering being: #0000ff, #008000, #ff0000, #ffdf00. To get correct marker we hence need
        // to take the symbol, account for how many colors we have for each symbol and then offset
        // everything by the color we'd like to have.
        val iconIndex = symbolIndex * numColors + colorOffset

        AppLog("IconIndex", "invoke",
               "markerSymbol=$symbol, markerColor=$color, index=$iconIndex")

        return iconIndex
    }

}
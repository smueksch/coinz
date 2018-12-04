package com.coinz.app.utils

/**
 * Navigation drawer menu item indices.
 *
 * Utility construct to access specific items in the navigation drawer menu. The values of the
 * specific items are based on the order in which they appear in menu/menu_nav_drawer.xml.
 *
 * This enum class really exists to make the code more readable and avoid having to comment on the
 * use of specific numbers as indices each time.
 */
enum class NavDrawerMenu(val index: Int) {
    Map(0),
    LocalWallet(1),
    CentralBank(2)
}
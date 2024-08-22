package com.jeff_media.deskript.color

import com.jeff_media.deskript.color.Color.*

object BashColorCodes : ColorCodes {
    private val ESCAPE = '\u001B'
    override fun get(color: Color): String {
        return ESCAPE + "[" + when(color) {
            BLACK -> "30"
            DARK_BLUE -> "34"
            DARK_GREEN -> "32"
            DARK_AQUA -> "36"
            DARK_RED -> "31"
            DARK_PURPLE -> "35"
            GOLD -> "33"
            GRAY -> "37"
            DARK_GRAY -> "90"
            BLUE -> "94"
            GREEN -> "92"
            AQUA -> "96"
            RED -> "91"
            LIGHT_PURPLE -> "95"
            YELLOW -> "93"
            WHITE -> "97"

            RESET -> "0"
            BOLD -> "1"
            ITALIC -> "3"
            UNDERLINE -> "4"
            MAGIC -> "2"
        } + "m"
    }
}

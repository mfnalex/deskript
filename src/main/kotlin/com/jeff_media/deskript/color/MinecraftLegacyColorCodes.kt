package com.jeff_media.deskript.color

object MinecraftLegacyColorCodes : ColorCodes {
    override fun get(color: Color): String {
        return when (color) {
            Color.BLACK -> "§0"
            Color.DARK_BLUE -> "§1"
            Color.DARK_GREEN -> "§2"
            Color.DARK_AQUA -> "§3"
            Color.DARK_RED -> "§4"
            Color.DARK_PURPLE -> "§5"
            Color.GOLD -> "§6"
            Color.GRAY -> "§7"
            Color.DARK_GRAY -> "§8"
            Color.BLUE -> "§9"
            Color.GREEN -> "§a"
            Color.AQUA -> "§b"
            Color.RED -> "§c"
            Color.LIGHT_PURPLE -> "§d"
            Color.YELLOW -> "§e"
            Color.WHITE -> "§f"

            Color.BOLD -> "§l"
            Color.UNDERLINE -> "§n"
            Color.ITALIC -> "§o"
            Color.RESET -> "§r"
            Color.MAGIC -> "§k"
        }
    }
}
package com.jeff_media.deskript.color

object LevelColors {

    private val levels = mapOf(
        0 to Color.YELLOW,
        1 to Color.GREEN,
        2 to Color.AQUA,
        3 to Color.LIGHT_PURPLE,
        4 to Color.BLUE,
    )

    private val unknown = Color.MAGIC

    fun get(level: Int): Color {

        val remainder = level % levels.size
        return levels[remainder] ?: unknown

    }

}
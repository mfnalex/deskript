package com.jeff_media.deskript.color

fun interface ColorCodes {

    fun get(color: Color): String

    val csyntax get() = get(Color.DARK_GRAY)
    val ckey get() = get(Color.GRAY)
    val cvalue get() = get(Color.GOLD)
    val cr get() = get(Color.RESET)
    val vnull get() = get(Color.RED) + get(Color.ITALIC) + "null"
    val vempty get() = get(Color.RED) + get(Color.ITALIC) + "empty"

    val cstring get() = get(Color.GOLD)
    val cchar get() = get(Color.GOLD)
    val cint get() = get(Color.BLUE)
    val cfloat get() = get(Color.AQUA)
    val cdouble get() = get(Color.DARK_AQUA)
    val cboolean get() = get(Color.LIGHT_PURPLE)
    val cbyte get() = get(Color.LIGHT_PURPLE)
    val cenum get() = get(Color.GREEN)
    val clong get() = get(Color.DARK_BLUE)
    val cshort get() = get(Color.BLUE)

}
package com.jeff_media.deskript

import com.jeff_media.deskript.color.BashColorCodes
import kotlin.test.Test

class Test {

    val deskript = Deskript(BashColorCodes, true)

    data class Person(
        val name: String = "Jeff",
        val age: Int = 29,
    )

    @Test
    fun test() {
        println(deskript.parse(Person()).describe())
    }
}
package com.jeff_media.deskript

fun interface ClassDescriptor<T> {

    fun describe(obj: T): Node

}
package com.jeff_media.deskript

import com.jeff_media.deskript.color.BashColorCodes
import com.jeff_media.deskript.color.Color
import com.jeff_media.deskript.color.ColorCodes
import com.jeff_media.deskript.color.LevelColors
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

data class Deskript(val colors: ColorCodes, val indent: Boolean) {

    private fun fromObject(obj: Any?): Node {

        if (obj == null) return Node.Null(this)

        if (obj is Node) return obj

        val name = getPartlyQualifiedName(obj)
        val clazz = obj::class

        if (Node.Primitive.isPrimitive(obj)) return Node.Primitive(this, obj)

        return when (obj) {
            is Map<*, *> -> Node.Complex(
                this,
                name,
                obj.mapValues { fromObject(it.value) }.mapKeys { it.key.toString() })

            is Collection<*> -> Node.List(this, name, obj.map { fromObject(it) })
            is Array<*> -> Node.List(this, name, obj.map { fromObject(it) })

            //else -> Node.Primitive(this, obj)
            else -> Node.Complex(this, name, clazz.memberProperties.associate {
                val propName = it.name
                val propValue = try {
                    @Suppress("UNCHECKED_CAST")
                    val value = (it as KProperty1<Any, *>).get(obj)  // Explicit cast to handle the type issue
                    println("Got value for $propName: $value [${value?.javaClass?.simpleName}]")
                    fromObject(value)
                } catch (e: Exception) {
                    Node.VoidNode(this)
                }
                propName to propValue
            }.filter { (_, node) -> node !is Node.VoidNode })
        }
    }

    fun parse(obj: Any): Node = fromObject(obj)
    fun describe(obj: Any): String = parse(obj).describe()
}

fun getFromGetter(deskript: Deskript, obj: Any, getter: KProperty1.Getter<*,*>): Any? {
    try {
        return getter.call(obj)
    } catch (e: Exception) { }

    try {
        return getter.call(obj)
    } catch (e: Exception) { }

    return Node.VoidNode(deskript)
}

sealed class Node(protected val deskript: Deskript, private val _name: String?) {
    class Null(deskript: Deskript) : Primitive(deskript, null)
    open class Primitive(deskript: Deskript, val value: Any?) : Node(deskript, null) {
        companion object {
            fun isPrimitive(value: Any?): Boolean {
                return value is String || value is Number || value is Boolean || value is Char || value is Enum<*>
            }
        }

        override fun toString(): String {
            when (value) {
                is String -> return "${deskript.colors.csyntax}\"${deskript.colors.cstring}$value${deskript.colors.csyntax}\""
                is Char -> return "${deskript.colors.csyntax}'${deskript.colors.cchar}$value${deskript.colors.csyntax}'"
                is Enum<*> -> return deskript.colors.cenum + value.name
                is Int -> return "${deskript.colors.cint}${value}i"
                is Long -> return "${deskript.colors.clong}${value}L"
                is Float -> return "${deskript.colors.cfloat}${value}f"
                is Double -> return "${deskript.colors.cdouble}${value}d"
                is Boolean -> return deskript.colors.cboolean + value.toString()
                is Short -> return "${deskript.colors.cshort}${value}s"
                is Byte -> return "${deskript.colors.cbyte}${value}b"
                else -> return value?.toString() ?: "null"
            }
        }
    }

    class Complex(deskript: Deskript, _name: String, val children: Map<String, Node>) : Node(deskript, _name) {
        fun size() = children.size
    }

    class List(deskript: Deskript, _name: String, val children: Collection<Node>) : Node(deskript, _name) {
        fun size() = children.size
    }

    class VoidNode(deskript: Deskript) : Primitive(deskript, null)

    val name: String
        get() = _name ?: "?"

    fun describe(): String {
        return nodeToString(
            node = this,
            colors = deskript.colors,
            levelColors = LevelColors,
            indent = deskript.indent,
            indentLevel = 0
        )
    }

}


private fun nodeToString(
    node: Node,
    colors: ColorCodes,
    levelColors: LevelColors,
    indent: Boolean,
    indentLevel: Int = 0
): String {
    val indentStr = if (indent) "  ".repeat(indentLevel) else ""
    val newlineStr = if (indent) "\n" else ""
    val cname = colors.get(levelColors.get(indentLevel))

    fun Any?.str(): Any = when (this) {
        null -> colors.vnull
        is String -> this.ifEmpty { colors.vempty }
        is Collection<*> -> this.ifEmpty { colors.vempty }
        is Map<*, *> -> this.ifEmpty { colors.vempty }
        else -> this
    }

    return when (node) {
        is Node.Primitive -> "${colors.cr}${colors.cvalue}${node}${colors.cr}"
        is Node.Complex -> {
            val childrenString =
                if (node.size() > 0)
                    node.children.entries.joinToString("${colors.cr}${colors.csyntax}, ${colors.cr}$newlineStr${colors.cr}") {
                        "${colors.cr}$indentStr${colors.ckey}${it.key}${colors.csyntax}=${colors.cr}${
                            nodeToString(
                                it.value,
                                colors,
                                levelColors,
                                indent,
                                indentLevel + 1
                            )
                        }"
                    }
                else
                    colors.vempty
            val innerString =
                if (childrenString == colors.vempty) colors.vempty else "${colors.cr}$newlineStr${colors.cr}$childrenString${colors.cr}$newlineStr${colors.cr}$indentStr${colors.csyntax}"
            "${colors.cr}${cname}${node.name}${colors.cr}${colors.csyntax}{${colors.cr}$innerString${colors.cr}${colors.csyntax}}${colors.cr}"
        }

        is Node.List -> {
            val childrenString =
                if (node.size() > 0)
                    node.children.joinToString("${colors.cr}${colors.csyntax}, ${colors.cr}") {
                        nodeToString(
                            it,
                            colors,
                            levelColors,
                            indent,
                            indentLevel + 1
                        )
                    }
                else
                    colors.vempty
            val innerString =
                if (childrenString == colors.vempty) colors.vempty else "${colors.cr}$childrenString${colors.cr}"
            "${colors.cr}${colors.csyntax}{${colors.cr}$innerString${colors.cr}${colors.csyntax}}${colors.cr}"
        }
    }
}

fun getPartlyQualifiedName(obj: Any): String {
    return getPartlyQualifiedName(obj::class)
}

fun getPartlyQualifiedName(klass: KClass<*>): String {
    return klass.qualifiedName?.substringAfterLast('.') ?: klass.simpleName ?: ""
}

fun main(args: Array<String>) {
    val deskript = Deskript(BashColorCodes, false)
    val node = deskript.parse(mapOf("name" to "Jeff", "age" to 29, "friends" to mapOf("mfnalex" to "very good friends", "mfnaley" to "close friends", "mfnalez" to mapOf("int" to 30, "float" to 27.5f, "double" to 99.00003, "boolean" to true, "char" to 'c', "short" to 1.toShort(), "byte" to 2.toByte(), "enum" to Color.DARK_BLUE, "long" to 1000000000000000L, "string" to "string", "list" to listOf("a", "b", "c")))))
    println(node.describe())
    println("")
    val deskript2 = Deskript(BashColorCodes, true)
    val node2 = deskript2.parse(mapOf("name" to "Jeff", "age" to 29, "friends" to mapOf("mfnalex" to "very good friends", "mfnaley" to "close friends", "mfnalez" to mapOf("int" to 30, "float" to 27.5f, "double" to 99.00003, "boolean" to true, "char" to 'c', "short" to 1.toShort(), "byte" to 2.toByte(), "enum" to Color.DARK_BLUE, "long" to 1000000000000000L, "string" to "string", "list" to listOf("a", "b", "c")))))
    println(node2.describe())


}
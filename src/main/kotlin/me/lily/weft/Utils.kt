package me.lily.weft

import org.objectweb.asm.Opcodes
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.createType

internal object Utils {

    val intType: KType      by lazy { Int::class.createType() }
    val booleanType: KType  by lazy { Boolean::class.createType() }
    val longType: KType     by lazy { Long::class.createType() }
    val doubleType: KType   by lazy { Double::class.createType() }
    val floatType: KType    by lazy { Float::class.createType() }
    val voidType: KType     by lazy { Unit::class.createType() }

    fun getReturnOpcode(desc: String): Int {
        desc.split(")")[1].run {
            if(this.length == 1) {
                return when (this) {
                    "I" -> Opcodes.IRETURN
                    "Z" -> Opcodes.IRETURN
                    "L" -> Opcodes.LRETURN
                    "D" -> Opcodes.DRETURN
                    "F" -> Opcodes.FRETURN
                    "V" -> Opcodes.RETURN
                    else -> throw IllegalStateException("Unknown primitive type, what the fuck???")
                }
            } else {
                return Opcodes.ARETURN
            }
        }
    }

    fun getDescriptor(type: KType): String {
        return when (type) {
            intType     -> "I"
            booleanType -> "Z"
            longType    -> "L"
            doubleType  -> "D"
            floatType   -> "F"
            voidType    -> "V"
            else        -> { "L${(((type.classifier ?: throw NullPointerException("What the fuck??")) as KClass<*>).qualifiedName
                ?: throw NullPointerException("What the fuck???")).replace(".", "/")};" }
        }
    }

    fun getDescriptor(func: KFunction<*>): String {
        val sb = StringBuilder()
        sb.append("(")
        func.parameters.forEach { sb.append(getDescriptor(it.type)) }
        sb.append(")").append(getDescriptor(func.returnType))
        return sb.toString()
    }

}
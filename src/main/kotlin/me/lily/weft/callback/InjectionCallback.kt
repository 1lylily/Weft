package me.lily.weft.callback

public class InjectionCallback(public val instance: Any?) {

    public var returnVal: Any? = PlaceholderObject

    public fun <T : Any> returnValue(value: T? = null) {
        returnVal = value
    }

}

internal object PlaceholderObject
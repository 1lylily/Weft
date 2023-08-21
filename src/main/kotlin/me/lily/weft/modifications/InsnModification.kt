package me.lily.weft.modifications

import org.objectweb.asm.tree.InsnList

internal interface InsnModification {

    fun getTargetIdentifier(): String

    fun inject(list: InsnList, static: Boolean, paramProvider: ParamProvider, identifier: String)

}

internal typealias ParamProvider = () -> Unit
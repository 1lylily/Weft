package me.lily.weft.modifications

public abstract class AbstractInsnModification(private val target: String) : InsnModification {
    override fun getTargetIdentifier(): String = target
}
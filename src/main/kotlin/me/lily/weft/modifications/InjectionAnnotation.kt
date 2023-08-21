package me.lily.weft.modifications

import me.lily.weft.modifications.impl.HeadInsnModification

/**
 * Declares a given function as an injection.
 *
 * @property type The [Type] of the injection.
 * @property target The target of the injection. This can either be just a function name, or a function name with descriptor.
 * @property shift The [Shift] position of the injection.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
public annotation class Injection(
    val type: Type,
    val target: String,
    val shift: Shift = Shift.AFTER
)

internal typealias ModificationBuilder = (args: Array<Any>) -> AbstractInsnModification

/**
 * The type of the given injection.
 *
 * @property HEAD Inject at the beginning of the target function.
 * @property TAIL Inject at the end of the target function.
 */
public enum class Type(public val builder: ModificationBuilder) {
    HEAD({ args -> HeadInsnModification(args[0] as String) }),
    //TAIL({ TailInsnModification() })
}

/**
 * Whether to inject [before][BEFORE] or [after][AFTER] the given target.
 *
 * This currently doesn't apply to any injection types.
 */
public enum class Shift {
    BEFORE,
    AFTER
}
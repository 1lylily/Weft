package me.lily.weft.threads

import me.lily.weft.Utils
import me.lily.weft.modifications.Injection
import me.lily.weft.modifications.InsnModification
import me.lily.weft.modifications.Type
import java.lang.RuntimeException
import java.util.function.Supplier
import kotlin.reflect.full.createType
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

/**
 * The base class for a Thread.
 *
 * A thread is the equivalent of a WeaveLoader Hook, however far more abstracted.
 *
 * @param target The target for injection. A [ThreadTarget] annotation can be used instead of this parameter. If both are present, parameter is prioritised.
 */
public open class Thread(target: String? = null) {

    /**
     * The target of this thread's injection.
     */
    public val target: String

    /**
     * A list of this Thread's injections.
     */
    internal val modifications: MutableMap<String, MutableList<InsnModification>> = HashMap()

    /**
     * Finds the target and modifications.
     */
    init { //todo readability idk
        this.target = target
            ?: (this::class.findAnnotation<ThreadTarget>()?.target
            ?: throw NullPointerException("No targets found for Thread."))

        this::class.declaredFunctions.filter {
            it.returnType == Unit::class.createType() &&
                    it.hasAnnotation<Injection>()
        }.forEach {
            val annotation: Injection = it.findAnnotation<Injection>()
                ?: throw RuntimeException("Annotation both exists and doesn't exist.")
            val identifier = "${(this::class.qualifiedName 
                ?: throw IllegalArgumentException("Threads cannot be anonymous classes."))
                .replace(".", "/")}|${it.name}|${Utils.getDescriptor(it)}"
            (modifications[identifier] ?: Supplier<MutableList<InsnModification>> {
                val list: MutableList<InsnModification> = ArrayList()
                modifications[identifier] = list
                return@Supplier list
            }.get()).add(Supplier<InsnModification> {
                    when (annotation.type) {
                        Type.HEAD -> {
                            Type.HEAD.builder(arrayOf("${this.target}|${if (annotation.target.contains("(")) { annotation.target
                                .replace("(", "$(").split("$")[0]} else annotation.target}|${if (annotation.target
                                    .contains("(")) { annotation.target.replace("(", "$(").split("$")[1]} 
                            else "NULL"}"))
                        }
                        else -> throw IllegalArgumentException("Unhandled injection type.")
                    }
            }.get())
        }
    }

}

/**
 * Declares the target for a thread.
 *
 * @property target The target for injection.
 *
 * @see Thread
 */
public annotation class ThreadTarget(public val target: String)
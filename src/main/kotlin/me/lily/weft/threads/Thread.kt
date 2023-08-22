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
    init {
        this.target = target
            ?: (this::class.findAnnotation<ThreadTarget>()?.target
            ?: throw NullPointerException("No targets found for Thread."))

        this::class.declaredFunctions.filter {
            it.returnType == Unit::class.createType() &&
                    it.hasAnnotation<Injection>()
        }.forEach {
            val annotation: Injection = it.findAnnotation<Injection>()
                ?: throw RuntimeException("Annotation both exists and doesn't exist.")
            val identifier = Utils.getIdentifier(it)
            (modifications[identifier] ?: run {
                val list: MutableList<InsnModification> = ArrayList()
                modifications[identifier] = list
                list
            }).add(run {
                    when (annotation.type) {
                        Type.HEAD -> {
                            Type.HEAD.builder(arrayOf(Utils.getIdentifier(this.target, annotation)))
                        }
                    }
            })
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
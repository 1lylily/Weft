package me.lily.weft

import me.lily.weft.loom.LoomManager
import me.lily.weft.modifications.InsnModification
import me.lily.weft.threads.Thread
import org.objectweb.asm.tree.ClassNode
import java.util.function.Supplier

/**
 * The main class of Weft.
 *
 * To use Weft, create an instance of this class and call [transform] from a [WeaveLoader Hook](https://docs.weavemc.net/-weave%20-loader/net.weavemc.loader.api/-hook/index.html) with no targets.
 * When creating an instance, pass instances of your threads as parameters.
 *
 * Note: The developer of this library is an idiot, and wanted to keep the theme of the library name, threads, unless stated otherwise, refer to [Thread], not Java threads.
 *
 * @param threads The threads to inject.
 * @param loom The loom for this Weft instance.
 */
public class Weft(vararg threads: Thread, loom: String = "DefaultLoom") {

    /**
     * Map of classes to list of threads modifying that class.
     *
     * This map is populated on Weft instantiation.
     */
    private val threadMap: MutableMap<String, MutableList<Thread>> = HashMap()

    /**
     * Populates the thread map on instantiation.
     */
    init {
        threads.forEach {
            (threadMap[it.target.replace(".", "/")] ?: Supplier<MutableList<Thread>> {
                val list: MutableList<Thread> = ArrayList()
                threadMap[it.target.replace(".", "/")] = list
                list
            }.get()).add(it)
        }

        LoomManager.registerLoom(loom, this, threadMap)
    }

    /**
     * Should be called whenever a class is transformed.
     *
     * @param classNode The ClassNode of the current transformation.
     */
    public fun transform(classNode: ClassNode) {
        threadMap[classNode.name]?.forEach {
            it.transform(classNode)
        }
    }

}

private fun Thread.transform(classNode: ClassNode) {
    val methodMap: MutableMap<String, MutableList<InsnModification>> = HashMap()

    this.modifications //todo finish this, my brain stopped working before i could do it
}
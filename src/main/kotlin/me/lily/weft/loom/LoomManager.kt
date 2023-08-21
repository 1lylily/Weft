package me.lily.weft.loom

import me.lily.weft.Weft
import me.lily.weft.threads.Thread

internal object LoomManager {
    private val loomMap: MutableMap<String, Loom> = HashMap()

    @JvmStatic
    fun getThread(identifier: String): Thread {
        loomMap.values.find { loom ->
            return loom.threads.poolAndAssociate()[identifier] ?: return@find false
        }

        throw IllegalStateException("Requesting non-existent Thread of InsnModification.")
        // ^ if you get this error, cry.
    }

    fun registerLoom(name: String, weft: Weft, threads: MutableMap<String, MutableList<Thread>>) {
        if(!loomMap.containsKey(name)) {
            loomMap[name] = Loom(weft, threads)
        }
    }
}

// todo optimize, maybe cache idk
private fun MutableMap<String, MutableList<Thread>>.poolAndAssociate(): MutableMap<String, Thread> {
    val map: MutableMap<String, Thread> = HashMap()

    this.values.forEach { list ->
        list.forEach {
            map[it::class.qualifiedName?.replace(".", "/")
                ?: throw NullPointerException("Impossible Error. (null qualifiedName)")] = it
        }
    }

    return map
}

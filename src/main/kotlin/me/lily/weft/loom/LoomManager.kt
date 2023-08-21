package me.lily.weft.loom

import me.lily.weft.threads.Thread

internal object LoomManager {

    private val loomMap: Map<String, Loom> = HashMap()

    @JvmStatic
    fun getThread(identifier: String): Thread {
        loomMap.values.find { loom ->
            return loom.threads[identifier] ?: return@find false
        }

        throw IllegalStateException("Requesting non-existent Thread of InsnModification.")
        // ^ if you get this error, cry.
    }


}
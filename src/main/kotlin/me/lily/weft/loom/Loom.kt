package me.lily.weft.loom

import me.lily.weft.Weft
import me.lily.weft.threads.Thread

internal data class Loom(val weft: Weft, val threads: Map<String, Thread>)

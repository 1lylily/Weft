package me.lily.weft.modifications.impl

import org.objectweb.asm.Opcodes.*
import me.lily.weft.modifications.AbstractInsnModification
import me.lily.weft.modifications.ParamProvider
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.InsnNode
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.TypeInsnNode
import org.objectweb.asm.tree.VarInsnNode
import kotlin.reflect.KFunction

internal class HeadInsnModification(target: String) : AbstractInsnModification(target) {

    //todo param checking
    //todo params to stack
    //todo dont use newList idfk
    override fun inject(list: InsnList, static: Boolean, paramProvider: ParamProvider, identifier: String) {
        // identifiers for threads are `qualifiedName`
        // identifier param here is `qualifiedName|funcName|desc` as it's an InsnModification identifier

        val insns = InsnList()

        insns.add(TypeInsnNode(NEW, "me/lily/weft/InjectionCallback"))
        insns.add(InsnNode(DUP))

        if(!static) {
            insns.add(VarInsnNode(ALOAD, 0))
        } else {
            insns.add(InsnNode(ACONST_NULL))
        }

        insns.add(MethodInsnNode(INVOKESPECIAL, "me/lily/weft/InjectionCallback", "<init>", "(Ljava/lang/Object;)V"))

        insns.add(InsnNode(DUP))

        insns.add(LdcInsnNode(identifier.split("|")[0]))

        insns.add(MethodInsnNode(INVOKESTATIC, "me/lily/weft/loom/LoomManager", "getThread", "(Ljava/lang/String;)Lme/lily/weft/threads/Thread;"))

        identifier.split("|").run {
            insns.add(MethodInsnNode(INVOKEDYNAMIC, this[0], this[1], this[2]))
        }

        insns.add(MethodInsnNode(INVOKEDYNAMIC, "me/lily/weft/InjectionCallback", "isCancelled", "()Z"))

        val label = LabelNode()

        insns.add(JumpInsnNode(IFEQ, label))

        insns.add(InsnNode(RETURN))

        insns.add(label)

        list.insert(insns)
    }

}
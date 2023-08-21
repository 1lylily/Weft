package example

import me.lily.weft.callback.InjectionCallback
import me.lily.weft.modifications.Injection
import me.lily.weft.modifications.Type
import me.lily.weft.threads.Thread
import me.lily.weft.threads.ThreadTarget

@ThreadTarget("example/TargetClass")
public class ExampleThread : Thread() {

    @Injection(
        type = Type.HEAD,
        target = "targetMethod", // targetMethod(Z)I
    )
    public fun exampleInjection(ic: InjectionCallback) {
        println("Injected at HEAD!")

        println("Injected into ${ic.instance!!::class.simpleName}.")

        ic.returnValue(4)
    }

}
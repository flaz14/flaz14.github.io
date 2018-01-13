package io.github.flaz14;

import java.lang.instrument.Instrumentation;

/**
 *
 */
public class SimpleAgent {
    public static void premain(String agentArgs, Instrumentation inst)
    {
        System.out.println(">>> premain()");
        //initialize(agentArgs, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst)
    {
        System.out.println(">>> agentmain()");
//        initialize(agentArgs, inst);
    }

}

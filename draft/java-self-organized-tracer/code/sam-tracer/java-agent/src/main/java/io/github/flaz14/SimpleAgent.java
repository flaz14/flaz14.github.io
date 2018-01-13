package io.github.flaz14;

import java.lang.instrument.Instrumentation;

/**
 *
 */
public class SimpleAgent {
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println(">>> premain()");
        initialize(instrumentation);
    }

    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        System.out.println(">>> agentmain()");
        initialize(instrumentation);
    }

    private static void initialize(final Instrumentation instrumentation) {
        instrumentation.addTransformer(new ClassTransformer());
    }
}

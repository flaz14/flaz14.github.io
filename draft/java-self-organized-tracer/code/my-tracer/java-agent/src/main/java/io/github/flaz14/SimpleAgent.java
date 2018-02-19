package io.github.flaz14;

import java.lang.instrument.Instrumentation;

public class SimpleAgent {
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        initialize(instrumentation);
    }

    public static void agentmain(String agentArgs, Instrumentation instrumentation) {
        initialize(instrumentation);
    }

    private static void initialize(final Instrumentation instrumentation) {
        System.out.println("Java Agent is loaded.");
        instrumentation.addTransformer(new ClassTransformer());
    }
}

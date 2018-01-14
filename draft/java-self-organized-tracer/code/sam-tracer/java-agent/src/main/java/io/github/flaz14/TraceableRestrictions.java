package io.github.flaz14;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

public class TraceableRestrictions {
    public static boolean shouldBeInstrumented(final Set<String> interfaceNames) {
        return interfaceNames.contains(INTERFACE_NAME);
    }

    public static boolean shouldBeInstrumented(final String methodSignature) {
        return METHOD_SIGNATURES.contains(methodSignature);
    }

    private static final String INTERFACE_NAME = "io/github/flaz14/publicapi/Traceable";

    private static final Set<String> METHOD_SIGNATURES = unmodifiableSet(
            new HashSet<>(
                    asList(
                            "doBusinessLogic(Ljava/lang/String;)V",
                            "calculateBusinessValue()Ljava/lang/Object;")
            )
    );
}

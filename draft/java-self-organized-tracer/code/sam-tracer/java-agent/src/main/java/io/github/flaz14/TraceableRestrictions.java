package io.github.flaz14;

import java.util.Collections;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

public class TraceableRestrictions {
    public static boolean shouldBeInstrumented(final Set<String> interfaceNames) {
        return interfaceNames.contains(INTERFACE_NAME);
    }

    public static boolean shouldBeInstrumented(final String methodSignature) {
        return methodSignaturePatterns().
                stream().
                map(pattern -> pattern.matcher(methodSignature)).
                anyMatch(Matcher::matches);
    }

    /**
     * <code>Traceable</code> is generic interface. So it doesn't matter what are the
     * exact types of the parameters and return values. But it's necessary to obey
     * the number of parameters (and they obviously should be <code>Object</code>
     * types, e.g. <em>link</em> in the terms of JVM).
     */
    private static Set<Pattern> methodSignaturePatterns() {
        return Stream.of(
                "doBusinessLogic\\(L.*;\\)V",
                "calculateBusinessValue\\(\\)L.*;").
                map(Pattern::compile).
                collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    private static final String INTERFACE_NAME = "io/github/flaz14/publicapi/Traceable";
}

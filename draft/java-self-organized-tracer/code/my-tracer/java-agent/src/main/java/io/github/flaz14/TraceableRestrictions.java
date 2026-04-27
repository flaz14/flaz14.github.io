package io.github.flaz14;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

public class TraceableRestrictions {
    public static boolean shouldBeInstrumented(final byte[] classfileBuffer) {
        Set<String> interfaces = interfaces(classfileBuffer);
        System.out.println(">>> interfaces: [" + interfaces + "]");
        return interfaces.
                contains(INTERFACE_NAME);
    }

    public static boolean shouldBeInstrumented(final String methodSignature) {
        return methodSignaturePatterns().
                stream().
                map(pattern -> pattern.matcher(methodSignature)).
                anyMatch(Matcher::matches);
    }

    private static Set<String> interfaces(final byte[] classfileBuffer) {
        final Set<String> interfaceNames = new HashSet<>();
        final ClassReader reader = new ClassReader(classfileBuffer);
        final ClassVisitor visitor = new EmptyVisitor() {
            @Override
            public void visit(final int version,
                              final int access,
                              final String name,
                              final String signature,
                              final String superName,
                              final String[] interfaces) {
                //interfaceNames.clear();
                interfaceNames.addAll(asList(interfaces));
                super.visit(version, access, name, signature, superName, interfaces);
            }
        };
        reader.accept(visitor, ClassTransformer.DO_NOT_SKIP_ANYTHING);
        return unmodifiableSet(interfaceNames);
    }

    /**
     * <code>Traceable</code> is generic interface. So it doesn't matter what are the
     * exact types of the parameters and return values. But it's necessary to obey
     * the number of parameters (and they obviously should be <code>Object</code>
     * types, e.g. <em>link</em> in the terms of JVM).
     */
    private static Set<Pattern> methodSignaturePatterns() {
        return Stream.of(
                "processSomething\\(L.*;\\)V",
                "getSomething\\(\\)L.*;").
                map(Pattern::compile).
                collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
    }

    private static final String INTERFACE_NAME = "io/github/flaz14/publicapi/Traceable";
}

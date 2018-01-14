package io.github.flaz14;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

public class TraceableVisitor extends EmptyVisitor {

    private String currentMethodSignature;

    @Override
    public MethodVisitor visitMethod(final int access,
                                     final String name,
                                     final String desc,
                                     final String signature,
                                     final String[] exceptions) {
        final String incomingMethodSignature = name + desc;
        if (shouldBeVisited(incomingMethodSignature)) {
            currentMethodSignature = incomingMethodSignature;
            System.out.printf("TRACE: sam-tracer has entered into method [%s]%n", currentMethodSignature);
        }
        return this;
    }

    @Override
    public void visitEnd() {
        if (currentMethodSignature != null) {
            System.out.printf("TRACE: sam-tracer has leaved method [%s]%n", currentMethodSignature);
            currentMethodSignature = null;
        }
    }

    private static boolean shouldBeVisited(final String methodSignature) {
        return TRACEABLE_METHOD_SIGNATURES.contains(methodSignature);
    }

    private static final Set<String> TRACEABLE_METHOD_SIGNATURES = unmodifiableSet(
            new HashSet<>(
                    asList(
                            "doBusinessLogic(Ljava/lang/String;)V",
                            "calculateBusinessValue()Ljava/lang/Object;"
                    )
            )
    );
}

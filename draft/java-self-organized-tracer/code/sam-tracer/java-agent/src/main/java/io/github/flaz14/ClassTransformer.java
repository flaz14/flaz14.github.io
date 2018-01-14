package io.github.flaz14;

import io.github.flaz14.util.ClassExplorer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Set;

public class ClassTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(
            final ClassLoader loader,
            final String className,
            final Class<?> classBeingRedefinedIsNullEveryTime,
            final ProtectionDomain protectionDomain,
            final byte[] classfileBuffer)
            throws IllegalClassFormatException {
        System.out.println(">>> transform()!");
        System.out.println(">>> className!");
        System.out.println(">>> className: " + className);
        System.out.println(">>> should be instrumented: " + shouldBeInstrumented(className, classfileBuffer));
        return classfileBuffer;
    }

    private static boolean shouldBeInstrumented(final String className, final byte[] classfileBuffer) {
        final Set<String> interfaceNames = ClassExplorer.interfaces(className, classfileBuffer);
        System.out.printf("Class [%s] implements [%s] interfaces%n", className, interfaceNames);
        return interfaceNames.contains("io/github/flaz14/publicapi/Traceable");
    }
}

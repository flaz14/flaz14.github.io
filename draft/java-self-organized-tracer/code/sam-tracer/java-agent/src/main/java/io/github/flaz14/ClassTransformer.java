package io.github.flaz14;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 *
 */
public class ClassTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(
            final ClassLoader loader,
            final String className,
            final Class<?> classBeingRedefined,
            final ProtectionDomain protectionDomain,
            final byte[] classfileBuffer)
            throws IllegalClassFormatException {
        System.out.println(">>> transform()");

        System.out.println(">>> className: " + classBeingRedefined.getName());

        return classfileBuffer;
    }
}

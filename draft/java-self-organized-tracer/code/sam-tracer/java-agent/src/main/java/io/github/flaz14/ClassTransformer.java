package io.github.flaz14;

import io.github.flaz14.util.ClassExplorer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

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
        System.out.println(">>> interfaces: " + ClassExplorer.interfaces(className, classfileBuffer));

        return classfileBuffer;
    }


}

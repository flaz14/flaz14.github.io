package io.github.flaz14;

import io.github.flaz14.util.ClassAnalysis;
import io.github.flaz14.util.ClassExplorer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

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

        if (shouldBeInstrumented(className, classfileBuffer))
            return getInstrumentedClassBytes(className, classfileBuffer);
        else
            return classfileBuffer;
    }

    private byte[] getInstrumentedClassBytes(String className,
                                             byte[] classfileBuffer) {
        try {
            ClassReader cr = new ClassReader(classfileBuffer);
            ClassAnalysis analysis = new ClassAnalysis();
            cr.accept(analysis, 0);
            ClassWriter cw = new ClassWriter(cr, 0);
            cr.accept(cw, 0);
            return cw.toByteArray();
        } catch (Throwable th) {
            System.err.println("Caught Throwable when trying to instrument: "
                    + className);
            th.printStackTrace();
            return null;
        }
    }

    private static boolean shouldBeInstrumented(final String className, final byte[] classfileBuffer) {
        final Set<String> interfaceNames = ClassExplorer.interfaces(className, classfileBuffer);
        System.out.printf("Class [%s] implements [%s] interfaces%n", className, interfaceNames);
        return interfaceNames.contains("io/github/flaz14/publicapi/Traceable");
    }
}

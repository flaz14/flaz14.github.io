package io.github.flaz14;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.EmptyVisitor;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

import static io.github.flaz14.TraceableRestrictions.shouldBeInstrumented;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

public class ClassTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(
            final ClassLoader loader,
            final String className,
            final Class<?> classBeingRedefinedIsNullEveryTime,
            final ProtectionDomain protectionDomain,
            final byte[] classfileBuffer)
            throws IllegalClassFormatException {
//        return shouldBeInstrumented(
//                interfaces(classfileBuffer))
//                ? instrumentedClassBytes(classfileBuffer)
//                : classfileBuffer;
        return instrumentedClassBytes(classfileBuffer);
    }

    private byte[] instrumentedClassBytes(final byte[] classfileBuffer) {
        final ClassReader reader = new ClassReader(classfileBuffer);
        final ClassWriter writer = new TraceableClassVisitor(reader, ClassWriter.COMPUTE_FRAMES);
        reader.accept(writer, ClassWriter.COMPUTE_FRAMES);
        return writer.toByteArray();
    }

    /**
     * According to {@link org.objectweb.asm.ClassReader} setting a certain flag
     * means "to skip something". So zero (all flags are unset) has opposite meaning.
     */
    public static final int DO_NOT_SKIP_ANYTHING = 0;
}

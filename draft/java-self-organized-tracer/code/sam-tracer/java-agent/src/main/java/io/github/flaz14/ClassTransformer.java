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
            TraceableVisitor analysis = new TraceableVisitor();
            cr.accept(analysis, DO_NOT_SKIP_ANYTHING);
            ClassWriter cw = new ClassWriter(cr, DO_NOT_SKIP_ANYTHING);
            cr.accept(cw, DO_NOT_SKIP_ANYTHING);
            return cw.toByteArray();
        } catch (Throwable th) {
            System.err.println("Caught Throwable when trying to instrument: "
                    + className);
            th.printStackTrace();
            return null;
        }
    }

    private static boolean shouldBeInstrumented(final String className, final byte[] classfileBuffer) {
        final Set<String> interfaceNames = interfaces(className, classfileBuffer);
        System.out.printf("Class [%s] implements [%s] interfaces%n", className, interfaceNames);
        return interfaceNames.contains("io/github/flaz14/publicapi/Traceable");
    }

    /**
     * Almost copied and pasted from intrace project TODO add link to github
     */
    private static Set<String> interfaces(final String classNameInVm, final byte[] originalClassfile) {
        final Set<String> interfaceNames = new HashSet<>();
        final ClassReader reader = new ClassReader(originalClassfile);
        final ClassVisitor visitor = new EmptyVisitor() {
            @Override
            public void visit(int version, int access, String name,
                              String signature, String superName, String[] interfaces) {
                interfaceNames.clear();
                interfaceNames.addAll(asList(interfaces));
                super.visit(version, access, name, signature, superName, interfaces);
            }
        };
        reader.accept(visitor, DO_NOT_SKIP_ANYTHING);
        return unmodifiableSet(interfaceNames);
    }

    /**
     * The purpose of using zero as flags (in the source of <strong>getInterfaces</strong>
     * original InTrace project) is mysterious.
     * TODO put link to GitHub
     * But according to the Javadoc of {@link org.objectweb.asm.ClassReader} zero clears
     * all "skip" flag. This is why such long name is used here.
     */
    private static final int DO_NOT_SKIP_ANYTHING = 0;
}

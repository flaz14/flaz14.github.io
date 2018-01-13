package io.github.flaz14;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.EmptyVisitor;

/**
 *
 */
public class ClassTransformer implements ClassFileTransformer {
    // classBeingRedefined - always equal to null

    @Override
    public byte[] transform(
            final ClassLoader loader,
            final String className,
            final Class<?> classBeingRedefined,
            final ProtectionDomain protectionDomain,
            final byte[] classfileBuffer)
            throws IllegalClassFormatException {
        System.out.println(">>> transform()!");
        System.out.println(">>> className!");
        System.out.println(">>> className: " + className);
        System.out.println(">>> classBeingRedefined: " + classBeingRedefined);

        final String[] interfaces = getInterfaces(className, classfileBuffer);
        System.out.println(">>> interfaces: " + Arrays.toString(interfaces));

        return classfileBuffer;
    }

    private String[] getInterfaces(String className, byte[] originalClassfile)
    {
        try
        {
            final String[][] interfaceNames = new String[1][];
            ClassReader cr = new ClassReader(originalClassfile);
            ClassVisitor cv = new EmptyVisitor() {
                @Override
                public void visit(int version, int access, String name,
                                  String signature, String superName, String[] interfaces)
                {
                    interfaceNames[0] = interfaces;
                    super.visit(version, access, name, signature, superName, interfaces);
                }
            };
            cr.accept(cv, 0);
            return interfaceNames[0];
        }
        catch (Throwable th)
        {
            System.err.println("Caught Throwable when trying to instrument: "
                    + className);
            th.printStackTrace();
            return new String[0];
        }
    }
}

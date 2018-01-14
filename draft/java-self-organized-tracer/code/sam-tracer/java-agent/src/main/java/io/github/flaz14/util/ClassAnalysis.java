package io.github.flaz14.util;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

public class ClassAnalysis extends EmptyVisitor {

    private String currentMethod_sig;

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        currentMethod_sig = name + desc;
        System.out.printf("TRACE: sam-tracer has entered into method [%s]%n", currentMethod_sig);
        return this;
    }

    @Override
    public void visitEnd() {
        System.out.printf("TRACE: sam-tracer has leaved method [%s]%n", currentMethod_sig);
        currentMethod_sig = null;
    }
}

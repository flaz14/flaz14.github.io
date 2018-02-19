package io.github.flaz14;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static io.github.flaz14.TraceableRestrictions.shouldBeInstrumented;

public class TraceableClassVisitor extends ClassWriter {
    public TraceableClassVisitor(ClassReader classReader, int flags) {
        super(classReader, flags);
    }

    @Override
    public MethodVisitor visitMethod(final int access,
                                     final String name,
                                     final String desc,
                                     final String signature,
                                     final String[] exceptions) {
        return new TraceableMethodVisitor(
                super.visitMethod(access, name, desc, signature, exceptions)
        );
    }
}

class TraceableMethodVisitor extends MethodAdapter {
    public TraceableMethodVisitor(MethodVisitor mv) {
        super(mv);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        final String incomingMethodSignature = name + desc;
        if (shouldBeInstrumented(incomingMethodSignature)) {
            insertInTrace(name);
            doOriginalCall(opcode, owner, name, desc);
            insertOutTrace(name);
        } else {
            doOriginalCall(opcode, owner, name, desc);
        }
    }

    private void doOriginalCall(int opcode, String owner, String name, String desc) {
        mv.visitMethodInsn(opcode, owner, name, desc);
    }

    private void insertInTrace(final String methodName) {
        /* System.out.println("my-tracer: IN [" + methodName + "]"); */
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("my-tracer: IN [" + methodName + "]");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
    }

    private void insertOutTrace(final String methodName) {
        /* System.out.println("my-tracer: OUT [" + methodName + "]"); */
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("my-tracer: OUT [" + methodName + "]");
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
    }
}

package io.github.flaz14;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static io.github.flaz14.TraceableRestrictions.shouldBeInstrumented;

public class TraceableClassVisitor extends ClassWriter {

    private final byte[] classfileBuffer;

    public TraceableClassVisitor(ClassReader classReader, int flags) {
        super(classReader, flags);
        classfileBuffer = classReader.b;
    }

    @Override
    public MethodVisitor visitMethod(final int access,
                                     final String name,
                                     final String desc,
                                     final String signature,
                                     final String[] exceptions) {
        System.out.println(">>> incomingMethodSignature #1: [" + name + desc + "]");
        return new TraceableMethodVisitor(
                super.visitMethod(access, name, desc, signature, exceptions),
                classfileBuffer
        );
    }
}

class TraceableMethodVisitor extends MethodAdapter {

    private final byte[] classfileBuffer;

    public TraceableMethodVisitor(MethodVisitor mv, byte[] classfileBuffer) {
        super(mv);
        this.classfileBuffer = classfileBuffer;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        final String incomingMethodSignature = name + desc;
        System.out.println(">>> incomingMethodSignature #2: [" + name + desc + "]");
        if (shouldBeInstrumented(incomingMethodSignature) /*&&
                shouldBeInstrumented(classfileBuffer)*/) {
//            System.out.println("Opcodes: \n" +
//                    "int INVOKEVIRTUAL = 182;  - visitMethodInsn \n" +
//                    "int INVOKESPECIAL = 183;  - \n" +
//                    "int INVOKESTATIC = 184;  - \n" +
//                    "int INVOKEINTERFACE = 185;  - \n" +
//                    "int INVOKEDYNAMIC = 186;  - ");
//            System.out.println(">>> incomingMethodSignature: [" + incomingMethodSignature + "]");
//            System.out.println(">>> opcode=[" + opcode + "]\n");

            insertInTrace(name);
            super.visitMethodInsn(opcode, owner, name, desc);
//            doOriginalCall(opcode, owner, name, desc);
//            mv.visitMethodInsn(182, owner, name, desc);
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

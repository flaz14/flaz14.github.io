package io.github.flaz14;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

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
        System.out.println("Visit [" + incomingMethodSignature + "]");
        if (!shouldBeInstrumented(incomingMethodSignature)) {
            doCall(opcode, owner, name, desc);
            return;
        }

//        /* System.err.println("CALL" + name); */
//        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        mv.visitLdcInsn("CALL " + name);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
//
        doCall(opcode, owner, name, desc);
//
//        /* System.err.println("RETURN" + name);  */
//        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//        mv.visitLdcInsn("RETURN " + name);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
//        super.visitMethodInsn(opcode, owner, name, desc);
    }

    private void doCall(int opcode, String owner, String name, String desc) {
        mv.visitMethodInsn(opcode, owner, name, desc);
    }
}

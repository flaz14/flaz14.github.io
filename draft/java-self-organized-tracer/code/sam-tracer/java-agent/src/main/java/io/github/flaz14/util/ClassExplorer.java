package io.github.flaz14.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;

public class ClassExplorer {

    /**
     * Almost copied and pasted from intrace project TODO add link to github
     */
    public static Set<String> interfaces(final String classNameInVm, final byte[] originalClassfile) {
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

package io.github.flaz14.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.commons.EmptyVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toSet;

public class ClassExplorer {




    /*
    private String[] getInterfaces(String className, byte[] originalClassfile) {
        try {
            final String[][] interfaceNames = new String[1][];
            ClassReader cr = new ClassReader(originalClassfile);
            ClassVisitor cv = new EmptyVisitor() {
                @Override
                public void visit(int version, int access, String name,
                                  String signature, String superName, String[] interfaces) {
                    interfaceNames[0] = interfaces;
                    super.visit(version, access, name, signature, superName, interfaces);
                }
            };
            cr.accept(cv, 0);
            return interfaceNames[0];
        } catch (Throwable th) {
            System.err.println("Caught Throwable when trying to instrument: "
                    + className);
            th.printStackTrace();
            return new String[0];
        }
    }
    */

    /**
     * Almost copied and pasted from intrace project TODO add link to github
     *
     * @param classNameInVm
     * @param originalClassfile
     * @return
     */
    public static Set<String> interfaces(final String classNameInVm, final byte[] originalClassfile) {
        final List<String> interfaceNames = new ArrayList<>();
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
        return interfaceNames.stream().
                map(ClassExplorer::humanReadableClassName).
                collect(collectingAndThen(toSet(), Collections::unmodifiableSet));

    }

    static String humanReadableClassName(final String classNameInJvm) {
        return Optional.ofNullable(classNameInJvm).
                map(
                        name -> name.replaceAll("/", ".")
                ).
                orElseThrow(
                        () -> new IllegalArgumentException("classNameInVm should not be null.")
                );
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

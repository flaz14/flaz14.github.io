package io.github.flaz14;

import io.github.flaz14.util.JavaAgent;
import io.github.flaz14.util.Jvm;

/**
 * An entry point to the tracer.
 * <p/>
 * If you would like to start tracing then just add <strong>loader.jar</strong> to runtime classpath and
 * load this class. For example, create dummy instance:
 * <p/>
 * <code>new io.github.flaz14.Loader()</code>
 * <p/>
 * or load the class explicitly:
 * <p/>
 * <code>Class.forName("io.github.flaz14.Loader")</code>
 * <p/>
 * Please note that <strong>tools.jar</strong> from Java SDK should be added to runtime classpath as well.
 * For instance, in Unix this can be achieved via:
 * <p/>
 * <code>java -cp &lt;some JARs&gt;:$JAVA_HOME/lib/tools.jar:&lt;some JARs& ...</code>
 */
public class Loader {

    /**
     * Static initialization block does the trick: it helps to execute some code without
     * <code>main()</code> method. So it's possible to force the JVM attach the agent to
     * itself once <strong>Loader</strong> class is loaded (it doesn't matter whether
     * the class was loaded explicitly or implicitly).
     */
    static {
        if (isDisabled()) {
            System.out.println("WARNING: sam-tracer is disabled.");
        } else {
            JavaAgent.attachToJvm(
                    Jvm.pid()
            );
        }
    }

    /**
     * Loading Java Agent can be unwanted in some situations. For example, the agent
     * should not be loaded during Maven's build. This method analyzes the presence
     * of the specific system property.
     */
    private static boolean isDisabled() {
        final String rawValue = System.getProperty(IS_DISABLED_PROPERTY);
        System.out.printf("[%s]=[%s]%n", IS_DISABLED_PROPERTY, rawValue);
        return Boolean.parseBoolean(rawValue);
    }

    private static final String IS_DISABLED_PROPERTY = "io.github.flaz14.sam-tracer.disabled";
}

package sh.platform.languages;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * It is the result of the compiled demo code in memory.
 * From this class, it will both execute the demo compiled demo code and show the Java demo source.
 * The Sample code class will execute the public <b>static void main method</b> on each demo compiled class.
 */
final class SampleCode {
    private static final Logger LOGGER = Logger.getLogger(SampleCode.class.getName());

    private static final Class[] ARG = new Class[]{String[].class};
    private static final Object NO_ARGS = new String[0];

    private final String source;

    private final Method mainMethod;

    SampleCode(String source, Class<?> demoClass) throws NoSuchMethodException {
        this.source = source;
        this.mainMethod = demoClass.getDeclaredMethod("main", ARG);
    }

    public String getSource() {
        return source;
    }

    /**
     * Executes the public static void main method that might return an exception on execution.
     *
     * @return An exception or {@link Optional#empty()} if executed with success
     */
    public Optional<Exception> execute() {
        try {
            mainMethod.invoke(null, NO_ARGS);
            return Optional.empty();
        } catch (Exception exp) {
            LOGGER.log(Level.SEVERE, "an error happened when executing the demo code", exp);
            return Optional.of(exp);
        }
    }
}

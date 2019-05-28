package sh.platform.languages.compiler;


/**
 * The Java source code to be compiled.
 *
 * @param <T> the source result type
 */
interface JavaSource<T> {

    /**
     * returns the {@link Class#getSimpleName()} to the class compiled
     *
     * @return the {@link Class#getSimpleName()}
     */
    String getSimpleName();

    /**
     * returns the {@link Class#getName()} to the class compiled
     *
     * @return the {@link Class#getName()}
     */
    String getName();

    /**
     * returns the java source code
     *
     * @return the java source code
     */
    String getJavaSource();

    /**
     * Returns the type class that the code will compile
     *
     * @return the Super class from the source code
     */
    Class<T> getType();
}

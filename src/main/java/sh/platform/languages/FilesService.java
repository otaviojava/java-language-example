package sh.platform.languages;

import sh.platform.languages.compiler.JavaCompilerProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class FilesService {

    private JavaCompilerProvider compiler = new JavaCompilerProvider();

    public void init() {
        for (SamplesAvailable available : SamplesAvailable.values()) {

            final String source = convert(available.getFile());
            JavaSource javaSource = new JavaSource(source, available.getFile());
            final Object instance = compiler.apply(javaSource);

        }


    }


    public static void main(String[] args) {
        FilesService filesService = new FilesService();
        filesService.init();
    }


    private String convert(String file)  {
        final InputStream stream = FilesService.class.getClassLoader().getResourceAsStream(file);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new LanguageException("An error when load files", e);
        }
    }

}

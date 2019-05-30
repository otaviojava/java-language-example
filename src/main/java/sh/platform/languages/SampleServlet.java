package sh.platform.languages;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class SampleServlet extends HttpServlet {

    private static final long serialVersionUID = -3462096228274971485L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        final String pathInfo = ofNullable(request.getPathInfo()).orElse("/");
        switch (pathInfo) {
            case "/":
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(SampleCodeType.getOptions());
                return;
            case "/postgresql":
                showSampleCode(response, SampleCodeType.POSTGRESQL);
                return;
            case "/mysql":
                showSampleCode(response, SampleCodeType.MYSQL);
                return;
            case "/mongodb":
                showSampleCode(response, SampleCodeType.MONGODB);
                return;
            case "/redis":
                showSampleCode(response, SampleCodeType.REDIS);
                return;
            case "/postgresql/output":
                executeCode(response, SampleCodeType.POSTGRESQL);
                return;
            case "/mysql/output":
                executeCode(response, SampleCodeType.MYSQL);
                return;
            case "/mongodb/output":
                executeCode(response, SampleCodeType.MONGODB);
                return;
            case "/redis/output":
                executeCode(response, SampleCodeType.REDIS);
            default:
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("text/plain");
                response.getWriter().println("Sorry, no sample code is available.");
        }
    }

    private void showSampleCode(HttpServletResponse response, SampleCodeType key) throws IOException {
        final SampleCode sampleCode = SampleCodeType.getSample(key);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/plain");
        response.getWriter().println(sampleCode.getSource());

    }

    private synchronized void executeCode(HttpServletResponse response, SampleCodeType key) throws IOException {
        PrintStream previous = System.out;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintStream custom = new PrintStream(stream);
        System.setOut(custom);

        final SampleCode sampleCode = SampleCodeType.getSample(key);
        final Optional<Exception> errorMessage = sampleCode.execute();
        System.setOut(previous);
        response.setContentType("text/plain");

        if (errorMessage.isPresent()) {
            final Exception exception = errorMessage.get();
            final String message = exception.getMessage();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(message);
        } else {
            final String message = new String(stream.toByteArray(), StandardCharsets.UTF_8);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(message);
        }
    }
}

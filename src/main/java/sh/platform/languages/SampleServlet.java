package sh.platform.languages;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Optional;

import static java.util.Optional.ofNullable;

public class SampleServlet extends HttpServlet {

    private static final long serialVersionUID = -3462096228274971485L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        final String pathInfo = ofNullable(request.getPathInfo()).orElse("");
        switch (pathInfo) {
            case "/":
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(SamplesAvailable.getOptions());
                return;
            case "/postgresql":
                showSampleCode(response, SamplesAvailable.POSTGRESQL);
                return;
            case "/mysql":
                showSampleCode(response, SamplesAvailable.MYSQL);
                return;
            case "/mongodb":
                showSampleCode(response, SamplesAvailable.MONGODB);
                return;
            case "/redis":
                showSampleCode(response, SamplesAvailable.REDIS);
                return;
            case "/postgresql/output":
                executeCode(response, SamplesAvailable.POSTGRESQL);
                return;
            case "/mysql/output":
                executeCode(response, SamplesAvailable.MYSQL);
                return;
            case "/mongodb/output":
                executeCode(response, SamplesAvailable.MONGODB);
                return;
            case "/redis/output":
                executeCode(response, SamplesAvailable.REDIS);
            default:
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("text/plain");
                response.getWriter().println("Sorry, no sample code is available.");
        }
    }

    private void showSampleCode(HttpServletResponse response, SamplesAvailable key) throws IOException {
        final SampleCode sampleCode = SamplesAvailable.getSample(key);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/plain");
        response.getWriter().println(sampleCode.getSource());

    }

    private synchronized void executeCode(HttpServletResponse response, SamplesAvailable key) throws IOException {
        PrintStream previous = System.out;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        final SampleCode sampleCode = SamplesAvailable.getSample(key);
        final Optional<Exception> errorMessage = sampleCode.execute();
        response.setContentType("text/plain");
        if (errorMessage.isPresent()) {
            final Exception exception = errorMessage.get();
            final String message = exception.getMessage();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(message);
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(sampleCode.getSource());
        }
    }
}

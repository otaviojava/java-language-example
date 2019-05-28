package sh.platform.languages;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class SampleServlet extends HttpServlet {

    private static final long serialVersionUID = -3462096228274971485L;

    private static final Map<SamplesAvailable, SampleCode> SAMPLES = new SampleCodeSupplier().get();

    @Override
    protected void doGet(HttpServletRequest reqest, HttpServletResponse response)
            throws IOException {

        final String pathInfo = reqest.getPathInfo();
        switch (pathInfo) {
            case "/":
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(SamplesAvailable.getOptions());
                return;
            case "postgresql":
                final SampleCode sampleCode = SAMPLES.get(SamplesAvailable.POSTGRESQL);
                if (sampleCode.executeWithSuccess()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("text/plain");
                    response.getWriter().println(sampleCode.getSource());
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("text/plain");
                    response.getWriter().println("There is an error when execute this service");
                }
            default:
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("text/plain");
                response.getWriter().println("Sorry, no sample code is available.");
        }

    }
}
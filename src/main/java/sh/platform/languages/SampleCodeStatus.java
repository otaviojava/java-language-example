package sh.platform.languages;

import java.io.PrintWriter;
import java.io.StringWriter;

final class SampleCodeStatus {

    private final String name;

    private final String source;

    private final String htmlSource;

    private final String output;

    private final boolean success;

    private SampleCodeStatus(SampleCode sampleCode, String output, boolean success) {
        this.name = sampleCode.getLabel();
        this.source = sampleCode.getSource();
        this.htmlSource = sampleCode.getHtmlSource();
        this.output = output;
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public String getOutput() {
        return output;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getSource() {
        return source;
    }

    public String getHtmlSource() {
        return htmlSource;
    }

    public static SampleCodeStatus of(SampleCodeType type) {
        final SampleCode sampleCode = SampleCodeType.getSample(type);
        try {
            String output = sampleCode.execute();
            return new SampleCodeStatus(sampleCode, output, true);
        } catch (Exception exp) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exp.printStackTrace(pw);
            return new SampleCodeStatus(sampleCode, sw.toString(), false);
        }
    }


}

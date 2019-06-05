package sh.platform.languages;

import java.io.PrintWriter;
import java.io.StringWriter;

final class SampleCodeStatus {

    private final String name;

    private final String source;

    private final String output;

    private final boolean success;

    private SampleCodeStatus(String name, String source, String output, boolean success) {
        this.name = name;
        this.source = source;
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


    public static SampleCodeStatus of(SampleCodeType type) {
        final SampleCode sampleCode = SampleCodeType.getSample(type);
        try {
            String output = sampleCode.execute();
            return new SampleCodeStatus(type.getLabel(), sampleCode.getSource(), output, true);
        } catch (Exception exp) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exp.printStackTrace(pw);
            return new SampleCodeStatus(type.getLabel(), sampleCode.getSource(), sw.toString(), false);
        }
    }


}

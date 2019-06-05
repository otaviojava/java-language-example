package sh.platform.languages;

import java.io.PrintWriter;
import java.io.StringWriter;

final class SampleCodeStatus {

    private final SampleCodeType type;

    private final String message;

    private final boolean success;

    private SampleCodeStatus(SampleCodeType type, String message, boolean success) {
        this.type = type;
        this.message = message;
        this.success = success;
    }

    public SampleCodeType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }


    public static SampleCodeStatus of(SampleCodeType type) {
        final SampleCode sampleCode = SampleCodeType.getSample(type);
        try {
            String message = sampleCode.execute();
            return new SampleCodeStatus(type, message, true);
        } catch (Exception exp) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exp.printStackTrace(pw);
            return new SampleCodeStatus(type, sw.toString(), false);
        }
    }


}

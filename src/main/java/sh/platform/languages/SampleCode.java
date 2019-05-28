package sh.platform.languages;

final class SampleCode {

    private final String source;

    private final Object intance;

    SampleCode(String source, Object intance) {
        this.source = source;
        this.intance = intance;
    }

    public String getSource() {
        return source;
    }

    public Object getIntance() {
        return intance;
    }
}

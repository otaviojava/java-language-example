package sh.platform.languages;

import java.util.Locale;
import java.util.Optional;

final class Route {

    private final RouteType route;

    private final SampleCodeType sampleCode;

    private Route(RouteType route, SampleCodeType sampleCode) {
        this.route = route;
        this.sampleCode = sampleCode;
    }

    public enum RouteType {
        SHOW, EXECUTE, DEFAULT, NOT_FOUND;
    }

    public static Route parse(String path) {
        if ("/".equals(path)) {
            return new Route(RouteType.DEFAULT, null);
        }
        String[] paths = path.split("//");
        Optional<SampleCodeType> available = SampleCodeType.parse(paths[0].toUpperCase(Locale.US));


    }
}

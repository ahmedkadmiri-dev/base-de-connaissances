package com.example.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "logging.aspect")
public class LoggingProperties {

    private final Controller controller = new Controller();
    private final Service service = new Service();
    private final Repository repository = new Repository();

    @Getter
    @Setter
    public static class Controller {

        private boolean enabled = true;

        private boolean includeRequestBody = true;

        private boolean includeResponseBody = true;

        private boolean includeHeaders = false;

        private int maxBodyLength = 5000;
    }

    @Getter
    @Setter
    public static class Service {

        private boolean enabled = true;

        private boolean includeParameters = true;

        private boolean includeResult = true;

        private int maxValueLength = 2000;
    }

    @Getter
    @Setter
    public static class Repository {

        private boolean enabled = true;

        private boolean includeParameters = true;

        private boolean includeResult = true;

        private int maxValueLength = 1000;
    }
}

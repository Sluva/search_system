package system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

/**
 * Application class.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    @Override
    protected final SpringApplicationBuilder configure(
            final SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    /**
     * Main method.
     * @param args parameter.
     * @throws Exception intercepts exceptions.
     */
    public static void main(final String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }
}

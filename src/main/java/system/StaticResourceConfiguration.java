package system;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation
        .ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation
        .WebMvcConfigurerAdapter;

/**
 * Configuration.
 */
@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public final void addResourceHandlers(
            final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("/resources/");
    }
}

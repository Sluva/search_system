package searchSystem;

import searchSystem.controller.InformationFactory;
import searchSystem.controller.impl.Twitter4jParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public InformationFactory imageFactory(){
        return new InformationFactory();
    }

    @Bean
    public Twitter4jParser parser(){
        return new Twitter4jParser(5);
    }
}

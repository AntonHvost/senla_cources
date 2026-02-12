package config;

import io.csv.GenericCSVService;
import org.springframework.context.annotation.*;
import util.HibernateUtil;

@Configuration
@ComponentScan(basePackages = {"repository.impl","service", "facade", "ui", "util"})
@PropertySource("classpath:app.properties")
public class SpringConfig {
    @Bean
    public HibernateUtil hibernateUtil() {
        return new HibernateUtil();
    }
    @Bean
    public GenericCSVService genericCSVService() {
        return new GenericCSVService();
    }
}

package hello;

import java.util.Arrays;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class Application {
    //starts Server
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //Extended output in server terminal
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            System.out.println("Lets inspect the beans");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName: beanNames) {
                System.out.println(beanName);
            }
        };
    }
}

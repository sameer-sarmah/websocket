package northwind.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

import northwind.config.AppConfig;


@SpringBootApplication
@Import({AppConfig.class})
public class WebSocketServerJSR356  extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    	return application.sources(WebSocketServerJSR356.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(WebSocketServerJSR356.class, args);
		System.err.println("##########WebSocketServerJSR356#######");
		
	}

}

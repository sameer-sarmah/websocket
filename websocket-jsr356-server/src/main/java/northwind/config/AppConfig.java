package northwind.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import northwind.websocket.endpoint.ProductEndpoint;

@Configuration
@ComponentScan(basePackages= {"northwind"})
public class AppConfig {

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	
	@Bean
	public ScheduledExecutorService  executorService() {
		return  Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
	}
	
//	@Bean
//	public ProductEndpoint productEndpoint() {
//		return new ProductEndpoint();
//	}
	
	  // main one is ServerEndpointExporter which prevents Servlet container's scan for WebSocket
	  @Bean
	  public ServerEndpointExporter endpointExporter(){
	     return new ServerEndpointExporter();
	  }
}

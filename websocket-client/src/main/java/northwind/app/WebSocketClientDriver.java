package northwind.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import northwind.client.WebSocketClient;
import northwind.config.AppConfig;



@SpringBootApplication
@Import({AppConfig.class})
public class WebSocketClientDriver  implements ApplicationRunner {

	@Autowired
	private WebSocketClient webSocketClient;
	
	public static void main(String[] args) {
		SpringApplication.run(WebSocketClientDriver.class, args);
		System.err.println("##########ClientApplication########");
		
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		//webSocketClient.request("ws://localhost:8888/products-stream-ws");
		webSocketClient.request("ws://localhost:8888/products-ws-jsr356");
	}

}

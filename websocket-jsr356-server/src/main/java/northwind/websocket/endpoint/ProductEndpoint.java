package northwind.websocket.endpoint;

import java.io.IOException;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import northwind.service.ProductService;

@Component
@ServerEndpoint(value = "/products-ws-jsr356")
public class ProductEndpoint {

	private final Map<String, Session> idToActiveSession = new ConcurrentHashMap<>();
	
	private ProductService productService = new ProductService();
	
	@Autowired
	private ScheduledExecutorService  executorService =Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
	
	public ProductEndpoint() {}

	@OnOpen
	public void afterConnectionEstablished(Session session) throws Exception {
		idToActiveSession.put(session.getId(), session);
		System.out.println(String.format("Connection to client with sessionId=%s,TextMessageSizeLimit=%d,RemoteUrl=%s established.",
				session.getId(),session.getMaxTextMessageBufferSize(),session.getBasicRemote()));


		executorService.scheduleAtFixedRate(()->{
			idToActiveSession.forEach((sessionId,webSocketSession)->{
				int productId = new Random().nextInt(75) + 1;
				String productJson = productService.getProductWithId(Integer.toString(productId));
				try {
					webSocketSession.getBasicRemote().sendText(productJson);
				} catch (IOException e) {
					System.err.println(e.getMessage());
				}
			});
		}, 0, 5, TimeUnit.SECONDS);
	}
	
	@OnMessage
	public void handleTextMessage(Session session, String message)
            throws Exception {
    	int productId = new Random().nextInt(75) + 1;
		String productJson = productService.getProductWithId(Integer.toString(productId));
		session.getBasicRemote().sendText(productJson);
		System.out.println(String.format("Product sent to client with sessionId=%s,product=%s",session.getId(),productJson));
		
		
    }
	
	@OnError
	public void errorHandler(Session session, Throwable exception) {
		System.err.println(exception.getMessage());
	}
	
	@OnClose
	public void afterConnectionClosed(Session session) throws Exception {
		idToActiveSession.remove(session.getId());
		System.out.println(String.format("Connection to client with sessionId=%s closed.",session.getId()));	
	}
}

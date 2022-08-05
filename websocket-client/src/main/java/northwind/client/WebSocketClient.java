package northwind.client;

import java.net.URI;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import northwind.exception.CoreException;
import northwind.websocket.handler.AddedProductHandler;

@Component
public class WebSocketClient {


	public void request(String url) throws CoreException {
		try {
			StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
			webSocketClient.getUserProperties().clear();
			WebSocketHandler webSocketHandler = new AddedProductHandler();
			ListenableFuture<WebSocketSession> webSocketSessionFuture = webSocketClient.doHandshake(webSocketHandler, new WebSocketHttpHeaders(), URI.create(url));
			WebSocketSession webSocketSession = webSocketSessionFuture.get();
		}  catch (Exception e) {
			throw new CoreException(e.getMessage(), 500);
		}
	}
	
	public static void analyseKeystore(KeyStore keyStore,List<String> publicKeys,String privateKeyName) {
		try {
			System.out.println(String.format("Size of keystore: %s, type of keystore: %s ",keyStore.size(),keyStore.getType()));
			publicKeys.stream().forEach((publicKey) ->{
				try {
					Certificate clientCertificate = keyStore.getCertificate(publicKey);
					analyseCertificate(clientCertificate);
				} catch (KeyStoreException e) {
					e.printStackTrace();
				}	
			});
			Key privateKey = keyStore.getKey(privateKeyName, "password".toCharArray());
			System.out.println(String.format("algorithm : %s,format : %s",privateKey.getAlgorithm(),privateKey.getFormat()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void analyseCertificate(Certificate certificate) {
		PublicKey serverPublicKey = certificate.getPublicKey();
		System.out.println(String.format("algorithm : %s,format : %s",serverPublicKey.getAlgorithm(),serverPublicKey.getFormat()));
		try {
			certificate.verify(serverPublicKey);
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException e) {
			e.printStackTrace();
		}
	}
}



package loggers;

import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import models.History;

public class RemoteListener implements Listener {
	String ip;
	int port;
	RestClient client;

	public RemoteListener(String ip, int port) {
		// TODO Auto-generated constructor stub
		this.ip = ip;
		this.port = port;  
		this.client = RestClient.create();
	}
	

	@Override
	public void update(History history) {
	    String baseUri = "http://" + this.ip + ":" + this.port + "/update";
	    
	    try {
	        this.client.post()
	            .uri(baseUri)
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(history)
	            .retrieve()
	            .toBodilessEntity();
	    } catch (Exception e) {
	        System.err.println("Failed to send update to " + baseUri + ": " + e.getMessage());
	    }
	}
}

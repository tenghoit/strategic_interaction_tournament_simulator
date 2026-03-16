package robots;

import java.util.ArrayList;

import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;

import models.History;
import models.MatchDetails;

public class RemoteBot extends Robot {

	public RemoteBot(String name, String ip, String port) {
		super(name);
		// TODO Auto-generated constructor stub
		this.client = RestClient.create();
		this.ip = ip;
		this.port = port;
	}
	RestClient client;
	String ip;
	String port;

	@Override
	public String getAction(String opponentName, ArrayList<History> history) {
		// TODO Auto-generated method stub
		MatchDetails details = new MatchDetails(opponentName, history);
		String uri = "http://" + this.ip + ":" + this.port + "/action";
		String action = this.client.post() 
				.uri(uri) 
				.contentType(MediaType.APPLICATION_JSON) 
				.body(details) 
				.retrieve()
				.body(String.class); 
		
		return action;
	}

}

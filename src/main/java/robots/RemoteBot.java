package robots;

import java.util.ArrayList;
import java.util.List; // Use the interface
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;

import models.Action;
import models.History;
import models.MatchDetails;

public class RemoteBot extends Robot {

    private final RestClient client;
    private final String ip;
    private final int port;

    public RemoteBot(String name, String ip, int port) {
        super(name);
        this.client = RestClient.create();
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String getAction(String opponentName, ArrayList<History> history) {
        MatchDetails details = new MatchDetails(opponentName, history);
        
        // Construct the full URL for the client robot
        String uri = "http://" + this.ip + ":" + this.port + "/action";
        System.out.println("RemoteBot: Requesting action from " + uri);

        try {
        	Action action = this.client.post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON) // CRITICAL: Tell the client this is JSON
                    .body(details)
                    .retrieve()
                    .body(Action.class);
            
            System.out.println("RemoteBot: Received action: " + action.action());
            return action.action();
            
            
        } catch (Exception e) {
            System.err.println("RemoteBot: failed to get action from " + uri);
            e.printStackTrace();
            return "ERROR"; // Or a default move like "COOPERATE"
        }
    }
}
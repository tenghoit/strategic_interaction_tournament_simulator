package robots;

import java.util.ArrayList;
import java.util.List; // Use the interface
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
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

        try {
            return this.client.post()
                    .uri(uri)
                    .contentType(MediaType.APPLICATION_JSON) // CRITICAL: Tell the client this is JSON
                    .body(details)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            System.err.println("Communication failure with bot " + getName() + " at " + uri);
            return "ERROR"; // Or a default move like "COOPERATE"
        }
    }
}
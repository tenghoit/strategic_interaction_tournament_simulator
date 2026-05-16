import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.client.RestTestClient;

import models.History;
import models.MatchDetails;
import networking.client.NetworkedTournamentClient;
import robots.Defector;
import robots.Reciprocator;
import robots.RemoteBot;
import robots.Robot;

import org.springframework.beans.factory.annotation.Autowired;



@SpringBootTest(
		webEnvironment = WebEnvironment.RANDOM_PORT,
		classes = NetworkedTournamentClient.class
)

@AutoConfigureRestTestClient
public class ClientTest {

	@Autowired
	private NetworkedTournamentClient client;
	
	@Autowired
	private RestTestClient tClient;
	

	@Test
	void testClient() {
		MatchDetails details = new MatchDetails("", new ArrayList<History>());
		
//		RemoteBot rb = new RemoteBot("jeff", client.getAssignedIP(), client.getAssignedPort());
		RemoteBot rb = new RemoteBot("jeff", "localhost", client.getAssignedPort()); // testing uses localhost
		
		assertEquals("COOPERATE", rb.getAction("", new ArrayList<History>()));
		
		client.setBot(new Defector("Carl"));
		assertEquals("DEFECT", rb.getAction("", new ArrayList<History>()));
		
		
		ArrayList<History> history = new ArrayList<History>();
		history.add(new History("Bob", "Charles", "DEFECT", "COOPERATE", 0, 5));
		client.setBot(new Reciprocator("Charles"));
		
		assertEquals("DEFECT", rb.getAction("Bob", history));
		
		history.add(new History("Bob", "Charles", "COOPERATE", "COOPERATE", 3, 3));		
		assertEquals("COOPERATE", rb.getAction("Bob", history));
		
	}
}

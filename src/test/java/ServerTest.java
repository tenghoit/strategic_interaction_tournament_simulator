
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.client.RestTestClient;

import games.PrisonerDilemma;
import models.RegistrationRequest;
import networking.server.NetworkedTournamentServer;
import tournaments.RoundRobin;

import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest(
		webEnvironment = WebEnvironment.RANDOM_PORT,
		classes = NetworkedTournamentServer.class
)
@AutoConfigureRestTestClient

public class ServerTest {

	@Autowired
	private NetworkedTournamentServer server;
	
	@Autowired
	private RestTestClient tClient;
	
	@Test
	void testInteraction() {
		
		String[] expectedTournaments = {"RoundRobin PD"};
		tClient.get().uri("/openTournaments").exchange()
		.expectBody(String[].class)
		.isEqualTo(expectedTournaments);
		
		assertEquals(1, server.getTournamentsByStatus(true).size());
	
		
		RegistrationRequest badRequest = new RegistrationRequest("Bracket PD", "Jeff", "192.0.0.1", 1000);
		RegistrationRequest goodRequest = new RegistrationRequest("RoundRobin PD", "Jeff", "192.0.0.1", 1000);
		
		tClient.post().uri("/register")
        .body(badRequest) 
        .exchange()
        .expectStatus().isOk()
        .expectBody(Boolean.class)
        .isEqualTo(false);
		
		
		tClient.post().uri("/register")
        .body(goodRequest)
        .exchange()
        .expectStatus().isOk()
        .expectBody(Boolean.class)
        .isEqualTo(true);
		
		
		for(int i = 0; i < 3; i++) {
			tClient.post().uri("/register")
	        .body(new RegistrationRequest("RoundRobin PD", "Jeff", "192.0.0.1", 1000))
	        .exchange()
	        .expectStatus().isOk()
	        .expectBody(Boolean.class)
	        .isEqualTo(true);
		}
		
		assertEquals(0, server.getTournamentsByStatus(true).size());
		
		server.addTournament(new RoundRobin("RoundRobin PD 2", new PrisonerDilemma()));
		
		tClient.get().uri("/openTournaments")
		.exchange()
		.expectBody(String[].class)
		.isEqualTo(new String[] {"RoundRobin PD 2"});
		
		tClient.get().uri("/closedTournaments")
		.exchange()
		.expectBody(String[].class)
		.isEqualTo(new String[] {"RoundRobin PD"});
		
	}

}

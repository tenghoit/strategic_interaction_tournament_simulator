import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.client.RestTestClient;

import models.RegistrationRequest;
import networking.server.NetworkedTournamentServer;

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
		tClient.get().uri("/tournaments").exchange()
		.expectBody(String[].class)
		.isEqualTo(expectedTournaments);
	
		
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
	}
	

}

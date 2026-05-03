import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.client.RestTestClient;

import networking.server.NetworkedTournamentServer;

@SpringBootTest(
		classes = NetworkedTournamentServer.class
)

@AutoConfigureRestTestClient
public class TestModel {
	
	@Autowired
	private NetworkedTournamentServer server;
	
	@Autowired
	private RestTestClient tClient;

	public TestModel() {
		// TODO Auto-generated constructor stub
	}

}

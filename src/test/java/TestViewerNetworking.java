import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.testfx.assertions.api.Assertions;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import loggers.RemoteListener;
import models.History;
import models.Model;

@SpringBootTest(
		webEnvironment = WebEnvironment.RANDOM_PORT,
		classes = Model.class
)

@AutoConfigureRestTestClient

public class TestViewerNetworking {
	
	@Autowired
	private Model model;

	@Test
	void testRemoteListener() {
		ObservableList<History> histories = FXCollections.observableArrayList(
				new History("Bob", "Charles", "DEFECT", "COOPERATE", 5, 0),
				new History("Alice", "Charles", "COOPERATE", "COOPERATE", 3, 3)
		);
		
		RemoteListener rl = new RemoteListener(model.getInternalIP(), model.getInternalPort());
		
		Assertions.assertThat(model.getEvents()).hasSize(0);
		
		rl.update(histories.get(0));
		Assertions.assertThat(model.getEvents()).hasSize(1);
		
		rl.update(histories.get(1));
		Assertions.assertThat(model.getEvents()).hasSize(2);
	}
}

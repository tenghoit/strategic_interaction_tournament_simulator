import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.web.client.MockRestServiceServer;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import loggers.RemoteListener;
import models.History;
import models.Model;
import models.ViewTransitionalModel;
import networking.server.NetworkedTournamentServer;

@SpringBootTest(
		webEnvironment = WebEnvironment.RANDOM_PORT,
		classes = Model.class
		
)

@AutoConfigureRestTestClient
@ExtendWith(ApplicationExtension.class)
public class TestViewerNetworking {
	
	private static ConfigurableApplicationContext serverContext;
	
	@Autowired
	private Model model;
	
	BorderPane root;
	ViewTransitionalModel vm;
	Scene s;
	Stage stage;
	
	@BeforeAll
    public static void startTournamentServer() {
        serverContext = new SpringApplicationBuilder(NetworkedTournamentServer.class)
                .profiles("server")
                // Force the port here to ensure it matches your test expectations
                .properties("server.port=9090") 
                .run();
        
        System.out.println("### TOURNAMENT SERVER STARTED ON PORT 9090 ###");
    }
	
	@Start  //Before
	private void start(Stage stage)
	{
	
		
		root = new BorderPane();
		
		vm = new ViewTransitionalModel(root, model); 
		
		vm.showTournamentList();
	  
		s = new Scene(root);
		this.stage = stage;
		this.stage.setScene(s);
		this.stage.show();
		
	}
	
	@AfterAll
    public static void stopTournamentServer() {
        if (serverContext != null) {
            serverContext.close();
            System.out.println("### TEST SERVER SHUT DOWN ###");
        }
    }

	@Test
	void testRemoteListener() {
		ObservableList<History> histories = FXCollections.observableArrayList(
				new History("Bob", "Charles", "DEFECT", "COOPERATE", 5, 0),
				new History("Alice", "Charles", "COOPERATE", "COOPERATE", 3, 3)
		);
		
		RemoteListener rl = new RemoteListener("localhost", model.getInternalPort());
		
		Assertions.assertThat(model.getEvents()).hasSize(0);
		
		rl.update(histories.get(0));
		Assertions.assertThat(model.getEvents()).hasSize(1);
		
		rl.update(histories.get(1));
		Assertions.assertThat(model.getEvents()).hasSize(2);
	}
	
	@Test
	public void TestServerButtons(FxRobot robot) {
		changeServerIP(robot, "localhost");
		changeServerPort(robot, "9090");
		
		Button btn = robot.lookup("#connectBtn").queryAs(Button.class);
		robot.interact(() -> {
			btn.requestFocus();
			btn.fire();
		});
		
		
		WaitForAsyncUtils.waitForFxEvents();
		
		
		ObservableList<String> tournaments = FXCollections.observableArrayList("TestTournament", "AlmostFullTournament");
		
		ListView<String> openTournaments = getOpenTournaments(robot);
		
		Assertions.assertThat(model.getOpenTournaments()).hasSize(tournaments.size());
		
		Assertions.assertThat(openTournaments).hasExactlyNumItems(tournaments.size());
		
		for(String name: tournaments) {
			Assertions.assertThat(openTournaments).hasListCell(name);
		}
		

		
		WaitForAsyncUtils.waitForFxEvents();
		
		ListView<String> closedTournaments = getClosedTournaments(robot);
		
		closedTournaments.getSelectionModel().clearAndSelect(0);
		
		Button btn2 = robot.lookup("#spectateBtn").queryAs(Button.class);
		robot.interact(() -> {
			btn2.requestFocus();
			btn2.fire();
		});
		
		Assertions.assertThat(robot.lookup("#backBtn")).isNotNull();
		
	}
	
	
	@SuppressWarnings("unchecked")
	private ListView<String> getOpenTournaments(FxRobot robot){
		return (ListView<String>) robot.lookup("#OpenTournamentsListView").queryAll().iterator().next();
	}
	
	@SuppressWarnings("unchecked")
	private ListView<String> getClosedTournaments(FxRobot robot){
		return (ListView<String>) robot.lookup("#ClosedTournamentsListView").queryAll().iterator().next();
	}
	
	
	private void changeServerPort(FxRobot robot, String serverPort)
	{
		TextField portField = robot.lookup("#ServerPortTextField").queryAs(TextField.class);
		robot.interact(() -> {
		    portField.requestFocus();
		    portField.setText(serverPort);
		});
	}
	
	
	private void changeServerIP(FxRobot robot, String serverIP)
	{	
		TextField portField = robot.lookup("#ServerIPTextField").queryAs(TextField.class);
		robot.interact(() -> {
		    portField.requestFocus();
		    portField.setText(serverIP);
		});
	}
}

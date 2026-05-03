
//import org.assertj.core.api.Assertions;
import org.testfx.assertions.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.testfx.api.FxRobot;
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
import models.ViewTransitionalModel;
import networking.server.NetworkedTournamentServer;
import models.Model;

import org.springframework.beans.factory.annotation.Autowired;



@ExtendWith(ApplicationExtension.class)
public class TestTournamentListView {
	
	
	Model model;
	BorderPane root;
	ViewTransitionalModel vm;
	Scene s;
	Stage stage;
	
	@Start  //Before
	private void start(Stage stage)
	{
		model = new Model();
		
		root = new BorderPane();
		
		vm = new ViewTransitionalModel(root, model); 
		
		vm.showTournamentList();
	  
		s = new Scene(root);
		this.stage = stage;
		this.stage.setScene(s);
		this.stage.show();
	    
	}


	private void checkServerIP(FxRobot robot, String serverIP) {		
		Assertions.assertThat(robot.lookup("#ServerIPTextField").queryAs(TextField.class)).hasText(serverIP);	
	}
	
	private void changeServerIP(FxRobot robot, String serverIP)
	{	
		TextField portField = robot.lookup("#ServerIPTextField").queryAs(TextField.class);
		robot.interact(() -> {
		    portField.requestFocus();
		    portField.setText(serverIP);
		});
	}

	
	
	private void checkServerPort(FxRobot robot, String serverPort) {
		Assertions.assertThat(robot.lookup("#ServerPortTextField").queryAs(TextField.class)).hasText(serverPort);
	}
	
	private void changeServerPort(FxRobot robot, String serverPort)
	{
		TextField portField = robot.lookup("#ServerPortTextField").queryAs(TextField.class);
		robot.interact(() -> {
		    portField.requestFocus();
		    portField.setText(serverPort);
		});
	}
	
	@Test
	public void testServerIP(FxRobot robot) {
		checkServerIP(robot, "");
		changeServerIP(robot, "1.1.1.1");
		checkServerIP(robot, "1.1.1.1");
	}
	


	@Test
	public void testServerPort(FxRobot robot) {
		checkServerPort(robot, "");
		changeServerPort(robot, "8000");
		checkServerPort(robot, "8000");
	}
	
	@SuppressWarnings("unchecked")
	private ListView<String> getOpenTournaments(FxRobot robot){
		return (ListView<String>) robot.lookup("#OpenTournamentsListView").queryAll().iterator().next();
	}
	
	@SuppressWarnings("unchecked")
	private ListView<String> getClosedTournaments(FxRobot robot){
		return (ListView<String>) robot.lookup("#ClosedTournamentsListView").queryAll().iterator().next();
	}
	
		
	@Test
	public void TestOpenTournaments(FxRobot robot) {
		ListView<String> openTournaments = getOpenTournaments(robot);
		Assertions.assertThat(openTournaments).isEmpty();
		
		ObservableList<String> tournaments = FXCollections.observableArrayList("t1", "t2");
		
		model.setOpenTournaments(tournaments);
		
		WaitForAsyncUtils.waitForFxEvents();
		
		Assertions.assertThat(openTournaments).hasExactlyNumItems(tournaments.size());
		
		for(String name: tournaments) {
			Assertions.assertThat(openTournaments).hasListCell(name);
		}
	
	}
	
	
	@Test
	public void TestClosedTournaments(FxRobot robot) {
		
		
		ListView<String> closedTournaments = getClosedTournaments(robot);
		Assertions.assertThat(closedTournaments).isEmpty();
		
		ObservableList<String> tournaments = FXCollections.observableArrayList("t3", "t4");
		
		model.setClosedTournaments(tournaments);
		
		WaitForAsyncUtils.waitForFxEvents();  
		
		Assertions.assertThat(closedTournaments).hasExactlyNumItems(tournaments.size());
		
		for(String name: tournaments) {
			Assertions.assertThat(closedTournaments).hasListCell(name);
		}
	
		
		Assertions.assertThat(closedTournaments.getSelectionModel().isEmpty());
		
		closedTournaments.getSelectionModel().clearAndSelect(0);
		WaitForAsyncUtils.waitForFxEvents();
		
		Assertions.assertThat(closedTournaments.getSelectionModel().getSelectedItem()).isEqualTo("t3");
		
		
	}
	


}

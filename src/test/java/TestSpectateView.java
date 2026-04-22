import org.testfx.assertions.api.Assertions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.ViewTransitionalModel;
import models.History;
import models.Model;

@ExtendWith(ApplicationExtension.class)
public class TestSpectateView {

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
		
		vm.showSpectateView();
	  
		s = new Scene(root);
		this.stage = stage;
		this.stage.setScene(s);
		this.stage.show();
	}
	
	@Test
	public void testTitle(FxRobot robot) {
		Assertions.assertThat(robot.lookup("#ActiveTournamentNameLabel").queryAs(Label.class)).hasText("");
		robot.interact(() -> {
	        this.model.setSelectedTournament("t3");
	    });
		Assertions.assertThat(robot.lookup("#ActiveTournamentNameLabel").queryAs(Label.class)).hasText("t3");

	}
	
	@Test
	public void testBack(FxRobot robot) {
		Assertions.assertThat(robot.lookup("#backBtn")).isNotNull();
		robot.clickOn("#backBtn");
		WaitForAsyncUtils.waitForFxEvents();
		Assertions.assertThat(robot.lookup("#spectateBtn")).isNotNull();
	}
	
	@SuppressWarnings("unchecked")
	private ListView<History> getEvents(FxRobot robot){
		return (ListView<History>) robot.lookup("#EventListView").queryAll().iterator().next();
	}

	@Test
	public void testEvents(FxRobot robot) {
		ListView<History> events = getEvents(robot);
		Assertions.assertThat(events).isEmpty();
		
		ObservableList<History> histories = FXCollections.observableArrayList(
				new History("Bob", "Charles", "DEFECT", "COOPERATE", 5, 0),
				new History("Alice", "Charles", "COOPERATE", "COOPERATE", 3, 3)
		);
		
		model.setEvents(histories);
		WaitForAsyncUtils.waitForFxEvents();
		
		Assertions.assertThat(events).hasExactlyNumItems(histories.size());
		
		for(History match: histories) {
			Assertions.assertThat(events).hasListCell(match);
		}
	
		
		
	}

}

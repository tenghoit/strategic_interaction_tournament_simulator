package main;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import models.History;
import models.Model;
import models.ViewTransitionalModel;
import views.TournamentListController;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private ConfigurableApplicationContext context;

    @Override
    public void init() {
        // This starts the Spring Boot server BEFORE JavaFX starts the UI
        this.context = new SpringApplicationBuilder(Model.class)
                .profiles("random")
                .run();
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Model model = this.context.getBean(Model.class);
		
		BorderPane root = new BorderPane();
		
		ViewTransitionalModel vm = new ViewTransitionalModel(root, model); 
		
		vm.showTournamentList();
		
		Scene s = new Scene(root);
		primaryStage.setScene(s);
		primaryStage.show();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

}

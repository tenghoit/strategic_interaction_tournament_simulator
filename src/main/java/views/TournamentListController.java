package views;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.Model;
import models.ViewTransitionModelInterface;

public class TournamentListController {
	
	Model model;
	ViewTransitionModelInterface transitionModel;

	public TournamentListController() {
		// TODO Auto-generated constructor stub
	}
	
	public void setModel(Model newModel, ViewTransitionModelInterface transitionModel) {
		this.model = newModel;
		this.transitionModel = transitionModel;
		
		Bindings.bindBidirectional(ServerIPTextField.textProperty(), this.model.getServerIP());
		Bindings.bindBidirectional(ServerPortTextField.textProperty(), this.model.getServerPort());
		
		OpenTournamentsListView.setItems(this.model.getOpenTournaments());
		ClosedTournamentsListView.setItems(this.model.getClosedTournaments());
	}
	
    @FXML
    private ListView<String> ClosedTournamentsListView;

    @FXML
    private ListView<String> OpenTournamentsListView;

    @FXML
    private TextField ServerIPTextField;

    @FXML
    private TextField ServerPortTextField;
    
    @FXML
    private Button connectBtn;

    @FXML
    private Button spectateBtn;

    @FXML
    void onConnect(ActionEvent event) {
    	System.out.println("IP: " + this.model.getServerIP().get() + " | Port: " + this.model.getServerPort().get());
//    	this.model.connect();
    }

    @FXML
    void onSpectate(ActionEvent event) {
    	String selectedTournament = ClosedTournamentsListView.getSelectionModel().getSelectedItem();
    	
    	if(selectedTournament == null) {
    		System.out.println("Please select a tournament");
    		return;
    	}
    	
    	this.model.setSelectedTournament(selectedTournament);
    	System.out.println("Selected: " + this.model.getSelectedTournament().get());

//    	this.model.spectate();
    	this.transitionModel.showSpectateView();
    	
    }

}

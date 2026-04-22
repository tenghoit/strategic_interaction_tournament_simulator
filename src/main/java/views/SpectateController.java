package views;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import models.History;
import models.Model;
import models.ViewTransitionModelInterface;

public class SpectateController {
	
	Model model;
	ViewTransitionModelInterface transitionModel;
	
	public void setModel(Model newModel, ViewTransitionModelInterface transitionModel) {
		this.model = newModel;
		this.transitionModel = transitionModel;
		
		Bindings.bindBidirectional(ActiveTournamentNameLabel.textProperty(), this.model.getSelectedTournament());
		EventListView.setItems(this.model.getEvents());
	}

    @FXML
    private Label ActiveTournamentNameLabel;
    
    @FXML
    private Button backBtn;

    @FXML
    private ListView<History> EventListView;

    public SpectateController() {
		super();
	}

	@FXML
    void back(ActionEvent event) {
    	this.transitionModel.showTournamentList();
    }

}

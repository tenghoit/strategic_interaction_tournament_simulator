package models;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import views.SpectateController;
import views.TournamentListController;

public class ViewTransitionalModel implements ViewTransitionModelInterface {

	BorderPane mainview;
	Model model;
	
	public ViewTransitionalModel(BorderPane mainview, Model model) {
		super();
		this.mainview = mainview;
		this.model = model;
	}
	

	@Override
	public void showTournamentList() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(ViewTransitionalModel.class.getResource("/views/TournamentListView.fxml"));
	    try {
	    	BorderPane view = loader.load();
	    	mainview.setCenter(view);
	    	TournamentListController cont = loader.getController();
	    	cont.setModel(model, this);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }

	}

	@Override
	public void showSpectateView() {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();
	    loader.setLocation(ViewTransitionalModel.class.getResource("/views/SpectateView.fxml"));
	    try {
	    	BorderPane view = loader.load();
	    	mainview.setCenter(view);
	    	SpectateController cont = loader.getController();
	    	cont.setModel(model, this);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}

}

package models;

import org.springframework.web.client.RestClient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Model {
	
	ObservableList<String> openTournaments = FXCollections.observableArrayList();
	ObservableList<String> closedTournaments = FXCollections.observableArrayList();
	ObservableList<History> events = FXCollections.observableArrayList();
	
	StringProperty serverIP;
	StringProperty serverPort;
	StringProperty selectedTournament;
	
	
	RestClient restClient;
	

	public Model() {
		// TODO Auto-generated constructor stub
		this.serverIP = new SimpleStringProperty("");
		this.serverPort = new SimpleStringProperty("");
		this.selectedTournament = new SimpleStringProperty("");
		
		this.restClient = RestClient.create();
		
//		ObservableList<String> tournaments = FXCollections.observableArrayList("t3", "t4");
//		this.setClosedTournaments(tournaments);
		
//		ObservableList<History> events = FXCollections.observableArrayList(new History("Bob", "Charles", "DEFECT", "COOPERATE", 5, 0));
//		this.setEvents(events);
		
	}
	
	public ObservableList<History> getEvents() {
		return events;
	}

	public void setEvents(ObservableList<History> events) {
		this.events.setAll(events);
	}

	public StringProperty getSelectedTournament() {
		return selectedTournament;
	}

	public void setSelectedTournament(String selectedTournament) {
		this.selectedTournament.set(selectedTournament);
	}
	
	public void spectate(){
		// subscribe as listener to selectedTournament (server)
		String baseURI = String.format("http://%s:%s", this.serverIP.get(), this.serverPort.get());
//		SpectateRequest = new SpectateRequest("") would need to know client ip, networkClient's job?
		
	}
	public void connect() {
		// use server info to get tournaments
		String baseURI = String.format("http://%s:%s", this.serverIP.get(), this.serverPort.get());
		
		
		
		String[] openTournaments = this.restClient
			.get()
			.uri(baseURI + "/openTournaments")
			.retrieve()
			.body(String[].class);
		
		this.setOpenTournaments(FXCollections.observableArrayList(openTournaments));
		
		
		
		String[] closedTournaments = this.restClient
				.get()
				.uri(baseURI + "/closedTournaments")
				.retrieve()
				.body(String[].class);
			
		this.setClosedTournaments(FXCollections.observableArrayList(closedTournaments));
		
	}

	public ObservableList<String> getOpenTournaments() {
		return openTournaments;
	}

	public void setOpenTournaments(ObservableList<String> openTournaments) {
		this.openTournaments.setAll(openTournaments);
	}

	public ObservableList<String> getClosedTournaments() {
		return closedTournaments;
	}

	public void setClosedTournaments(ObservableList<String> closedTournaments) {
		this.closedTournaments.setAll(closedTournaments);
	}

	public StringProperty getServerIP() {
		return serverIP;
	}

	public StringProperty getServerPort() {
		return serverPort;
	}

}

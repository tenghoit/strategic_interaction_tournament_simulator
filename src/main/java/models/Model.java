package models;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.server.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@SpringBootApplication
@RestController
public class Model {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(Model.class)
		.profiles("random")
		.run(args);
	}
	
	ObservableList<String> openTournaments;
	ObservableList<String> closedTournaments;
	ObservableList<History> events;
	
	PlaybackInvoker playbackInvoker;
	
	StringProperty serverIP;
	StringProperty serverPort;
	StringProperty selectedTournament;
	
	
	RestClient restClient;
	String internalIP;
	public String getInternalIP() {
		return internalIP;
	}

	public int getInternalPort() {
		return internalPort;
	}

	int internalPort;
	
	@Autowired
	private ServletWebServerApplicationContext serverContext;
	

	public RestClient getRestClient() {
		return restClient;
	}

	public Model() {
		// TODO Auto-generated constructor stub
		this.serverIP = new SimpleStringProperty("");
		this.serverPort = new SimpleStringProperty("");
		this.selectedTournament = new SimpleStringProperty("");
		
		this.restClient = RestClient.create();
		
		openTournaments = FXCollections.observableArrayList();
		closedTournaments = FXCollections.observableArrayList();
		events = FXCollections.observableArrayList();
		
//		ObservableList<String> tournaments = FXCollections.observableArrayList("t3", "t4");
//		this.setClosedTournaments(tournaments);
		
//		ObservableList<History> events = FXCollections.observableArrayList(new History("Bob", "Charles", "DEFECT", "COOPERATE", 5, 0));
//		this.setEvents(events);
		
		this.playbackInvoker = new PlaybackInvoker();
		
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
		String baseURI = this.getServerURI();
		SpectateRequest req = new SpectateRequest(this.selectedTournament.get(), this.internalIP, this.internalPort);
		
		Boolean result = this.restClient.post()
			.uri(baseURI + "/spectate")
			.body(req)
			.contentType(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(Boolean.class);

		if(result){
			System.out.println("Successfully joined as spectator to " + selectedTournament.get() + " on " + getServerURI());
		}
	}
	
	public void connect() {
		// use server info to get tournaments
		String baseURI = this.getServerURI();
		
		String[] openTournaments = this.restClient
			.get()
			.uri(baseURI + "/openTournaments")
			.retrieve()
			.body(String[].class);
		
		this.setOpenTournaments(FXCollections.observableArrayList(openTournaments));
		
		System.out.println(openTournaments);
		
		
		
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
	
	@Bean
    public ApplicationListener<ServletWebServerInitializedEvent> serverPortListenerBean() {
        return event -> {
            this.internalPort = event.getWebServer().getPort();
            System.out.println("Port is "+this.internalPort);
            
            try {
				this.internalIP = InetAddress.getLocalHost().getHostAddress();
				this.internalIP = "localhost";
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        };
    }
	
	public String getServerURI() {
		return String.format("http://%s:%s", this.serverIP.get(), this.serverPort.get());
	}
	
	@PostMapping("/update")
	public void update(@RequestBody History match) {
	    // This ensures the UI update happens on the JavaFX thread
	    javafx.application.Platform.runLater(() -> {
	        this.playbackInvoker.add(new PlaybackCommand(match, this.events));
	    });
	    System.out.println("Received update");
	}
	
	public void togglePlayback() {
		this.playbackInvoker.togglePlayback();
	}
	
	public void forward() {
		this.playbackInvoker.forward();
	}

	public void previous() {
		this.playbackInvoker.previous();
	}
}

package networking.server;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.server.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import games.Game;
import games.PrisonerDilemma;
import loggers.RemoteListener;
import models.RegistrationRequest;
import models.SpectateRequest;
import robots.Cooperator;
import robots.Defector;
import robots.Reciprocator;
import robots.RemoteBot;
import robots.Robot;
import tournaments.RoundRobin;
import tournaments.Tournament;

@SpringBootApplication
@RestController
public class NetworkedTournamentServer {

	public static void main(String[] args) {
        new SpringApplicationBuilder(NetworkedTournamentServer.class)
        .profiles("server")
        .run(args);
	}
	
	ArrayList<Tournament> tournaments;
	
	
	public NetworkedTournamentServer() {
		Game pd = new PrisonerDilemma();
		this.tournaments = new ArrayList<Tournament>();
		this.tournaments.add(new RoundRobin("TestTournament", pd));
		
		Tournament fullTournament = new RoundRobin("FullTournament", pd);
		fullTournament.addPlayer(new Cooperator("Alice"));
		fullTournament.addPlayer(new Defector("Bob"));
		fullTournament.addPlayer(new Reciprocator("Charles"));
		fullTournament.addPlayer(new Cooperator("Dave"));
		
		this.tournaments.add(fullTournament);
		
		Tournament almostFullTournament = new RoundRobin("AlmostFullTournament", pd);
		almostFullTournament.addPlayer(new Cooperator("Alice"));
		almostFullTournament.addPlayer(new Defector("Bob"));
		almostFullTournament.addPlayer(new Reciprocator("Charles"));
		this.tournaments.add(almostFullTournament);
		
	}
	
	public void addTournament(Tournament tournament) {
		this.tournaments.add(tournament);
	}
	
	public ArrayList<Tournament> getTournamentsByStatus(Boolean status){
		ArrayList<Tournament> curr = new ArrayList<Tournament>();
		for(Tournament tour: this.tournaments) {
			if(tour.isOpen() == status)
			{
				curr.add(tour);
			}
		}
		return curr;
	}

	@Autowired
	private ServletWebServerApplicationContext serverContext;
	
	@Bean
    public ApplicationListener<ServletWebServerInitializedEvent> serverPortListenerBean() {
        return event -> {
            System.out.println("### TOURNAMENT SERVER STARTING ON PORT " + event.getWebServer().getPort() + " ###");
        };
    }
	
	
	@GetMapping("/openTournaments")
    public String[] getOpenTournaments() {
        return this.getTournamentsByStatus(true).stream()
                .map(Tournament::getName)
                .toArray(String[]::new);
    }
	
	@GetMapping("/closedTournaments")
    public String[] getClosedTournaments() {
        return this.getTournamentsByStatus(false).stream()
                .map(Tournament::getName)
                .toArray(String[]::new);
    }
	
	
	public Tournament getTournament(String name) {
		for(Tournament tour : this.tournaments) {
			if(tour.getName().equals(name)) {
				return tour;
			}
		}
		return null;
	}
	
	@PostMapping("/spectate")
	public Boolean spectate(@RequestBody SpectateRequest req) {
		Tournament target = this.getTournament(req.tournamentName());
		if(target == null) {
			return false;
		}
		
		target.addListener(new RemoteListener(req.ip(), req.port()));
		return true;
	}
	
	
	@PostMapping("/register")
	public Boolean register(@RequestBody RegistrationRequest req) {
		Tournament target = this.getTournament(req.tournamentName());
		if(target == null) {
			return false;
		}
				
		boolean result = target.addPlayer(new RemoteBot(req.robotName(), req.ip(), req.port()));
				
		return result;
	} 
	
	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/run/{tournamentName}")
	public String run(@PathVariable String tournamentName) {
		
		// Fire and forget
	    CompletableFuture.runAsync(() -> this.getTournament(tournamentName).run());

	    return "Tournament started: " + tournamentName; 
	}
	
	
	
}

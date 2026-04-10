package networking.server;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.server.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import games.Game;
import games.PrisonerDilemma;
import models.RegistrationRequest;
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
		this.tournaments.add(new RoundRobin("RoundRobin PD", pd));
	}
	
	public void addTournament(Tournament tournament) {
		this.tournaments.add(tournament);
	}
	
	public ArrayList<Tournament> getActiveTournaments(){
		ArrayList<Tournament> curr = new ArrayList<Tournament>();
		for(Tournament tour: this.tournaments) {
			if(tour.isOpen() == true)
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
	
	
	@GetMapping("/tournaments")
    public String[] getTournaments() {
        return tournaments.stream()
                .map(Tournament::getName)
                .toArray(String[]::new);
    }
	
	
	@PostMapping("/register")
	public Boolean register(@RequestBody RegistrationRequest req) {
		Tournament target = null;
		for(Tournament tour : this.tournaments) {
			if(tour.getName().equals(req.tournamentName())) {
				target = tour;
				break;
			}
		}

		if(target == null) {
			return false;
		}
		
		Robot bot = new RemoteBot(req.robotName(), req.ip(), req.port());
		
		boolean result = target.addPlayer(bot);
				
		return result;
	}
	
	
	
}

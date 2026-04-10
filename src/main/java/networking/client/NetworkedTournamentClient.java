package networking.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.server.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import models.MatchDetails;
import robots.Cooperator;
import robots.Robot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RestController
public class NetworkedTournamentClient extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		new SpringApplicationBuilder(NetworkedTournamentClient.class)
		.profiles("random")
		.run(args);
	}
	
	private Robot bot;
	int assignedPort;
	String assignedIP;
	String serverUrl = "http://localhost:8080";

	@Autowired
	private ServletWebServerApplicationContext serverContext;
	
	public NetworkedTournamentClient() {
		this.bot = new Cooperator("Jeff");
	}
	
	public NetworkedTournamentClient(Robot bot) {
		this.bot = bot;
	}
	
	public void setBot(Robot bot) {
		this.bot = bot;
	}
	

	public int getAssignedPort() {
		return assignedPort;
	}

	public String getAssignedIP() {
		return assignedIP;
	}

	@Bean
    public ApplicationListener<ServletWebServerInitializedEvent> serverPortListenerBean() {
        return event -> {
            this.assignedPort = event.getWebServer().getPort();
            System.out.println("Port is "+this.assignedPort);
            
            try {
				this.assignedIP = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        };
    }
	
	@RequestMapping("/action")
	@ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
	public String getAction(@RequestBody MatchDetails details) {
		return this.bot.getAction(details.opponentName(), details.history());
	}
	
	
	
}

package networking.client.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import models.History;
import robots.Robot;

@Service
public class ClientService {
	
	Robot bot;

	public Robot getBot() {
		return bot;
	}

	public void setBot(Robot bot) {
		this.bot = bot;
	}

	public ClientService(Robot bot) {
		// TODO Auto-generated constructor stub
		this.bot = bot;
	}
	
	public String getAction(String opponentName, ArrayList<History> history) {
		String action = this.bot.getAction(opponentName, history);
		return action;
	}
	
	

}

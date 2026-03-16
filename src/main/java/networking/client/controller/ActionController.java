package networking.client.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import models.MatchDetails;
import networking.client.service.ClientService;

@RestController
@RequestMapping("/action")
public class ActionController {
	
	ClientService clientService;

	public ActionController(ClientService clientService) {
		super();
		this.clientService = clientService;
	}
	
	@ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
	public String getAction(@RequestBody MatchDetails details) {
		String action = this.clientService.getAction(details.opponentName(), details.history());
		return action;
	}

}

package com.elearning.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.elearning.model.ChatMessage;
import com.elearning.model.GameInteractCode;
import com.elearning.model.GameInteractRequest;
import com.elearning.model.GameRequest;
import com.elearning.model.GameRequestResponse;
import com.elearning.model.Gamer;
import com.elearning.util.Util;

@Component
public class MessageListener {

	private SimpMessagingTemplate template;
	
	@Autowired
	Util util;
	
	@Autowired
	public MessageListener(SimpMessagingTemplate template) {
		this.template = template;
	}
	
	// flexible like requestHandler (see javadoc)
	@KafkaListener(id="main-listener", topics="kafka-chat")
	public void listen(String message) {
		ChatMessage messageTmp = util.jsonToObject(message, ChatMessage.class);
		template.convertAndSend("/chat/" + messageTmp.getRoom(), messageTmp);
	}

	// flexible like requestHandler (see javadoc)
	@KafkaListener(id="unread-listener",topics="kafka-unread")
	public void listenUnread(String json) {
		ChatMessage message = util.jsonToObject(json, ChatMessage.class);
		template.convertAndSend("/chat/" + message.getUserKey(), message);
	}

	@KafkaListener(id="game-request-listener",topics="kafka-game")
	public void listenGameRequest(String json) {
		GameRequest request = util.jsonToObject(json, GameRequest.class);
		for (Gamer gamer : request.getGamers()) {
			GameRequestResponse response = new GameRequestResponse(request.getHostName(), request.getGameRoomKey());
			template.convertAndSend("/game/request/" + gamer.getUserKey(),response);
		}
		template.convertAndSend("/game/request/" + request.getGameRoomKey(),request);
	}
	
	@KafkaListener(id="game-interact-listener",topics="kafka-game-interact")
	public void listenGameInteractRequest(String json) {
		GameInteractRequest request = util.jsonToObject(json, GameInteractRequest.class);
		if (request.getCode().equals(GameInteractCode.START)
				|| request.getCode().equals(GameInteractCode.ROUNDSTOP)) {
			template.convertAndSend("/game/interact/" + request.getBody(),request.getCode().name());
		}
		
	}
}

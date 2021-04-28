package com.elearning.listener;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.elearning.model.ChatMessage;

import kafka.message.Message;

@Component
public class MessageListener {

	private SimpMessagingTemplate template;

	@Autowired
	public MessageListener(SimpMessagingTemplate template) {
		this.template = template;
	}
	
	// flexible like requestHandler (see javadoc)
	@KafkaListener(id="main-listener", topics="kafka-chat")
	public void listen(ChatMessage message) {
		template.convertAndSend("/chat/" + message.getRoom(), message);
	}

	// flexible like requestHandler (see javadoc)
	@KafkaListener(id="unread-listener",topics="kafka-unread")
	public void listenUnread(ChatMessage message) {
		template.convertAndSend("/chat/" + message.getUserKey(), message);
	}
}

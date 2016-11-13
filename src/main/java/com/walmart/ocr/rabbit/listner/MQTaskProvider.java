package com.walmart.ocr.rabbit.listner;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

public class MQTaskProvider {

	@Autowired
	private AmqpTemplate template;

	public String getUserTask() {

		Message message = template.receive();
		String smartId = null;
		if (message != null) {
			smartId = new String(message.getBody());
		} 
		return smartId;
	}
}

/**
 * 
 */
package com.walmart.ocr.rabbit.provider;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Rahul
 * 
 */
public class RabbitMQProvider {

	@Autowired
	private AmqpTemplate template;

	public AmqpTemplate getTemplate() {
		return template;
	}

	public void setTemplate(AmqpTemplate template) {
		this.template = template;
	}
	
	public void sendMessage(String message) {
		System.out.println();
		template.convertAndSend(message);
		
		System.out.println();
	}

}

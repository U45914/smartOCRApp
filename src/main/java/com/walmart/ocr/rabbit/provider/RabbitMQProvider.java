/**
 * 
 */
package com.walmart.ocr.rabbit.provider;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author Rahul
 * 
 */
public class RabbitMQProvider {
	
	Logger LOGGER = Logger.getLogger(RabbitMQProvider.class);

	@Autowired
	private AmqpTemplate template;
	
	@Autowired
	private AmqpAdmin ocrQueueAdmin;
	
	private String queueName;

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public void setOcrQueueAdmin(AmqpAdmin ocrQueueAdmin) {
		this.ocrQueueAdmin = ocrQueueAdmin;
	}

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
	
	public void purgeMessages() {
		
		try{
			ocrQueueAdmin.purgeQueue(queueName, false);
		}catch(Exception e){
			LOGGER.error("Error occured while purging queue ", e);
		}
		
	}

}

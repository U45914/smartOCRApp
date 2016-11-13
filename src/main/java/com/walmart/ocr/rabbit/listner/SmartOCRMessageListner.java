/**
 * 
 */
package com.walmart.ocr.rabbit.listner;

import org.springframework.amqp.core.Message;

/**
 * @author Rahul
 *
 */
public class SmartOCRMessageListner/* implements MessageListener */{

	//@Override
	public void onMessage(Message message) {
		System.out.println("******************************************************************************");
		System.out.println("******************************************************************************");
		System.out.println("**********************Message Received from RabbitMQ Server*******************");
		System.out.println("******************************************************************************");
		System.out.println("Received from Exchage : " + message.getMessageProperties().getReceivedExchange());
		System.out.println("Message : " + message.toString());
		System.out.println("******************************************************************************");
		System.out.println("******************************************************************************");
		System.out.println("******************************************************************************");
		System.out.println("******************************************************************************");
	}

}

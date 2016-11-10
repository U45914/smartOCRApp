/**
 * 
 */
package com.walmart.ocr.rabbit.listner;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * @author Rahul
 *
 */
public class SmartOCRMessageListner implements MessageListener {

	@Override
	public void onMessage(Message message) {
		System.out.println(message.toString());
	}

}

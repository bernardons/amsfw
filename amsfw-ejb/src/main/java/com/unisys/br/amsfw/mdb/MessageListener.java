package com.unisys.br.amsfw.mdb;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * Utilizado caso seja necessário fila de mensagens para processamento.
 * 
 * @author DelfimSM
 *
 */
//@MessageDriven(activationConfig = {
//		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
//		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/queue/MyQueue"),
//		@ActivationConfigProperty(propertyName = "reconnectAttempts", propertyValue = "60"),
//		@ActivationConfigProperty(propertyName = "reconnectInterval", propertyValue = "10") })
public class MessageListener implements javax.jms.MessageListener {

	@Override
	public void onMessage(Message arg0) {
		TextMessage message = (TextMessage) arg0;
		try {
			System.out.println("Mensagem recebida: " + message.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}

package com.pg.mail;


import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMail {

	public static void main(String [] args){  
		String to = "xxxxxxxxx@gmail.com";
		String subject = "Registration";
		final String from ="xxxxxxx@gmail.com";
		final  String password ="9156405251";
		String msg ="Hello Xxxx Kumar,\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"Greetings for the day..!! \r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"Your Account is created by RI HIS Application Administrator. You can access the application using below details.\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"Application : www.google.com \r\n" + 
				"Username : Xxxx \r\n" + 
				"Password : Kumar@123 \r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"For any questions please contact Administrator using +91-xxxxxxxxxx.\r\n" + 
				"\r\n" + 
				"Thanks & Regards,\r\n" + 
				"HIS Team.";

		try{
			Properties props = new Properties();  
			props.setProperty("mail.transport.protocol", "smtp");     
			props.setProperty("mail.host", "smtp.gmail.com");  
			props.put("mail.smtp.auth", "true");  
			props.put("mail.smtp.port", "465");  
			props.put("mail.debug", "true");  
			props.put("mail.smtp.socketFactory.port", "465");  
			props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
			props.put("mail.smtp.socketFactory.fallback", "false");  
			Session session = Session.getDefaultInstance(props,  
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {  
					return new PasswordAuthentication(from,password);  
				}  
			});  

			//session.setDebug(true);  
			Transport transport = session.getTransport();  
			InternetAddress addressFrom = new InternetAddress(from);  

			MimeMessage message = new MimeMessage(session);  
			message.setSender(addressFrom);  
			message.setSubject(subject);  
			message.setContent(msg, "text/plain");  
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));  

			transport.connect();  
			Transport.send(message);  
			transport.close();
		}
		catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}
}


package com.pg.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/regurl")
public class RegistrationServlet extends HttpServlet {
	static SecretKeySpec secretKey;
	static byte[] key;
	private static final String INSERT_QUERY="INSERT INTO USERLIST(UNAME,PASS) VALUES(?,?)";
	public static String encrypt(String strToEncrypt, String secret)
	{
		try
		{
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		}
		catch (Exception e)
		{
			System.out.println("Error while encrypting: " + e.toString());
		}
		return null;
	}//encrypt


	public static String decrypt(String strToDecrypt, String secret)
	{
		try
		{
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		}
		catch (Exception e)
		{
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}//decrypt


	public static void setKey(String myKey)
	{

		MessageDigest sha = null;
		try {
			key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}//setKey


	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter pw=response.getWriter();
		response.setContentType("text/html");
		String result=null;
		System.out.println("RegistrationServlet.doPost()");
		pw.println("RegistrationServlet.doPost()");
		Connection con=null;
		PreparedStatement ps=null;
		String name=null,pass=null;
		try {
			//read form date
			name=request.getParameter("uname");
			pass=request.getParameter("pwd");
			System.out.println(name+"  "+pass);
			System.out.println("send mail");
			//sendMsg(name, pass);
			String encryptedData=encrypt(pass, "ghidfkm@447%$&");
			System.out.println(encryptedData);
			//String decryptedData=decrypt(encryptedData, "ghidfkm@447%$&");
			//System.out.println(decrypt(encrypt(pass, "ghidfkm@447%$&"), "ghidfkm@447%$&"));
			//load driver class name
			Class.forName("oracle.jdbc.driver.OracleDriver");
			//create connection
			con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "scott", "tiger");
			//create preparedStatement obj
			ps=con.prepareStatement(INSERT_QUERY);
			ps.setString(1, name);
			ps.setString(2, encryptedData);
			int i=ps.executeUpdate();
			if(i>0) {
				System.out.println("at last........");
				result=sendMsg(name, pass);
				pw.println(result);
			}
		}//try
		catch(Exception e) {
			e.printStackTrace();

		}
	}//dopost
	public static String sendMsg(String user,String Pass){  
		System.out.println("RegistrationServlet.sendMsg()");
		String to = "msoftcyber@gmail.com";
		String subject = "subject";
		final String from ="gaganpreetam5@gmail.com";
		final  String password ="9156405251";
		String msg ="Hello "+user+",\r\n" + 
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
				"Application : www.his.com \r\n" + 
				"Username : "+user+" \r\n" + 
				"Password : "+Pass+" \r\n" + 
				"\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"For any questions please contact Administrator using +91-9156405251.\r\n" + 
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
			System.out.println("Message sent Successfully........");
		}
		catch (MessagingException mex) {
			mex.printStackTrace();
		}
		return msg;
	}//sendMsg

public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doPost(request, response);
	System.out.println("RegistrationServlet.doGet()");
}//doGet
}


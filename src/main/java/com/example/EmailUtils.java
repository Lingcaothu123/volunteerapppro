package com.example;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailUtils {

    public static void sendEmail(String toEmail, String subject, String messageText) throws MessagingException {
        final String fromEmail = "aiqingyinggaideyangzi@gmail.com"; 
        final String password = "ewqaigboqdmqwhkp";     

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); 
        props.put("mail.smtp.port", "587");              
        props.put("mail.smtp.auth", "true");             
        props.put("mail.smtp.starttls.enable", "true");  

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        Session session = Session.getInstance(props, auth);

        Message msg = new MimeMessage(session);
try {
    msg.setFrom(new InternetAddress(fromEmail, "Bookstore Support"));
} catch (java.io.UnsupportedEncodingException e) {
    msg.setFrom(new InternetAddress(fromEmail)); 
}
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        msg.setSubject(subject);
        msg.setText(messageText);

        Transport.send(msg);
    }
}

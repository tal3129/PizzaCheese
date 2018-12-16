package com.cheeze.pizza.pizzacheeze.MailSender;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



/**
 * Created by yuval on 22/01/2018.
 */
public class OutlookSender {
    private Properties properties;
    private String from, pass, host;


    public OutlookSender(String sender, String senderPass) {
        from = sender;
        pass = senderPass;

        host = "smtp.live.com";

        // Get system properties
        properties = System.getProperties();
        // Setup mail server
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.user", from);
        properties.put("mail.smtp.password", pass);
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "true");


    }

    public boolean sendMail(String subject, String body, String receiver) {
        Session session = Session.getInstance(properties);
        boolean success = true;
        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            if (receiver.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            message.setText(body);

            // Send message
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (MessagingException mex) {
            mex.printStackTrace();
            success = false;
        }
        return success;
    }
}
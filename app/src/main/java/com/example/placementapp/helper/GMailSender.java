package com.example.placementapp.helper;





import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

public class GMailSender {
    private String mailhost = "smtp.gmail.com";
    private String user;
    private String password;
    public static final String successResult = "Mail sent successfully.";



    public GMailSender(String user, String password) {
        this.user = user;
        this.password = password;


    }
    private Session getSession() {
        //Gmail Host
        String host = "smtp.gmail.com";
        //Enter your Gmail password
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", "586");
        prop.put("mail.smtp.ssl.trust", host);

        return Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
    }

    public synchronized void sendMail(String subject, String body, String sender, String recipient) throws Exception {
        Session session = getSession();
        MimeMessage message = new MimeMessage(session);
        // Sent From
        message.setFrom(new InternetAddress(user));
        // Sent To
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        // Set Subject
        message.setSubject(subject);
        // Send message
        Transport.send(message);
    }


}
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//Thanks :) https://www.digitalocean.com/community/tutorials/javamail-example-send-mail-in-java-smtp 
public class EmailSystem {
    public static final String EMAIL_SUCESS = "Email sent successfully";
    public static final String AUTH_ERROR = "Authentication Error";
    public static final String EMAIL_FAIL = "Email failed to send";
    public static final String NAME_ERROR = "Name Error";

    public static String sendEmail(String header, String body, String toEmail) {
        Properties props = new Properties();
		props.put("mail.smtp.host", AccountSystem.currentUser.stmpHost); //SMTP Host
		props.put("mail.smtp.port", AccountSystem.currentUser.tlsPort); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");

        //create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(AccountSystem.currentUser.messageEmail, AccountSystem.currentUser.messageEmailPass);
			}
		};
        Session session = Session.getInstance(props, auth);

        try {
            MimeMessage emailContent = new MimeMessage(session); 

            //set message headers
            emailContent.addHeader("Content-type", "text/HTML; charset=UTF-8");
            emailContent.addHeader("format", "flowed");
            emailContent.addHeader("Content-Transfer-Encoding", "8bit");
    
            emailContent.setFrom(new InternetAddress(AccountSystem.currentUser.messageEmail, AccountSystem.currentUser.firstName + " " + AccountSystem.currentUser.lastName));
    
            emailContent.setReplyTo(InternetAddress.parse(AccountSystem.currentUser.messageEmail, false));
    
            emailContent.setSubject(header, "UTF-8");
    
            emailContent.setText(body, "UTF-8");
    
            emailContent.setSentDate(new Date());

            emailContent.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            Transport.send(emailContent);
            return EMAIL_SUCESS;

        } catch (SendFailedException sendEx) {
            return EMAIL_FAIL;
        } catch (MessagingException msgEx) {
            msgEx.printStackTrace();
            return AUTH_ERROR;
        } catch (UnsupportedEncodingException e) {
            return NAME_ERROR;
        }
    }

    public static String sendMockEmail() {
        return AccountSystem.currentUser.stmpHost;
    }
}

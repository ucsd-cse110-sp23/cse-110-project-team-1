import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import org.bson.Document;

import com.mongodb.internal.connection.tlschannel.TlsChannel;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//FIX BUT UPDATE FIRST
//Thanks :) https://www.digitalocean.com/community/tutorials/javamail-example-send-mail-in-java-smtp 
public class EmailSystem {
    public static final String EMAIL_SUCESS = "Email sent successfully";
    public static final String AUTH_ERROR = "Authentication Error";
    public static final String EMAIL_FAIL = "Email failed to send";
    public static final String NAME_ERROR = "Name Error";

    //Email field Strings
    public static final String FIRSTNAME = "FIRST_NAME";
    public static final String LASTNAME = "LAST_NAME";
    public static final String DISPLAYNAME = "DISPLAY_NAME";
    public static final String MESSAGE_EMAIL = "Message_Email";
    public static final String STMP = "STMP";
    public static final String TLS = "TLS";
    public static final String MESSAGE_PASS = "Message_Pass";
    public static final String EMAIL_EXISTS = "email_exists";

    /**
     * @require account has setup its email
     * @param username
     * @param password
     * @param header
     * @param body
     * @param toEmail
     * @return
     */
    public static String sendEmail(String username, String password, String header, String body, String toEmail) {
        Document currentUser = AccountSystem.getAccount(username, password);

        String firstName = (String) currentUser.get(FIRSTNAME);
        String lastName = (String) currentUser.get(LASTNAME);
        String displayName = (String) currentUser.get(DISPLAYNAME);
        String messageEmail = (String) currentUser.get(MESSAGE_EMAIL);
        String smtp = (String) currentUser.get(STMP);
        String tls = (String) currentUser.get(TLS);
        String messagePass = (String) currentUser.get(MESSAGE_PASS);

        Properties props = new Properties();
		props.put("mail.smtp.host", (String) smtp); //SMTP Host
		props.put("mail.smtp.port", (String) tls); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");

        //create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(messageEmail, messagePass);
			}
		};
        Session session = Session.getInstance(props, auth);

        try {
            MimeMessage emailContent = new MimeMessage(session); 

            //set message headers
            emailContent.addHeader("Content-type", "text/HTML; charset=UTF-8");
            emailContent.addHeader("format", "flowed");
            emailContent.addHeader("Content-Transfer-Encoding", "8bit");
    
            emailContent.setFrom(new InternetAddress(messageEmail, firstName + " " + lastName));
    
            emailContent.setReplyTo(InternetAddress.parse(messageEmail, false));
    
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

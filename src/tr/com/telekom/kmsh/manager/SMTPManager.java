package tr.com.telekom.kmsh.manager;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import tr.com.telekom.kmsh.config.MailConfig;
import tr.com.telekom.kmsh.util.KmshLogger;

import com.sun.mail.smtp.SMTPTransport;

public class SMTPManager {

	public static String sendMail(MailConfig conf, String subject,
			String content) {
		String response = null;
		KmshLogger.log(0, "Sending mail");
		InternetAddress[] addressTo = new InternetAddress[conf.receipents
				.size()];
		for (int i = 0; i < conf.receipents.size(); i++) {
			try {
				addressTo[i] = new InternetAddress(conf.receipents.get(i));
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Properties props = System.getProperties();
		props.put("mail.smtps.host", "smtp.gmail.com");
		props.put("mail.smtps.auth", "true");
		Session session = Session.getInstance(props, null);
		MimeMessage msg = new MimeMessage(session);

		try {
			msg.setFrom(new InternetAddress(conf.sender));
			msg.setRecipients(Message.RecipientType.CC, addressTo);

			msg.setContent(msg, "text/html;charset=UTF-8");
			msg.setHeader("Content-Type", "text/html;charset=UTF-8");
			msg.setSubject(subject, "UTF-8");
			msg.setText(content, "UTF-8");

			SMTPTransport t = (SMTPTransport) session.getTransport("smtps");
			t.connect("smtp.gmail.com", conf.sender, conf.password);

			t.sendMessage(msg, msg.getAllRecipients());

			response = t.getLastServerResponse();
			t.close();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}
}

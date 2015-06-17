package tr.com.telekom.kmsh.manager;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.KmshLogger;

import java.util.ArrayList;
import java.util.List;

public class TwilioConnector {
	// Find your Account Sid and Token at twilio.com/user/account

	private static String ACCOUNT_SID;
	private static String AUTH_TOKEN;

	static {
		ConfigReader conf = ConfigReader.getInstance();
		ACCOUNT_SID = conf.getProperty("ACCOUNT_SID");
		AUTH_TOKEN = conf.getProperty("AUTH_TOKEN");
	}

	public static void sendSMS(String content, String smsNo)
			throws TwilioRestException {
		KmshLogger.log(1, "Sendig SMS");
		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

		// Build a filter for the MessageList
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Body", content));
		params.add(new BasicNameValuePair("To", smsNo));
		params.add(new BasicNameValuePair("From", "+12405132106"));

		MessageFactory messageFactory = client.getAccount().getMessageFactory();
		Message message = messageFactory.create(params);
		KmshLogger.log(0, message.getSid());
	}
}

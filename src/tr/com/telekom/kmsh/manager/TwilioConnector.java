package tr.com.telekom.kmsh.manager;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import tr.com.telekom.kmsh.util.KmshLogger;

import java.util.ArrayList;
import java.util.List;

public class TwilioConnector {
	// Find your Account Sid and Token at twilio.com/user/account
	public static final String ACCOUNT_SID = "ACa105df99a0908a2c593dd91123fbd4b1";
	public static final String AUTH_TOKEN = "690c578ec253b596d8221b7044947ed4";

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

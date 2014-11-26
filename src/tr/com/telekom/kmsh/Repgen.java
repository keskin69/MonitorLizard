package tr.com.telekom.kmsh;

import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.config.MailConfig;
import tr.com.telekom.kmsh.config.ReportConfig;
import tr.com.telekom.kmsh.config.SMSConfig;
import tr.com.telekom.kmsh.manager.ReportManager;
import tr.com.telekom.kmsh.manager.SMSManager;
import tr.com.telekom.kmsh.manager.SMTPManager;
import tr.com.telekom.kmsh.util.KmshUtil;
import tr.com.telekom.kmsh.util.KmshLogger;

import com.twilio.sdk.TwilioRestException;

public class Repgen {
	public Repgen(XMLManager conf, String repName) {
		String content = null;

		for (ReportConfig repConf : conf.reportList) {
			if (repConf.name.equals(repName)) {
				ReportManager report = new ReportManager(repConf);
				KmshLogger.log(1, "Processing Report " + repName);
				boolean condition = report.process(conf);
				content = report.getContent();

				if (condition) {
					KmshLogger.log(1, "Sending notifications ");
					// send mail
					sendMail(conf, repConf, content);

					// send SMS
					sendSMS(conf, repConf, content);
				}

				// write content to report log
				KmshUtil.writeLog(repConf.name + ".log", content);
				KmshLogger.log(0, content);
			}
		}

		if (content == null) {
			KmshLogger.log(0, "Cannot find report definition for " + repName);
		}
	}

	private void sendMail(XMLManager conf, ReportConfig repConf, String content) {
		// Send content to SMS
		if (repConf.useMail != null) {
			MailConfig mailConf = conf.findMailConfig(repConf.useMail);

			if (mailConf != null) {
				String response = SMTPManager.sendMail(mailConf, repConf.title,
						content);

				KmshLogger.log(0, "SMTP Response: " + response);
			}
		}
	}

	private void sendSMS(XMLManager conf, ReportConfig repConf, String content) {
		// Send content to SMS
		if (repConf.useSms != null) {
			SMSConfig sms = conf.findSMS(repConf.useSms);

			if (sms != null) {
				try {
					for (String number : sms.number) {
						SMSManager.sendSMS(content, number);
					}
				} catch (TwilioRestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}

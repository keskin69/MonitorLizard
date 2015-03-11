package tr.com.telekom.kmsh;

import net.gpedro.integrations.slack.SlackApi;
import net.gpedro.integrations.slack.SlackMessage;
import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.config.MailConfig;
import tr.com.telekom.kmsh.config.ReportConfig;
import tr.com.telekom.kmsh.config.SMSConfig;
import tr.com.telekom.kmsh.manager.ReportManager;
import tr.com.telekom.kmsh.manager.TwilioConnector;
import tr.com.telekom.kmsh.manager.SMTPManager;
import tr.com.telekom.kmsh.util.ConfigReader;
import tr.com.telekom.kmsh.util.KmshUtil;
import tr.com.telekom.kmsh.util.KmshLogger;

import com.twilio.sdk.TwilioRestException;

public class Repgen {
	public Repgen(XMLManager conf, String repName) {
		boolean found = false;

		if (repName.equals("")) {
			found = true;

			// execute all reports
			for (ReportConfig repConf : conf.reportList) {
				if (repConf.canExecute(repConf.runAt)) {
					singleReport(conf, repConf);
				}
			}
		} else {
			// execute single report
			for (ReportConfig repConf : conf.reportList) {
				if (repConf.id.equals(repName)) {
					singleReport(conf, repConf);
					found = true;
				}
			}
		}

		if (!found) {
			KmshLogger.log(3, "Cannot find report definition for " + repName);
		}
	}

	private void singleReport(XMLManager conf, ReportConfig repConf) {
		String content = null;

		ReportManager report = new ReportManager(repConf);
		KmshLogger.log(1, "Processing Report " + repConf.id);
		boolean condition = report.process(conf);
		content = report.getContent();

		if (condition) {
			KmshLogger.log(0, "Sending notifications ");
			// send mail
			sendMail(conf, repConf, content);

			// send SMS
			sendSMS(conf, repConf, content);
		}

		// write content to report log
		KmshUtil.writeLog(repConf.id + ".log", content);
		KmshLogger.log(0, content);
	}

	private void sendMail(XMLManager conf, ReportConfig repConf, String content) {
		// Send content to SMS
		if (repConf.useMail != null) {
			MailConfig mailConf = conf.findMailConfig(repConf.useMail);

			if (mailConf != null) {
				String response = SMTPManager.sendMail(mailConf, repConf.name,
						content);

				KmshLogger.log(0, "SMTP Response: " + response);
			}

			String slackUrl = ConfigReader.getInstance()
					.getProperty("slackURL");
			String slackGroup = ConfigReader.getInstance().getProperty(
					"slackGroup");
			KmshLogger.log(1, "Sending message to slack group " + slackGroup);

			SlackApi api = new SlackApi(slackUrl);
			api.call(new SlackMessage(slackGroup, null, content));
		}
	}

	private void sendSMS(XMLManager conf, ReportConfig repConf, String content) {
		// Send content to SMS
		if (repConf.useSms != null) {
			SMSConfig sms = conf.findSMS(repConf.useSms);

			if (sms != null) {
				try {
					for (String number : sms.number) {
						TwilioConnector.sendSMS(content, number);
					}
				} catch (TwilioRestException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}

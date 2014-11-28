package tr.com.telekom.kmsh;

import tr.com.telekom.kmsh.config.XMLManager;
import tr.com.telekom.kmsh.config.MailConfig;
import tr.com.telekom.kmsh.config.ReportConfig;
import tr.com.telekom.kmsh.config.SMSConfig;
import tr.com.telekom.kmsh.manager.ReportManager;
import tr.com.telekom.kmsh.manager.SMSManager;
import tr.com.telekom.kmsh.manager.SMTPManager;
import tr.com.telekom.kmsh.util.H2Util;
import tr.com.telekom.kmsh.util.KmshUtil;
import tr.com.telekom.kmsh.util.KmshLogger;

import com.twilio.sdk.TwilioRestException;

public class Repgen {
	public Repgen(XMLManager conf, String repName) {
		boolean found = false;

		if (repName.equals("")) {
			// execute all reports
			for (ReportConfig repConf : conf.reportList) {
				if (repConf.period != -1) {
					if (H2Util.getAge(repConf.id) >= repConf.period) {
						singleReport(conf, repConf);
						found = true;
					} else {
						KmshLogger.log(2, "Skipping report" + repConf.id);
					}
				} else {
					KmshLogger.log(2, "Ignoring report" + repConf.id);
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
			KmshLogger.log(0, "Cannot find report definition for " + repName);
		}
	}

	private void singleReport(XMLManager conf, ReportConfig repConf) {
		String content = null;

		ReportManager report = new ReportManager(repConf);
		KmshLogger.log(1, "Processing Report " + repConf.id);
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

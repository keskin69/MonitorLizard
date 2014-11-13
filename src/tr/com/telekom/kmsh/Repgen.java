package tr.com.telekom.kmsh;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import tr.com.telekom.kmsh.config.ConfigManager;
import tr.com.telekom.kmsh.config.MailConfig;
import tr.com.telekom.kmsh.config.ReportConfig;
import tr.com.telekom.kmsh.config.SMSConfig;
import tr.com.telekom.kmsh.manager.ReportManager;
import tr.com.telekom.kmsh.manager.SMSManager;
import tr.com.telekom.kmsh.manager.SMTPManager;

import com.twilio.sdk.TwilioRestException;

public class Repgen {
	public Repgen(ConfigManager conf, String repName) {
		String content = null;

		for (ReportConfig repConf : conf.reportList) {
			if (repConf.name.equals(repName)) {
				ReportManager report = new ReportManager(repConf);

				boolean condition = report.process(conf);
				content = report.getContent();

				if (condition) {
					// send mail
					sendMail(conf, repConf, content);

					// send SMS
					sendSMS(conf, repConf, content);
				}

				// write content to report log
				writeLog("log/" + repConf.name + ".log", content);

				System.out.println(content);
			}
		}

		if (content == null) {
			System.out.println("Cannot find report definition for " + repName);
		}
	}

	private void sendMail(ConfigManager conf, ReportConfig repConf,
			String content) {
		// Send content to SMS
		if (repConf.useMail != null) {
			MailConfig mailConf = conf.findMailConfig(repConf.useMail);

			if (mailConf != null) {
				String response = SMTPManager.sendMail(mailConf, repConf.title,
						content);

				System.out.println("SMTP Response: " + response);
			}
		}
	}

	private void sendSMS(ConfigManager conf, ReportConfig repConf,
			String content) {
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

	private void writeLog(String logFile, String content) {
		File file = new File(logFile);

		try {
			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(content);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}

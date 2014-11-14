package tr.com.telekom.kmsh.manager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.util.KmshLogger;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;

public class SSHManager {
	public static String executeCommand(ConnectionConfig con, String cmdStr) {
		String output = null;
		KmshLogger.log("Executing ssh >" + cmdStr);

		SSHClient client = new SSHClient();
		try {
			client.loadKnownHosts();
			client.connect(con.host);
			client.authPassword(con.user, con.password);
			final Session session = client.startSession();
			try {
				final Command cmd = session.exec(cmdStr);
				output = IOUtils.readFully(cmd.getInputStream()).toString();

				cmd.join(1, TimeUnit.SECONDS);
			} finally {
				session.close();
			}

			client.disconnect();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		KmshLogger.log("Executing ssh completed");
		return output;
	}
}

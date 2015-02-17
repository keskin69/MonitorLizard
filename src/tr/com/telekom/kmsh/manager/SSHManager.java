package tr.com.telekom.kmsh.manager;

import java.io.IOException;
import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

import tr.com.telekom.kmsh.config.ConnectionConfig;
import tr.com.telekom.kmsh.util.KmshLogger;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.HostKeyVerifier;

public class SSHManager {
	public static String executeCommand(ConnectionConfig con, String cmdStr) {
		String output = "";
		KmshLogger.log(1, "Executing ssh> " + cmdStr);

		SSHClient client = new SSHClient();
		try {
			// client.setConnectTimeout(10000);

			client.addHostKeyVerifier(new HostKeyVerifier() {
				public boolean verify(String arg0, int arg1, PublicKey arg2) {
					return true; // don't bother verifying
				}
			});

			client.loadKnownHosts();

			client.connect(con.host);
			client.authPassword(con.user, con.password);
			final Session session = client.startSession();
			try {
				final Command cmd = session.exec(cmdStr);
				output = IOUtils.readFully(cmd.getInputStream()).toString();

				cmd.join(10, TimeUnit.SECONDS);
			} finally {
				session.close();
			}

			client.disconnect();

		} catch (IOException ex) {
			KmshLogger.log(4, "Cannot execute remote SSH Command");
			ex.printStackTrace();
		}

		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		KmshLogger.log(1, "Executing ssh completed");
		return output;
	}
}

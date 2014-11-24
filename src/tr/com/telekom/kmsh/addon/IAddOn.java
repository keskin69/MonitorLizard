package tr.com.telekom.kmsh.addon;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IAddOn {
	public void process();

	public void processRow(ResultSet rs) throws SQLException;
}

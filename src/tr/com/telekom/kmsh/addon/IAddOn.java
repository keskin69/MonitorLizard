package tr.com.telekom.kmsh.addon;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IAddOn {
	void process(String cmdId);

	void processRow(ResultSet rs) throws SQLException;
}

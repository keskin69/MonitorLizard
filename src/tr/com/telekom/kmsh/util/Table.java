package tr.com.telekom.kmsh.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class Table extends ArrayList<ArrayList> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2526374518280597095L;

	public Table(ResultSet rs) {
		ResultSetMetaData rsmd = null;
		try {
			rsmd = rs.getMetaData();

			int columnsNumber = rsmd.getColumnCount();

			// header
			ArrayList<String> row = new ArrayList<String>();
			for (int i = 1; i <= columnsNumber; i++) {
				row.add(rsmd.getColumnName(i));
			}

			add(row);

			while (rs.next()) {
				row = new ArrayList<String>();
				for (int i = 1; i <= columnsNumber; i++) {
					String d = rs.getString(i);
					d = d.trim();
					row.add(d);
				}

				add(row);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

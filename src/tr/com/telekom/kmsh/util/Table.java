package tr.com.telekom.kmsh.util;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class Table extends ArrayList<ArrayList>  implements Serializable {
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

	public String getString() {
		String output = "";

		for (int i = 0; i < size(); i++) {
			@SuppressWarnings("unchecked")
			ArrayList<String> row = get(i);

			for (int j = 0; j < row.size(); j++) {
				output += row.get(j) + ";";
			}

			output += "\n";
		}

		return output;
	}
}

package tr.com.telekom.kmsh.util;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class Table extends ArrayList<ArrayList> implements Serializable {
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

	public String getHTML(String tblId) {
		String out = "<TABLE id=\"" + tblId + "\" border=\"1\"><THEAD>";

		for (int i = 0; i < size(); i++) {
			@SuppressWarnings("unchecked")
			ArrayList<String> row = get(i);
			if (i == 1) {
				out += "</THEAD><TBODY>";
			}

			out += "<TR>";
			for (int j = 0; j < row.size(); j++) {

				if (i == 0) {
					out += "<TH>" + row.get(j) + "</TH>";
				} else {
					out += "<TD>" + row.get(j) + "</TD>";
				}
			}

			out += "</TR>\n";
		}

		out += "</TBODY></TABLE><BR>\n";

		return out;
	}
}

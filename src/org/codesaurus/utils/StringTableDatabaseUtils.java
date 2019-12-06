package org.codesaurus.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Simple utility to read a @link{java.sql.ResultSet} into a StringTable which can then be
 * exported to the various output formats
 */
public class StringTableDatabaseUtils {

    public static StringTable readStringTable(ResultSet rs) throws SQLException{

        StringTable resultsTable = null;
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        resultsTable = new StringTable(getHeadings(metadata, columnCount));

        if (rs.first()) {
            do {
                ArrayList<String> row = new ArrayList<String>();
                for (int i = 1; i < columnCount + 1; i++) {
                    row.add(rs.getString(i));
                }
                resultsTable.addRow(row.toArray());
                row.clear();
            } while (rs.next());
        }

        return resultsTable;
    }

    protected static String[] getHeadings(ResultSetMetaData metadata,
                                          int columnCount) throws SQLException {
        ArrayList<String> headersList = new ArrayList<String>();
        for (int i = 1; i < columnCount + 1; i++)
            headersList.add(metadata.getColumnName(i));
        return headersList.toArray(new String[headersList.size()]);
    }
}

package org.codesaurus.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Utilities to convert tabulated model into various output formats
 *
 * @since 01/2004
 * @author Simon Johnson <simon622 AT gmail.com>
 *
 * Contributors;
 * 2019 Matt h - improved ASCII output
 */
public class StringTableWriters {

    static final char PLUS = '+';
    static final char HYPHEN = '-';
    static final char UNDERSCORE = '_';
    static final char PIPE = '|';
    static final char WHITESPACE = ' ';
    static final String NEWLINE = System.lineSeparator();

    public static String writeStringTableAsCSV(StringTable st){
        CSVTextWriter textTable = new CSVTextWriter();
        return textTable.write(st);
    }

    public static String writeStringTableAsASCII(StringTable st){
        PrettyASCIITableWriter prettyTable = new PrettyASCIITableWriter();
        return prettyTable.write(st);
    }

    public static String writeStringTableAsHTML(StringTable st, boolean borders, int padding){
        HTMLTextWriter htmlTable = new HTMLTextWriter(borders, padding);
        return htmlTable.write(st);
    }

    private static class HTMLTextWriter {

        boolean borders = false;
        int padding;

        public HTMLTextWriter(boolean borders, int padding) {
            this.borders = borders;
            this.padding = padding;
        }

        public String write(StringTable table) {

            StringBuilder sb = new StringBuilder();

            sb.append("<table style=\"border: "+(borders ? 1 : 0)+"px solid black;\">");
            sb.append("<thead>");
            sb.append("<tr>");

            //-- headings
            List<String> headings = table.getHeadings();
            Iterator<String> itr = headings.iterator();
            while(itr.hasNext()){
                String heading = itr.next();
                sb.append("<th style=\"padding:"+padding+"px;border-bottom: 1px solid #ddd;\">");
                sb.append(heading.trim());
                sb.append("</th>");
            }

            sb.append("</tr>");
            sb.append("</thead>");

            //-- data
            List<String[]> data = table.getRows();
            if(data != null && !data.isEmpty()) {

                Iterator<String[]> dataItr = data.iterator();
                while(dataItr.hasNext()){
                    sb.append("<tr>");
                    String[] row = dataItr.next();
                    for (int i = 0; i < row.length; i++) {
                        sb.append("<td style=\"padding:"+padding+"px;border-bottom: 1px solid #ddd;\">");
                        sb.append(row[i].trim());
                        sb.append("</td>");
                    }

                    sb.append("</tr>");
                }
            }

            //-- rollup
            List<String> footers = table.getFooter();
            if(footers != null && !footers.isEmpty()) {
                Iterator<String> footerItr = footers.iterator();
                sb.append("<tr>");
                while(footerItr.hasNext()){
                    sb.append("<td>");
                    String footer = footerItr.next();
                    sb.append(footer.trim());
                    sb.append("</td>");
                }
                sb.append("</tr>");
            }

            sb.append("</table>");
            return sb.toString();
        }
    }

    private static class CSVTextWriter {

        static final char SEP = ',';
        static final String NEWLINE = System.lineSeparator();

        public String write(StringTable table) {
            return write(table, SEP);
        }

        public String write(StringTable table, char sep) {

            StringBuilder sb = new StringBuilder();

            //-- headings
            List<String> headings = table.getHeadings();
            Iterator<String> itr = headings.iterator();
            while(itr.hasNext()){
                String heading = itr.next();
                sb.append(heading.trim());
                if(itr.hasNext()) sb.append(sep);
            }

            //-- data
            List<String[]> data = table.getRows();
            if(data != null && !data.isEmpty()) {
                sb.append(NEWLINE);

                Iterator<String[]> dataItr = data.iterator();
                while(dataItr.hasNext()){
                    String[] row = dataItr.next();
                    for (int i = 0; i < row.length; i++) {
                        if(i > 0)  sb.append(sep);
                        sb.append(row[i].trim());
                    }

                    if(dataItr.hasNext()) sb.append(NEWLINE);
                }
            }

            //-- rollup
            List<String> footers = table.getFooter();
            if(footers != null && !footers.isEmpty()) {
                sb.append(NEWLINE);

                Iterator<String> footerItr = footers.iterator();
                while(footerItr.hasNext()){
                    String footer = footerItr.next();
                    sb.append(footer.trim());
                    if(footerItr.hasNext()) sb.append(sep);
                }
            }

            return sb.toString();
        }
    }

    private static class ASCIITableWriter {

        private StringTable table;
        private int startRowIndex;
        private int rowCount;
        private int[] columnWidths;
        private StringBuilder text;
        private static final char HEADER_SEPERATOR = '-';
        private int textPosition;

        public String write(StringTable table, int startRowIndex) {

            return write(table, startRowIndex, table.getRows().size());
        }

        public String write(StringTable table, int startRowIndex, int rowCount) {

            // Initiate.
            this.table = table;
            this.startRowIndex = startRowIndex;
            this.rowCount = rowCount;

            // Calculate column positions.
            columnWidths = getColumnWidths();

            // Write table.
            return writeTable();
        }

        private int[] getColumnWidths() {

            int size = StringTable.getColumnCount(table);
            int[] columnWidths = new int[size];

            for (int i = 0; i < size; i++) {
                columnWidths[i] = getColumnWidth(i);
            }

            return columnWidths;
        }

        private int getColumnWidth(int columnIndex) {
            int columnWidth = StringTable.getColumnName(table, columnIndex).length();
            for (int i = 0; i < rowCount; i++) {
                if(isSeparatorRow(i)) continue;
                int valueWidth = table.getCellValue(startRowIndex + i, columnIndex).length();
                if (valueWidth > columnWidth) {
                    columnWidth = valueWidth;
                }
            }
            if (table.getFooter() != null && table.getFooter().size() == StringTable.getColumnCount(table)) {
                String footer = table.getFooter().get(columnIndex);
                if (null != footer && footer.length() > columnWidth) {
                    columnWidth = footer.length();
                }
            }
            return columnWidth;
        }

        private String writeTable() {
            text = new StringBuilder();

            if(this.table.getTableName() != null){

                text.append(table.getTableName().toUpperCase());
                text.append(NEWLINE);
            }

            writeHeaderTop();

            writeColumnNames();
            writeHeaderSeperator(HEADER_SEPERATOR);
            for (int i = 0; i < rowCount; i++) {
                writeRow(startRowIndex + i);
            }
            writeHeaderSeperator(HEADER_SEPERATOR);

            if(table.getFooter() != null && table.getFooter().size() == StringTable.getColumnCount(table)){
                int size = StringTable.getColumnCount(table);
                for (int i = 0; i < size; i++) {
                    writeValue(table.getFooter().get(i), i);
                }
                writeLine();
                writeHeaderSeperator(HEADER_SEPERATOR);
            }

            return text.toString();
        }

        private void writeHeaderTop() {
            StringBuffer headBuff = new StringBuffer();
            for (Integer width : columnWidths) {
                for (int i = 0; i < width+2; i++) {
                    headBuff.append(UNDERSCORE);
                }
            }
            headBuff.replace(0, 1, " ");
            headBuff.replace(headBuff.length()-1, headBuff.length(), " ");
            text.append(headBuff.toString());
            writeLine();
        }

        private void writeColumnNames() {
            int size = StringTable.getColumnCount(table);
            for (int i = 0; i < size; i++) {
                writeValue(StringTable.getColumnName(table, i), i);
            }
            writeLine();
        }

        private void writeHeaderSeperator(char seperator) {
            int size = StringTable.getColumnCount(table);
            for (int i = 0; i < size; i++) {
                writeValue(getHeaderSeperator( (columnWidths[i]),seperator ), i);
            }
            writeLine();
        }

        private String getHeaderSeperator(int size, char seperator) {

            char[] ch = new char[size];
            for (int i = 0; i < size; i++) {
                ch[i] = seperator;
            }
            return new String(ch);
        }

        protected boolean isSeparatorRow(int rowIndex){
            return table.getRows().get(rowIndex) == null;
        }

        private void writeRow(int rowIndex) {

            //check to see if its a separator
            if(isSeparatorRow(rowIndex)){
                writeHeaderSeperator(HEADER_SEPERATOR);

            } else {
                int size = StringTable.getColumnCount(table);
                for (int i = 0; i < size; i++) {
                    writeValue(table.getCellValue(rowIndex, i), i);
                }
                writeLine();
            }

        }

        private void writeValue(String value, int columnIndex) {
            StringBuffer valuebuff = new StringBuffer("|").append(value);

            while(valuebuff.length() < columnWidths[columnIndex] + 1) {
                valuebuff.append(WHITESPACE);
            }

            valuebuff.append(PIPE);
            value = valuebuff.toString();

            text.append( value );
            textPosition = textPosition + value.length();
        }

        private void writeLine() {
            text.append(NEWLINE);
            textPosition = 0;
        }
    }

    private static class PrettyASCIITableWriter {

        private StringTable table;
        private int rowCount;
        private int columnCount;
        private int[] columnWidths;
        private StringBuilder text;

        private String write(StringTable table) {
            // Initialise
            this.table = table;
            this.rowCount = table.getRows().size();
            this.columnCount = StringTable.getColumnCount(table);

            // Calculate column positions.
            columnWidths = getColumnWidths();

            // Write table.
            return writeTable();
        }

        private int[] getColumnWidths() {
            int[] columnWidths = new int[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnWidths[i] = getColumnWidth(i);
            }
            return columnWidths;
        }

        private int getColumnWidth(int columnIndex) {
            int columnWidth = StringTable.getColumnName(table, columnIndex).length();
            for (int i = 0; i < rowCount; i++) {
                int valueWidth = table.getCellValue(i, columnIndex).length();
                columnWidth = Math.max(columnWidth, valueWidth);
            }
            if (null != table.getFooter() && table.getFooter().size() == columnCount) {
                String footer = table.getFooter().get(columnIndex);
                if (null != footer && footer.length() > columnWidth) {
                    columnWidth = footer.length();
                }
            }
            return columnWidth;
        }

        private String writeTable() {
            text = new StringBuilder();

            if (null != table.getTableName()) {
                text.append(table.getTableName().toUpperCase());
                writeLine();
            }

            writeSeparatorRow();
            writeColumnNames();
            writeSeparatorRow();
            for (int i = 0; i < rowCount; i++) {
                writeRow(i);
            }
            writeSeparatorRow();

            if (null != table.getFooter() && table.getFooter().size() == columnCount) {
                for (int i = 0; i < columnCount; i++) {
                    writeValue(table.getFooter().get(i), i);
                }
                writeLine();
                writeSeparatorRow();
            }

            return text.toString();
        }

        private void writeSeparatorRow() {
            StringBuilder sb = new StringBuilder();
            for (int width : columnWidths) {
                for (int i = 0; i < width + 4; i++) {
                    sb.append((i == 0 || i == width + 3) ? PLUS : HYPHEN);
                }
            }
            text.append(sb);
            writeLine();
        }

        private void writeColumnNames() {
            for (int i = 0; i < columnCount; i++) {
                writeValue(StringTable.getColumnName(table, i), i);
            }
            writeLine();
        }

        private void writeRow(int rowIndex) {
            for (int i = 0; i < columnCount; i++) {
                writeValue(table.getCellValue(rowIndex, i), i);
            }
            writeLine();
        }

        private void writeValue(String value, int columnIndex) {
            StringBuilder sb = new StringBuilder("| ").append(value);
            while (sb.length() < columnWidths[columnIndex] + 2) {
                sb.append(WHITESPACE);
            }
            sb.append(" |");
            text.append(sb);
        }

        private void writeLine() {
            text.append(NEWLINE);
        }
    }
}
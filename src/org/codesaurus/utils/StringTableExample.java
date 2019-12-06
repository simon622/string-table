package org.codesaurus.utils;

import java.util.*;
import java.util.function.Function;

/**
 * Simple main method to demonstrate example usage out to console
 */
public class StringTableExample {

    public static void main(String[] args){

        StringTable st = new StringTable("First Column", "Second Column", "Third Column");
        for (int i = 0; i < 10; i++) {
            st.addRow(i, i, i);
        }

        System.err.println(StringTableWriters.writeStringTableAsCSV(st));
        System.err.println(StringTableWriters.writeStringTableAsASCII(st));
        System.err.println(StringTableWriters.writeStringTableAsHTML(st, false, 2));

    }
}
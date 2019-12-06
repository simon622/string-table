# string-table
Java based framework to allow quick rendering of tabulated data in various output formats.

Great for debugging and quickly visualising data for logging or reporting.

Inbuilt support for the following export formats;
* ASCII Table
* CSV
* HTML Table

```java
  StringTable st = new StringTable("First Column", "Second Column", "Third Column");
  for (int i = 0; i < 10; i++) {
    st.addRow(i, i, i);
  }

  System.out.println(StringTableWriters.writeStringTableAsCSV(st));
  System.out.println(StringTableWriters.writeStringTableAsASCII(st));
  System.out.println(StringTableWriters.writeStringTableAsHTML(st, false, 2));
```

<pre>
+--------------++---------------++--------------+
| First Column || Second Column || Third Column |
+--------------++---------------++--------------+
| 0            || 0             || 0            |
| 1            || 1             || 1            |
| 2            || 2             || 2            |
| 3            || 3             || 3            |
| 4            || 4             || 4            |
| 5            || 5             || 5            |
| 6            || 6             || 6            |
| 7            || 7             || 7            |
| 8            || 8             || 8            |
| 9            || 9             || 9            |
+--------------++---------------++--------------+
</pre>

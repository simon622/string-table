# String-Table
Lightweight Java framework to allow quick rendering of tabulated data.
Great for debugging and quickly visualising data for logging or reporting. 
Supports automatic column size detection, advanced sort, rollup and mutliple output formats.

## Recent changes
* 2018 - Added support for ResultSet reading into StringTable
* 2019 - Added support for data sorting by Lexical and Numeric orders.
* 2019 - Added support for HTML output

Inbuilt support for the following export formats;
* ASCII
* CSV
* HTML

## Simple example

```java
  StringTable st = new StringTable("First Column", "Second Column", "Third Column");
  for (int i = 0; i < 10; i++) {
    st.addRow(i, i, i);
  }
  System.out.println(StringTableWriters.writeStringTableAsASCII(st));
```

## Database example

```java
  ResultSet rs = ...
  StringTable st = StringTableDatabaseUtils.readStringTable(rs);
  System.out.println(StringTableWriters.writeStringTableAsHTML(st));
```

### Export as ASCII
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

### Export as HTML
<table style="border: 0px solid black;"><thead><tr><th style="padding:2px;border-bottom: 1px solid #ddd;">First Column</th><th style="padding:2px;border-bottom: 1px solid #ddd;">Second Column</th><th style="padding:2px;border-bottom: 1px solid #ddd;">Third Column</th></tr></thead><tr><td style="padding:2px;border-bottom: 1px solid #ddd;">0</td><td style="padding:2px;border-bottom: 1px solid #ddd;">0</td><td style="padding:2px;border-bottom: 1px solid #ddd;">0</td></tr><tr><td style="padding:2px;border-bottom: 1px solid #ddd;">1</td><td style="padding:2px;border-bottom: 1px solid #ddd;">1</td><td style="padding:2px;border-bottom: 1px solid #ddd;">1</td></tr><tr><td style="padding:2px;border-bottom: 1px solid #ddd;">2</td><td style="padding:2px;border-bottom: 1px solid #ddd;">2</td><td style="padding:2px;border-bottom: 1px solid #ddd;">2</td></tr><tr><td style="padding:2px;border-bottom: 1px solid #ddd;">3</td><td style="padding:2px;border-bottom: 1px solid #ddd;">3</td><td style="padding:2px;border-bottom: 1px solid #ddd;">3</td></tr><tr><td style="padding:2px;border-bottom: 1px solid #ddd;">4</td><td style="padding:2px;border-bottom: 1px solid #ddd;">4</td><td style="padding:2px;border-bottom: 1px solid #ddd;">4</td></tr><tr><td style="padding:2px;border-bottom: 1px solid #ddd;">5</td><td style="padding:2px;border-bottom: 1px solid #ddd;">5</td><td style="padding:2px;border-bottom: 1px solid #ddd;">5</td></tr><tr><td style="padding:2px;border-bottom: 1px solid #ddd;">6</td><td style="padding:2px;border-bottom: 1px solid #ddd;">6</td><td style="padding:2px;border-bottom: 1px solid #ddd;">6</td></tr><tr><td style="padding:2px;border-bottom: 1px solid #ddd;">7</td><td style="padding:2px;border-bottom: 1px solid #ddd;">7</td><td style="padding:2px;border-bottom: 1px solid #ddd;">7</td></tr><tr><td style="padding:2px;border-bottom: 1px solid #ddd;">8</td><td style="padding:2px;border-bottom: 1px solid #ddd;">8</td><td style="padding:2px;border-bottom: 1px solid #ddd;">8</td></tr><tr><td style="padding:2px;border-bottom: 1px solid #ddd;">9</td><td style="padding:2px;border-bottom: 1px solid #ddd;">9</td><td style="padding:2px;border-bottom: 1px solid #ddd;">9</td></tr></table>

### Export as CSV
First Column,Second Column,Third Column
0,0,0
1,1,1
2,2,2
3,3,3
4,4,4
5,5,5
6,6,6
7,7,7
8,8,8
9,9,9

## Sort table
```java
  StringTable st = new StringTable("First Column", "Second Column", "Third Column");
  for (int i = 0; i < 10; i++) {
    st.addRow(i, i, i);
  }
  st.sort(0, false);
```

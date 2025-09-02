package com.example.mytube.common;

import com.p6spy.engine.common.ConnectionInformation;
import com.p6spy.engine.event.JdbcEventListener;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.SQLException;

@Component
public class P6SpyFormatter extends JdbcEventListener implements MessageFormattingStrategy {

    // ANSI Color Codes
    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";      // SELECT, INSERT, UPDATE, DELETE
    private static final String GREEN = "\u001B[32m";     // FROM, WHERE, JOIN
    private static final String YELLOW = "\u001B[33m";    // ORDER BY, GROUP BY, HAVING
    private static final String CYAN = "\u001B[36m";      // AND, OR, NOT
    private static final String MAGENTA = "\u001B[35m";   // VALUES, SET
    private static final String RED = "\u001B[31m";       // Execution time
    private static final String BRIGHT_GREEN = "\u001B[92m"; // String values
    private static final String BRIGHT_YELLOW = "\u001B[93m"; // Numbers
    private static final String BRIGHT_CYAN = "\u001B[96m";   // NULL values
    private static final String BRIGHT_MAGENTA = "\u001B[95m"; // Boolean values

    @Override
    public void onAfterGetConnection(ConnectionInformation connectionInformation, SQLException e) {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(getClass().getName());
    }

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if (!StringUtils.hasText(sql)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        // Execution time with color
        sb.append(GREEN).append("execution ").append(elapsed).append("ms").append(RESET);
        sb.append(" | ");

        // Format and colorize SQL
        String formattedSql = formatAndColorize(sql);
        sb.append(formattedSql);

        return sb.toString();
    }

    private String formatAndColorize(String sql) {
        // First format the SQL
        String formatted = format(sql);

        // Apply colors to actual values first (before keywords to avoid conflicts)
        formatted = colorizeValues(formatted);

        // Then apply colors to SQL keywords and convert to uppercase
        formatted = formatted
                // Main SQL commands (MAGENTA)
                .replaceAll("(?i)\\bselect\\b", MAGENTA + "SELECT" + RESET)
                .replaceAll("(?i)\\binsert\\b", MAGENTA + "INSERT" + RESET)
                .replaceAll("(?i)\\bupdate\\b", MAGENTA + "UPDATE" + RESET)
                .replaceAll("(?i)\\bdelete\\b", MAGENTA + "DELETE" + RESET)

                // Table/Join keywords (MAGENTA)
                .replaceAll("(?i)\\bfrom\\b", MAGENTA + "FROM" + RESET)
                .replaceAll("(?i)\\bwhere\\b", MAGENTA + "WHERE" + RESET)
                .replaceAll("(?i)\\bjoin\\b", MAGENTA + "JOIN" + RESET)
                .replaceAll("(?i)\\binner join\\b", MAGENTA + "INNER JOIN" + RESET)
                .replaceAll("(?i)\\bleft join\\b", MAGENTA + "LEFT JOIN" + RESET)
                .replaceAll("(?i)\\bright join\\b", MAGENTA + "RIGHT JOIN" + RESET)
                .replaceAll("(?i)\\bon\\b", MAGENTA + "ON" + RESET)

                // Ordering/Grouping (MAGENTA)
                .replaceAll("(?i)\\border by\\b", MAGENTA + "ORDER BY" + RESET)
                .replaceAll("(?i)\\bgroup by\\b", MAGENTA + "GROUP BY" + RESET)
                .replaceAll("(?i)\\bhaving\\b", MAGENTA + "HAVING" + RESET)
                .replaceAll("(?i)\\blimit\\b", MAGENTA + "LIMIT" + RESET)
                .replaceAll("(?i)\\boffset\\b", MAGENTA + "OFFSET" + RESET)
                .replaceAll("(?i)\\basc\\b", MAGENTA + "ASC" + RESET)
                .replaceAll("(?i)\\bdesc\\b", MAGENTA + "DESC" + RESET)

                // Logical operators (MAGENTA)
                .replaceAll("(?i)\\band\\b", MAGENTA + "AND" + RESET)
                .replaceAll("(?i)\\bor\\b", MAGENTA + "OR" + RESET)
                .replaceAll("(?i)\\bnot\\b", MAGENTA + "NOT" + RESET)
                .replaceAll("(?i)\\bin\\b", MAGENTA + "IN" + RESET)
                .replaceAll("(?i)\\bexists\\b", MAGENTA + "EXISTS" + RESET)
                .replaceAll("(?i)\\bis null\\b", MAGENTA + "IS NULL" + RESET)
                .replaceAll("(?i)\\bis not null\\b", MAGENTA + "IS NOT NULL" + RESET)
                .replaceAll("(?i)\\blike\\b", MAGENTA + "LIKE" + RESET)
                .replaceAll("(?i)\\bbetween\\b", MAGENTA + "BETWEEN" + RESET)

                // Insert/Update specific (MAGENTA)
                .replaceAll("(?i)\\bvalues\\b", MAGENTA + "VALUES" + RESET)
                .replaceAll("(?i)\\bset\\b", MAGENTA + "SET" + RESET)
                .replaceAll("(?i)\\binto\\b", MAGENTA + "INTO" + RESET)

                // Additional common keywords (MAGENTA)
                .replaceAll("(?i)\\bas\\b", MAGENTA + "AS" + RESET)
                .replaceAll("(?i)\\bdistinct\\b", MAGENTA + "DISTINCT" + RESET)
                .replaceAll("(?i)\\bcount\\b", MAGENTA + "COUNT" + RESET)
                .replaceAll("(?i)\\bsum\\b", MAGENTA + "SUM" + RESET)
                .replaceAll("(?i)\\bavg\\b", MAGENTA + "AVG" + RESET)
                .replaceAll("(?i)\\bmax\\b", MAGENTA + "MAX" + RESET)
                .replaceAll("(?i)\\bmin\\b", MAGENTA + "MIN" + RESET);

        return formatted;
    }

    private String colorizeValues(String sql) {
        // Color string values (anything in single quotes) - Bright Green
        sql = sql.replaceAll("'([^']*)'", "'" + BLUE + "$1" + RESET + "'");

        // Color NULL values - Bright Cyan
        sql = sql.replaceAll("\\bNULL\\b", BRIGHT_YELLOW + "NULL" + RESET);

        // Color boolean values - Bright Magenta
        sql = sql.replaceAll("\\btrue\\b", BRIGHT_YELLOW + "true" + RESET);
        sql = sql.replaceAll("\\bfalse\\b", BRIGHT_YELLOW + "false" + RESET);

        // Color numeric values (integers and decimals) - Bright Yellow
        // This regex matches numbers that are not part of strings or identifiers
        sql = sql.replaceAll("(?<!\\w)(-?\\d+(?:\\.\\d+)?)(?!\\w)", BLUE + "$1" + RESET);

        return sql;
    }

    private String format(String sql) {
        if (isDDL(sql)) {
            return FormatStyle.DDL.getFormatter().format(sql);
        } else if (isBasic(sql)) {
            return FormatStyle.BASIC.getFormatter().format(sql);
        }
        return sql;
    }

    private boolean isDDL(String sql) {
        return sql.toLowerCase().matches("^\\s*(create|alter|drop|comment)\\b.*");
    }

    private boolean isBasic(String sql) {
        return sql.toLowerCase().matches("^\\s*(select|insert|update|delete)\\b.*");
    }
}
//package com.example.mytube.common;
//
//import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
//import org.hibernate.engine.jdbc.internal.FormatStyle;
//
//public class UpperCaseSqlFormatter implements MessageFormattingStrategy {
//    @Override
//    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
//        if (sql == null || sql.trim().isEmpty()) return "";
//
//        String formatted = FormatStyle.BASIC.getFormatter().format(sql);
//
//        // 절만 대문자로 바꾸기 (정규식 활용)
//        formatted = formatted
//                .replaceAll("(?i)\\bselect\\b", "SELECT")
//                .replaceAll("(?i)\\binsert\\b", "INSERT")
//                .replaceAll("(?i)\\bupdate\\b", "UPDATE")
//                .replaceAll("(?i)\\bdelete\\b", "DELETE")
//                .replaceAll("(?i)\\bfrom\\b", "FROM")
//                .replaceAll("(?i)\\bwhere\\b", "WHERE")
//                .replaceAll("(?i)\\border by\\b", "ORDER BY")
//                .replaceAll("(?i)\\bgroup by\\b", "GROUP BY");
//
//        return String.format("execution %dms | %s", elapsed, formatted);
//    }
//}

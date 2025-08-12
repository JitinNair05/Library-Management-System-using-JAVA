package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class CsvUtil {

    public static List<String[]> readCsv(Path file) throws IOException {
        List<String[]> rows = new ArrayList<>();
        if (!Files.exists(file)) return rows;
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            rows.add(parseCsvLine(line));
        }
        return rows;
    }

    public static void writeLines(Path file, List<String> lines) throws IOException {
        Files.createDirectories(file.getParent());
        Files.write(file, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    // very small CSV parser - handles simple quoted values
    private static String[] parseCsvLine(String line) {
        List<String> cols = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuote = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuote) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"'); // escaped quote
                        i++;
                    } else {
                        inQuote = false;
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == ',') {
                    cols.add(cur.toString());
                    cur.setLength(0);
                } else if (c == '"') {
                    inQuote = true;
                } else {
                    cur.append(c);
                }
            }
        }
        cols.add(cur.toString());
        return cols.toArray(new String[0]);
    }
}

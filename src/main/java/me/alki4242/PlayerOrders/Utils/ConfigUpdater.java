package me.alki4242.PlayerOrders.Utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigUpdater {

    /**
     * Config'i günceller: yorumları korur, eksik key'leri ekler, mevcut değerleri silmez.
     * @param defaultConfigResource Jar içindeki default config (örn: "config.yml")
     * @param userConfigFile Gerçek kullanıcı config dosyası
     * @throws IOException Hata durumunda
     */
    public static void update(InputStream defaultConfigResource, File userConfigFile) throws IOException {
        if (!userConfigFile.exists()) return;

        // Default config
        List<String> defaultLines = readLines(defaultConfigResource);
        // Kullanıcı config
        List<String> userLines = readLines(new FileInputStream(userConfigFile));

        // Map: key -> value
        Map<String, String> userValues = extractKeyValues(userLines);

        List<String> output = new ArrayList<>();
        StringBuilder pendingComments = new StringBuilder();

        for (String line : defaultLines) {
            if (line.trim().startsWith("#") || line.trim().isEmpty()) {
                // Yorum veya boş satır
                pendingComments.append(line).append("\n");
                continue;
            }

            String key = line.split(":", 2)[0].trim();
            if (userValues.containsKey(key)) {
                String value = userValues.get(key);
                String indent = getIndent(line);
                // Yorum varsa ekle
                if (pendingComments.length() > 0) {
                    output.add(pendingComments.toString().trim());
                    pendingComments = new StringBuilder();
                }
                output.add(indent + key + ": " + value);
                userValues.remove(key);
            } else {
                if (pendingComments.length() > 0) {
                    output.add(pendingComments.toString().trim());
                    pendingComments = new StringBuilder();
                }
                output.add(line);
            }
        }

        // Kullanıcı config’te olup default’ta olmayan key’leri ekle
        if (!userValues.isEmpty()) {
            output.add("");
            output.add("# === Kullanıcı eklediği ekstra ayarlar ===");
            for (Map.Entry<String, String> entry : userValues.entrySet()) {
                output.add(entry.getKey() + ": " + entry.getValue());
            }
        }

        // Dosyaya yaz
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(userConfigFile), StandardCharsets.UTF_8)) {
            for (String line : output) {
                writer.write(line + System.lineSeparator());
            }
        }
    }

    private static List<String> readLines(InputStream input) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) lines.add(line);
        return lines;
    }

    private static Map<String, String> extractKeyValues(List<String> lines) {
        Map<String, String> map = new HashMap<>();
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#") || !trimmed.contains(":")) continue;
            String[] split = trimmed.split(":", 2);
            map.put(split[0].trim(), split[1].trim());
        }
        return map;
    }

    private static String getIndent(String line) {
        int i = 0;
        while (i < line.length() && line.charAt(i) == ' ') i++;
        return " ".repeat(i);
    }
}

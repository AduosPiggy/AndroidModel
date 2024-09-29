package com.example.androidmodel.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author kfflso
 * @data 2024/9/29 15:34
 * @plus:
 */
public class Kfflso_FileUtils {
    public static void checkAndCreateFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs(); // Create all directories up to this point
            }
            try {
                file.createNewFile(); // Create the file
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readFileToString(String filePath){
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString().trim();
    }

    public static void appendToFile(String filePath, String text) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true)); // 'true' for append mode
            bw.write(text);
            bw.newLine(); // This writes a newline
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

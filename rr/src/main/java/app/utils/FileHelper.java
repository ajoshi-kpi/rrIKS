package app.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
    private static final String SEPARATOR = "\n";

    public static void appendToFile(String filename, List<Long> listToAppend) throws IOException {
        List<Long> oldValues = new ArrayList<Long>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String currentLine;
        if (!isEmpty(filename)) {
            StringBuilder sb = new StringBuilder();
            while ((currentLine = reader.readLine()) != null) {
                sb.append(currentLine.trim()).append(SEPARATOR);
            }
            for (String s : sb.toString().trim().split(SEPARATOR)) {
                oldValues.add(Long.valueOf(s));
            }
        }
        reader.close();

        for (Long aLong : listToAppend) {
            oldValues.add(aLong);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        for (Long aLong : oldValues) {
            writer.write(String.valueOf(aLong));
            writer.write(SEPARATOR);
        }
        writer.close();
    }

    public static List<Long> readFromFile(String filename) throws IOException {
        List<Long> oldValues = new ArrayList<Long>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String currentLine;
        if (!isEmpty(filename)) {
            StringBuilder sb = new StringBuilder();
            while ((currentLine = reader.readLine()) != null) {
                sb.append(currentLine.trim()).append(SEPARATOR);
            }
            for (String s : sb.toString().trim().split(SEPARATOR)) {
                oldValues.add(Long.valueOf(s));
            }
        }
        reader.close();
        return oldValues;
    }

    public static boolean isEmpty(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        if (reader.readLine() == null) {
            return true;
        }
        return false;
    }

    public static String getPasswordFromFile(String filename) throws IOException {
        String password;
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        password = reader.readLine();
        reader.close();
        return password;
    }

    public static void savePasswordToFile(String filename, String password) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(password.toLowerCase());
        writer.newLine();
        writer.flush();
        writer.close();
    }
}

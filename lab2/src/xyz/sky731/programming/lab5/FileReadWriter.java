package xyz.sky731.programming.lab5;

import java.io.*;

public class FileReadWriter {
    private String filename;

    public FileReadWriter(String filename) {
        this.filename = filename;
    }

    public String readFromFile() throws IOException {
        StringBuilder result = new StringBuilder();
        InputStream inputStream = new FileInputStream(filename);

        try (Reader inputStreamReader = new InputStreamReader(inputStream)) {
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String str;
            while ((str = reader.readLine()) != null && str.length() != 0) {
                result.append(str);
                result.append("\n");
            }
        }
        return result.toString();
    }

    public void writeToFile(String string) throws IOException {
        OutputStream outputStream = new FileOutputStream(filename);
        try (Writer outputStreamWriter = new OutputStreamWriter(outputStream)) {
            outputStreamWriter.write(string);
        }
    }
}

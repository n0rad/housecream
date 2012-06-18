package net.awired.housecream.server.core.application.enumeration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public enum FileInfoEnum {
    readme("README.rst"), //
    changelog("CHANGELOG.txt"), //
    install("INSTALL.txt"), //
    licence("LICENCE.txt");

    private final String filename;

    private FileInfoEnum(String filename) {
        this.filename = filename;
    }

    public void display() {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        try {
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

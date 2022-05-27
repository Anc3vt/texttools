package com.ancevt.tools.texttool;

import com.ancevt.util.args.Args;
import com.ancevt.util.args.ArgsParameter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

public class TexttoolLicense {

    public static void main(String[] args) throws IOException {
        Args a = Args.of(args);

        ArgsInput argsInput = a.convert(ArgsInput.class);
        String headerPath = argsInput.getHeaderPath();
        String licenseHeaderText = Files.readString(Path.of(headerPath));

        Files.walk(Path.of("./"))
                .filter(path -> path.endsWith(".java"))
                .toList()
                .forEach(path -> {
                    try {
                        String string = Files.readString(path);

                        if (string.startsWith("/**")) {
                            removeOldLicense(string, path);
                        }

                        addLicenseHeader(string, path, licenseHeaderText);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private static void addLicenseHeader(String string, Path path, String header) throws IOException {
        String resultText = header + string;
        Files.writeString(path, resultText, StandardCharsets.UTF_8, WRITE, TRUNCATE_EXISTING);
    }

    private static void removeOldLicense(String string, Path path) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        boolean doNotAdd = true;

        for (String line : string.lines().toList()) {
            String lineTrim = line.trim();
            if (!lineTrim.startsWith("/**") && !lineTrim.startsWith("*")) doNotAdd = false;

            if (!doNotAdd) {
                stringBuilder.append(line).append(System.lineSeparator());
            }
        }

        Files.writeString(path, stringBuilder.toString(), StandardCharsets.UTF_8, WRITE, TRUNCATE_EXISTING);
    }


    private static class ArgsInput {

        @ArgsParameter(names = { "--header", "-h" }, required = true)
        private String headerPath;

        public String getHeaderPath() {
            return headerPath;
        }
    }

}

package ru.unn.webservice.test_automation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class WindowsGCC_CPP_Builder implements ICPPBuilder {
    @Override
    public byte[] build(String sourceFilePath, String outputFilePath) {
        try {
            Process process = new ProcessBuilder(
                    "g++",sourceFilePath, "-o", outputFilePath).start();
            InputStream is = process.getErrorStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            return result.getBytes(Charset.forName("UTF-8"));
        } catch (Exception ex) {
            return null;
        }
    }
}

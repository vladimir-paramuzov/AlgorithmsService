package ru.unn.webservice.test_automation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class WindowsGCC_CPP_Tester implements ICPPTester {
    @Override
    public byte[] test(String executablePath, String testDataPath) {
        try {
            Process process = new ProcessBuilder(
                    executablePath, testDataPath).start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            InputStream es = process.getErrorStream();
            InputStreamReader esr = new InputStreamReader(es);
            BufferedReader ebr = new BufferedReader(esr);

            while ((line = ebr.readLine()) != null) {
                result += line;
            }

            return result.getBytes(Charset.forName("UTF-8"));
        } catch (Exception ex) {
            return null;
        }
    }
}

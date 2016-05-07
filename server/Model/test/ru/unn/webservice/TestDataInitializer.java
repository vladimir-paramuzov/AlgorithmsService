package ru.unn.webservice;

import ru.unn.webservice.storage.Algorithm;
import ru.unn.webservice.storage.DataAccess;
import ru.unn.webservice.storage.Statistic;
import ru.unn.webservice.storage.User;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static ru.unn.webservice.storage.Language.CPP;
import static ru.unn.webservice.storage.User.TYPE.ADMIN;
import static ru.unn.webservice.storage.User.TYPE.DEVELOPER;
import static ru.unn.webservice.storage.User.TYPE.USER;

public class TestDataInitializer {
    public TestDataInitializer(DataAccess dataAccess) {
        user = new User("testUser", "user", USER, 100, purchasedAlgorithms1);
        developer = new User("testDeveloper", "developer", DEVELOPER, 200, purchasedAlgorithms2);
        admin = new User("testAdmin", "admin", ADMIN, 300, purchasedAlgorithms3);

        alg1 = new Algorithm("alg1", "superalg1", tags1, sourceFile, testFile, "testUser", 0, CPP);
        alg2 = new Algorithm("alg2", "superalg2", tags2, sourceFile, testFile, "testDeveloper", 20, CPP);
        alg3 = new Algorithm("alg3", "superalg3", tags3, sourceFile, testFile, "testAdmin", 300, CPP);

        this.dataAccess = dataAccess;
        tags1.add("tag1");
        tags1.add("tag2");
        tags2.add("tag1");
        tags3.add("tag1");
        tags3.add("tag2");
        tags3.add("tag3");

        purchasedAlgorithms1.add("alg1");
        purchasedAlgorithms2.add("alg1");
        purchasedAlgorithms2.add("alg2");
        purchasedAlgorithms3.add("alg1");
        purchasedAlgorithms3.add("alg2");
        purchasedAlgorithms3.add("alg3");

        downloads.add(Calendar.getInstance().getTime());
        purchases.add(Calendar.getInstance().getTime());
    }

    public void init() {
        clear();

        dataAccess.writeAlgorithm(alg1);
        dataAccess.writeAlgorithm(alg2);
        dataAccess.writeAlgorithm(alg3);

        dataAccess.writeUser(user);
        dataAccess.writeUser(developer);
        dataAccess.writeUser(admin);

        Statistic statistic = new Statistic(downloads, purchases);
        dataAccess.writeStatistic(statistic);
    }

    public void clear() {
        dataAccess.deleteUsers();
        dataAccess.deleteAlgorithms();
        dataAccess.deleteStatistic();
    }

    public Algorithm alg1;
    public Algorithm alg2;
    public Algorithm alg3;
    public User user;
    public User developer;
    public User admin;
    public DataAccess dataAccess;
    public String testdata = "Here will be test data";
    public byte[] testFile = testdata.getBytes(Charset.forName("UTF-8"));
    public ArrayList<String> tags1 = new ArrayList<>();
    public ArrayList<String> tags2 = new ArrayList<>();
    public ArrayList<String> tags3 = new ArrayList<>();
    public ArrayList<String> purchasedAlgorithms1 = new ArrayList<>();
    public ArrayList<String> purchasedAlgorithms2 = new ArrayList<>();
    public ArrayList<String> purchasedAlgorithms3 = new ArrayList<>();
    public ArrayList<Date> downloads = new ArrayList<>();
    public ArrayList<Date> purchases = new ArrayList<>();

    public String code = "#include <stdio.h>\n" +
            "#include <iostream>\n" +
            "#include <fstream>\n" +
            "\n" +
            "int main(int argc, char* argv[]) {\n" +
            "\n" +
            "\tif (argc < 2) {\n" +
            "\t\tprintf(\"Error\\n\");\n" +
            "\t\treturn 1;\n" +
            "\t}\n" +
            "\n" +
            "\tchar* path = argv[1];\n" +
            "\n" +
            "\tstd::ifstream input(path);\n" +
            "\tstd::string line;\n" +
            "\tif (input.is_open())\n" +
            "\t{\n" +
            "\t\twhile ( std::getline (input,line) )\n" +
            "\t\t{\n" +
            "\t\t\tstd::cout << line << '\\n';\n" +
            "\t\t}\n" +
            "\t\tinput.close();\n" +
            "\t\treturn 0;\n" +
            "\t}\n" +
            "\n" +
            "\tprintf(\"Couldn't open file\\n\");\n" +
            "\treturn 1;\n" +
            "}";
    public byte[] sourceFile = code.getBytes(Charset.forName("UTF-8"));

}

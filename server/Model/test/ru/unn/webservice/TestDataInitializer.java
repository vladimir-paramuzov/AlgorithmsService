package ru.unn.webservice;

import ru.unn.webservice.storage.Algorithm;
import ru.unn.webservice.storage.DataAccess;
import ru.unn.webservice.storage.Statistic;
import ru.unn.webservice.storage.User;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static ru.unn.webservice.storage.Algorithm.Language.CPP;
import static ru.unn.webservice.storage.User.TYPE.ADMIN;
import static ru.unn.webservice.storage.User.TYPE.DEVELOPER;
import static ru.unn.webservice.storage.User.TYPE.USER;

public class TestDataInitializer {
    public TestDataInitializer(DataAccess dataAccess) {
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

        Algorithm alg1 = new Algorithm("alg1", "superalg1", tags1, sourceFile, testFile, "testUser", 10, CPP);
        Algorithm alg2 = new Algorithm("alg2", "superalg2", tags2, sourceFile, testFile, "testDeveloper", 20, CPP);
        Algorithm alg3 = new Algorithm("alg3", "superalg3", tags3, sourceFile, testFile, "testAdmin", 30, CPP);

        dataAccess.writeAlgorithm(alg1);
        dataAccess.writeAlgorithm(alg2);
        dataAccess.writeAlgorithm(alg3);

        User user = new User("testUser", "user", USER, 100, purchasedAlgorithms1);
        User developer = new User("testDeveloper", "developer", DEVELOPER, 200, purchasedAlgorithms2);
        User admin = new User("testAdmin", "admin", ADMIN, 300, purchasedAlgorithms3);

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

    public DataAccess dataAccess;
    public String code = "#include <cstdio>";
    public String testdata = "Here will be test data";
    public byte[] sourceFile = code.getBytes(Charset.forName("UTF-8"));
    public byte[] testFile = testdata.getBytes(Charset.forName("UTF-8"));
    public ArrayList<String> tags1 = new ArrayList<>();
    public ArrayList<String> tags2 = new ArrayList<>();
    public ArrayList<String> tags3 = new ArrayList<>();
    public ArrayList<String> purchasedAlgorithms1 = new ArrayList<>();
    public ArrayList<String> purchasedAlgorithms2 = new ArrayList<>();
    public ArrayList<String> purchasedAlgorithms3 = new ArrayList<>();
    public ArrayList<Date> downloads = new ArrayList<>();
    public ArrayList<Date> purchases = new ArrayList<>();
}

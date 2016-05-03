package ru.unn.webservice.storage;

import java.io.Serializable;
import java.util.ArrayList;

public class Algorithm implements Serializable {
    public enum Language { CPP }

    public String name;
    public String description;
    public ArrayList<String> tags;
    public byte[] sourceFile;
    public byte[] testFile;
    public String owner;
    public int cost;
    public Language lang;

    public Algorithm(String name, String description, ArrayList<String> tags, byte[] sourceFile, byte[] testFile, String owner, int cost, Language lang) {
        this.name = name;
        this.description = description;
        this.tags = tags;
        this.sourceFile = sourceFile;
        this.testFile = testFile;
        this.owner = owner;
        this.cost = cost;
        this.lang = lang;
    }

    public void print() {
        System.out.println("Name: " + name + " Description: " + description + " Owner: " + owner + " Cost: " + cost);
    }
}

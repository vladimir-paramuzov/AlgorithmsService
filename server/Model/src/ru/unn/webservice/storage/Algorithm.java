package ru.unn.webservice.storage;

import java.io.Serializable;
import java.util.ArrayList;

public class Algorithm implements Serializable {
    public enum Language {
        CPP("CPP");

        private String lang;

        Language(final String lang) {
            this.lang = lang;
        }

        @Override
        public String toString() {
            return lang;
        }
    }

    public String name;
    public String description;
    public String owner;
    public ArrayList<String> tags;
    public int cost;
    public Language lang;
    public byte[] sourceFile;
    public byte[] testFile;

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
        System.out.println("Name: " + name
                         + " Description: " + description
                         + " Language: " + lang
                         + " Owner: " + owner
                         + " Cost: " + cost);
    }
}

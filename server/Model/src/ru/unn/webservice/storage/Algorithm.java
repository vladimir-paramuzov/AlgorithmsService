package ru.unn.webservice.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Algorithm algorithm = (Algorithm) o;

        if (cost != algorithm.cost) return false;
        if (!name.equals(algorithm.name)) return false;
        if (!description.equals(algorithm.description)) return false;
        if (!owner.equals(algorithm.owner)) return false;
        if (!tags.equals(algorithm.tags)) return false;
        if (lang != algorithm.lang) return false;
        if (!Arrays.equals(sourceFile, algorithm.sourceFile)) return false;
        return Arrays.equals(testFile, algorithm.testFile);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + owner.hashCode();
        result = 31 * result + tags.hashCode();
        result = 31 * result + cost;
        result = 31 * result + lang.hashCode();
        result = 31 * result + Arrays.hashCode(sourceFile);
        result = 31 * result + Arrays.hashCode(testFile);
        return result;
    }
}

package ru.unn.webservice.infrastructure;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    public enum TYPE
    {
        USER("User"),
        DEVELOPER("Developer"),
        ADMIN("Admin");

        private String type;

        TYPE(final String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    public String login;
    public String pass;
    public TYPE type;
    public int balance;
    public ArrayList<String> purchasedAlgorithms;

    public User(String login, String pass, TYPE type, int balance, ArrayList<String> purchasedAlgorithms) {
        this.login = login;
        this.pass = pass;
        this.type = type;
        this.balance = balance;
        this.purchasedAlgorithms = purchasedAlgorithms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (balance != user.balance) return false;
        if (!login.equals(user.login)) return false;
        if (!pass.equals(user.pass)) return false;
        if (type != user.type) return false;
        return purchasedAlgorithms.equals(user.purchasedAlgorithms);

    }

    @Override
    public int hashCode() {
        int result = login.hashCode();
        result = 31 * result + pass.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + balance;
        result = 31 * result + purchasedAlgorithms.hashCode();
        return result;
    }

    public void print() {
        System.out.println("Login: " + login + " Password: " + pass + " Type: " + type + " Money: " + balance);
    }
}

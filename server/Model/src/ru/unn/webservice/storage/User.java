package ru.unn.webservice.storage;

import java.io.Serializable;

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
    public int money;

    public User(String login, String pass, TYPE type, int money) {
        this.login = login;
        this.pass = pass;
        this.type = type;
        this.money = money;
    }

    public void print() {
        System.out.println("Login: " + login + " Password: " + pass + " Type: " + type + " Money: " + money);
    }
}

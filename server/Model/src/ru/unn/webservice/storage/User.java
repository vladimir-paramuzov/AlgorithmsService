package ru.unn.webservice.storage;

import java.io.Serializable;

public class User implements Serializable {
    public String login;
    public String pass;
    public int type;
    public int money;

    public User(String login, String pass, int type, int money) {
        this.login = login;
        this.pass = pass;
        this.type = type;
        this.money = money;
    }

    public void print() {
        System.out.println("Login: " + login + " Password: " + pass + " Type: " + type + " Money: " + money);
    }
}

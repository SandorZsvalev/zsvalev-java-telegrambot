package org.telbot.telran.info.roles;

public enum Roles {

    USER("user"),
    ADMINISTRATOR("admin");

    private String name;

    Roles(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package org.telbot.telran.info.commands;

public enum Command {

    START("/start"),
    STOP("/stop");
    private String name;

    Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

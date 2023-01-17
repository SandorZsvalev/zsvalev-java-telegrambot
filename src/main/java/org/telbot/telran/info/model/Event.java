package org.telbot.telran.info.model;

import java.util.List;

public class Event {

    List<TelegramChannelPost> report;

    public Event() {
        //
    }

    public List<TelegramChannelPost> getReport() {
        return report;
    }

    public void setReport(List<TelegramChannelPost> report) {
        this.report = report;
    }
}

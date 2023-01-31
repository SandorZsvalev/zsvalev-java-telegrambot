package org.telbot.telran.info.model;

import javax.persistence.*;

@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    private int userId;

    @Column(name = "date_time")
    private String date;

    @Column(name = "message")
    private String message;

    @Column(name = "to_send")
    private boolean toSend;

    public Event() {
        //
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isToSend() {
        return toSend;
    }

    public void setToSend(boolean toSend) {
        this.toSend = toSend;
    }
}

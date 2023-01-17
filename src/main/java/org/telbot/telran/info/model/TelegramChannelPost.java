package org.telbot.telran.info.model;

import javax.persistence.*;

@Entity
@Table(name = "telegram_channel_post")
public class TelegramChannelPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "channel_tlg_id")
    private long channelTlgId;

    @Column(name = "message_id")
    private int messageId;

    @Column(name = "text_message")
    private String text;

    @Column(name = "date_message")
    private int date;

    public TelegramChannelPost() {
    //
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getChannelTlgId() {
        return channelTlgId;
    }

    public void setChannelTlgId(long channelTlgId) {
        this.channelTlgId = channelTlgId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}

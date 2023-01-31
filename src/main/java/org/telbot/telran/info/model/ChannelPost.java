package org.telbot.telran.info.model;

import javax.persistence.*;

@Entity
@Table(name = "channel_post")
public class ChannelPost {

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

    @Column(name = "unread")
    private boolean unread;

    @Column(name = "channel_inside_id")
    private int channelInsideId;

    public ChannelPost() {
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

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public int getChannelInsideId() {
        return channelInsideId;
    }

    public void setChannelInsideId(int channelInsideId) {
        this.channelInsideId = channelInsideId;
    }
}

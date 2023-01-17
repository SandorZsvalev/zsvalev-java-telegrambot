package org.telbot.telran.info.model;

import javax.persistence.*;

@Entity
@Table(name = "channel")
public class Channel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "channel_name")
    String channelName;

    @Column(name="channel_tlg_id")
    long channelTlgId;

    public Channel() {
        //
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public long getChannelTlgId() {
        return channelTlgId;
    }

    public void setChannelTlgId(long channelTlgId) {
        this.channelTlgId = channelTlgId;
    }
}

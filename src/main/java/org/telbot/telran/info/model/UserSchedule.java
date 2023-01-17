package org.telbot.telran.info.model;

import javax.persistence.*;

@Entity
@Table(name = "user_schedule")
public class UserSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="user_id")
    private int userId;

//    private User user;

}

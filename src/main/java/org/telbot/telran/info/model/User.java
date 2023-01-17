package org.telbot.telran.info.model;

import javax.persistence.*;

@Entity
@Table(name = "user_tbl")
public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        // можно заменить на GenerationType.IDENTITY тогда не будет сквозной нумерации
        private int id;
        @Column(name = "user_name")
        private String name;

        public User() {
            ///
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }
}

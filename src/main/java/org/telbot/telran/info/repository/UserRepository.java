package org.telbot.telran.info.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telbot.telran.info.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}

package org.telbot.telran.info.repository;

//1. Extend from JpaRepository
//2. Create new methods if needed

import org.springframework.data.jpa.repository.JpaRepository;
import org.telbot.telran.info.model.User;

public interface UserRepository extends JpaRepository<User,Integer> {
}

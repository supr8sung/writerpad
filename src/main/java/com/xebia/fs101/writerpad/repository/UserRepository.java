package com.xebia.fs101.writerpad.repository;

import com.xebia.fs101.writerpad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameOrEmail(String userName, String email);
}

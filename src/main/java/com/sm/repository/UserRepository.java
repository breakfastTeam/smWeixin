package com.sm.repository;

import com.sm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Ez丶kkk on 15/2/8.
 */

public interface UserRepository extends JpaRepository<User,Long> {

    User findByLoginNameAndPassword(String loginName, String password);

}

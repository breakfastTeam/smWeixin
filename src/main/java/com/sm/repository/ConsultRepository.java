package com.sm.repository;

import com.sm.entity.Consult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Ez丶kkk on 15/2/8.
 */

public interface ConsultRepository extends JpaRepository<Consult,Long> {


    List<Consult> findByOpenId(String openId);
}

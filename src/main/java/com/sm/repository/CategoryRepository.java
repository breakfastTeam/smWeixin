package com.sm.repository;

import com.sm.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Ez丶kkk on 15/2/8.
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {

    List<Category> findByType(String category);

    Category findOneByType(String category);
}

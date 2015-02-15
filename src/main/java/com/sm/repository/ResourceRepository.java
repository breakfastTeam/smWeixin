package com.sm.repository;

import com.sm.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Ez丶kkk on 15/2/8.
 */
public interface ResourceRepository extends JpaRepository<Resource,Long> {

    Page<Resource> findByNameLike(String name, Pageable pageable);

    List<Resource> findByNameLikeAndCategoryId(String name, Long categoryId);

    Page<Resource> findByNameLikeAndCategoryId(String name, Long categoryId, Pageable pageable);

    List<Resource> findByCategoryId(Long categoryId);

    List<Resource> findByCategoryIdIn(List<Long> ids);
}

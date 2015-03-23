package com.sm.repository;

import com.sm.entity.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Set;

/**
 * Created by Ez丶kkk on 15/2/8.
 */
public interface ResourceRepository extends JpaRepository<Resource,Long> {

    List<Resource> findByNameLikeAndCategoryId(String name, Long categoryId);

    Page<Resource> findByNameLikeAndCategoryId(String name, Long categoryId, Pageable pageable);

    List<Resource> findByCategoryId(Long categoryId);

    List<Resource> findByCategoryIdIn(Set<Long> ids);
}

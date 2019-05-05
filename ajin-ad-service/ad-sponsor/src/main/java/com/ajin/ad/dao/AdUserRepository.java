package com.ajin.ad.dao;

import com.ajin.ad.entity.AdUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: ajin
 * @Date: 2019/4/5 16:23
 */
public interface AdUserRepository extends JpaRepository<AdUser, Long> {
    /**
     * <h2>根据用户名查询用户记录</h2>
     */
    AdUser findByUsername(String username);

}

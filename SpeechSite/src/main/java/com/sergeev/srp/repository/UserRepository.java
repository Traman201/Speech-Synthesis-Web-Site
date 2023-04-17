package com.sergeev.srp.repository;


import com.sergeev.srp.entity.site.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {

    SiteUser findByUsername(String username);
}

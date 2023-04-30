package com.sergeev.srp.site.repository;


import com.sergeev.srp.site.entity.site.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<SiteUser, Long> {

    SiteUser findByUsername(String username);
}

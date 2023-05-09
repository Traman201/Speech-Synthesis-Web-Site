package com.sergeev.srp.site.repository;

import com.sergeev.srp.site.entity.site.Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudioRepository extends JpaRepository<Audio, Long> {

    List<Audio> findBySessionId(String sessionId);
}

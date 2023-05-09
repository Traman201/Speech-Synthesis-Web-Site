package com.sergeev.srp.site.repository;

import com.sergeev.srp.site.entity.site.Phonemes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhonemeRepository extends JpaRepository<Phonemes, Long> {

    List<Phonemes> findBySessionId(String sessionId);
}

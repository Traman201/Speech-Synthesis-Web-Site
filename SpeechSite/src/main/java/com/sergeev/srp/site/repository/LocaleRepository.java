package com.sergeev.srp.site.repository;

import com.sergeev.srp.site.entity.mary.MaryTTSLocale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocaleRepository extends JpaRepository<MaryTTSLocale, Long> {

    MaryTTSLocale findByName(String name);
}

package com.sergeev.srp.site.repository;

import com.sergeev.srp.site.entity.hmm.HmmModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HmmModelRepository extends JpaRepository<HmmModel, Long> {
}

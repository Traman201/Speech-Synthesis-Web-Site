package com.sergeev.srp.site.entity.site;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Phonemes extends Request {

    @Column(columnDefinition = "TEXT")
    private String phonemes;
}

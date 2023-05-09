package com.sergeev.srp.site.entity.site;

import com.sergeev.srp.common.model.enums.Systems;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Systems systemName;

    private String rawText;

    private String time;

    private String sessionId;


}

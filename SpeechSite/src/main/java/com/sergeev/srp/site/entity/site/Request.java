package com.sergeev.srp.site.entity.site;

import com.sergeev.srp.common.model.enums.Systems;
import jakarta.persistence.*;
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

    @Column(columnDefinition = "TEXT")
    private String rawText;

    private String time;

    private String sessionId;


}

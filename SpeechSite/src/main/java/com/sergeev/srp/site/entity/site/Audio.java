package com.sergeev.srp.site.entity.site;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Audio extends Request {

    private String filePath;


}

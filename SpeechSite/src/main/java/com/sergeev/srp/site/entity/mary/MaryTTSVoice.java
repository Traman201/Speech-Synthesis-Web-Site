package com.sergeev.srp.site.entity.mary;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "maryttsvoice")
@Getter
@Setter
public class MaryTTSVoice {

    @Id
    private String name;


    @Column(name = "name_for_site")
    private String nameForSite;

    @ManyToOne
    @JoinColumn(name = "locale_id", nullable = false)
    private MaryTTSLocale locale;
}

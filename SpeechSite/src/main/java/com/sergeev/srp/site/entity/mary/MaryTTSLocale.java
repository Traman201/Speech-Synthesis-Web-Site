package com.sergeev.srp.site.entity.mary;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity(name = "maryttslocale")
@Getter
@Setter
public class MaryTTSLocale {

    @Id
    private String name;

    @Column(name = "name_for_site")
    private String nameForSite;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "locale")
    private List<MaryTTSVoice> voices;

}

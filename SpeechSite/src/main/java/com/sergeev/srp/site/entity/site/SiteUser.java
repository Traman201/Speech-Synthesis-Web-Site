package com.sergeev.srp.site.entity.site;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    private String username;

    private String password;

    private boolean accountNotLocked;

    @ManyToMany
    @JoinTable(name = "user_group_containment",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserGroup> userGroups;

}

package com.dev.security.core.entity;

import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * This is the user that the client needs to cast to. Or user can return any {@code Principal} object.
 * @auther Archan Gohel
 */

@Component
public class User implements Principal {

    private Long id;
    private String name;
    private Boolean active;


    @Override
    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

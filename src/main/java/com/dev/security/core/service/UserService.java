package com.dev.security.core.service;

import com.dev.security.core.entity.User;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * only for testing purpose
 *
 * @auther Archan Gohel
 */

public interface UserService {

    /**
     * THis is the api that should be implemented by the Application / client to return the Principal.
     * @return
     */
    public User getDefaultUser();

    /**
     * For testing purpose for now.
     * @return
     */
    public User getCurrentUser();
}

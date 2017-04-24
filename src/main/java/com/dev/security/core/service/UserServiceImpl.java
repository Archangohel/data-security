package com.dev.security.core.service;

import com.dev.security.core.entity.User;
import org.springframework.stereotype.Component;

import java.security.Principal;

/**
 * Only for testing purpose for now.
 *
 * @author Archan Gohel
 */

@Component
public class UserServiceImpl implements UserService {

    private static final User defaultUser;

    static {
        defaultUser = new User();
        defaultUser.setActive(true);
        defaultUser.setId(1l);
        defaultUser.setName("admin");
    }

    public User getDefaultUser() {
        return getCurrentUser();
    }

    public User getCurrentUser() {
        return defaultUser;
    }
}

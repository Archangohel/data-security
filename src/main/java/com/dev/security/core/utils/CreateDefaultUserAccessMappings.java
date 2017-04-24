package com.dev.security.core.utils;

import com.dev.security.core.dao.EntitySecurityMappingDao;
import com.dev.security.core.entity.UserEntityAccessMapping;
import com.dev.security.core.service.UserService;
import com.dev.target.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to create default mappings for e2e testing purpose.
 * Created by archangohel on 03/07/16.
 */

@Component
public class CreateDefaultUserAccessMappings {


    @Autowired
    private EntitySecurityMappingDao entitySecurityMappingDao;

    @Autowired
    private UserService userService;

    public  void createDefaultUserAccessMappings() {
        List<UserEntityAccessMapping> userEntityAccessMappings = entitySecurityMappingDao.getUserEntityAccessMappings(userService.getDefaultUser());
        if (userEntityAccessMappings.size() == 0) {
            userEntityAccessMappings = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                UserEntityAccessMapping userMapping = new UserEntityAccessMapping();
                userMapping.setUserId(userService.getDefaultUser().getId());
                userMapping.setEntityClass(Person.class.getName());
                userMapping.setEntityId(String.valueOf(i));
                userEntityAccessMappings.add(userMapping);
            }
            entitySecurityMappingDao.addUserEntityAccessMappings(userEntityAccessMappings);
        }
    }
}

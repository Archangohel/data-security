package com.dev.security.core.service;

import com.dev.security.core.dao.EntitySecurityMappingDao;
import com.dev.security.core.entity.User;
import com.dev.security.core.utils.SecureObjectPropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by archangohel on 27/06/16.
 */

@Component
public class EntitySecurityService {

    @Autowired
    private EntitySecurityMappingDao entitySecurityMappingDao;

    //TODO: Internal cache to replace with redis.
    private Map<String, Boolean> cashedUserAccess = new ConcurrentHashMap<>();

    @Autowired
    private UserService userService;

    /**
     * This method will check for the current user access over the object.
     *
     * @param object
     * @param id
     * @return
     */
    public boolean isAccessibleForCurrentPrincipal(Object object, Object id) {
        User user = userService.getCurrentUser();
        String baseKey = user.getId() + "_" + object.getClass().getName() + "_";
        String key = baseKey + String.valueOf(id);
        if (!cashedUserAccess.containsKey(key)) {
            List<String> accessibleUserIds = entitySecurityMappingDao.getUserEntityAccessIdMappings(user);
            for (String entityId : accessibleUserIds) {
                StringBuilder keyBuilder = new StringBuilder();
                keyBuilder.append(baseKey).append(String.valueOf(entityId));
                cashedUserAccess.put(keyBuilder.toString(), true);
            }
        }
        Boolean isObjectAccessible = cashedUserAccess.get(key);
        return (isObjectAccessible != null) ? isObjectAccessible : false;
    }


    /**
     * This method will check for the current user access over the object.
     *
     * @param object
     * @return
     */
    public boolean isAccessibleForCurrentPrincipal(Object object) {
        Object id = SecureObjectPropertyUtils.getObjectIdentity(object);
        User user = userService.getCurrentUser();
        String baseKey = user.getId() + "_" + object.getClass().getName() + "_";
        String key = baseKey + String.valueOf(id);
        if (!cashedUserAccess.containsKey(key)) {
            List<String> accessibleUserIds = entitySecurityMappingDao.getUserEntityAccessIdMappings(user);
            for (String entityId : accessibleUserIds) {
                StringBuilder keyBuilder = new StringBuilder();
                keyBuilder.append(baseKey).append(String.valueOf(entityId));
                cashedUserAccess.put(keyBuilder.toString(), true);
            }
        }
        Boolean isObjectAccessible = cashedUserAccess.get(key);
        return (isObjectAccessible != null) ? isObjectAccessible : false;
    }

    public boolean isAccessibleForPrincipal(Object object, Principal principal) {

        return false;
    }
}

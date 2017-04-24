package com.dev.security.core.entity;

import org.springframework.stereotype.Component;

/**
 * This class holds the access mapping for the user. Entity id is the id of the entity that user wants to secure.
 * Application will be the owner of the entity_id. Typically this three values makes a unique key, that
 * will be used by the security utility.
 *
 * @auther Archan Gohel
 */

@Component
public class UserEntityAccessMapping {
    Long userId;
    String entityClass;
    String entityId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEntityClass() {
        return entityClass;
    }

    public void setEntityClass(String entityClass) {
        this.entityClass = entityClass;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }
}

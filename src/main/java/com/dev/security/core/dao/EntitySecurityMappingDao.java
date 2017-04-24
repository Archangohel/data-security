package com.dev.security.core.dao;

import com.dev.security.core.entity.User;
import com.dev.security.core.entity.UserEntityAccessMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO to access the security mappings for user.
 * @auther Archan Gohel
 */

@Component
public class EntitySecurityMappingDao {

    @Autowired
    @Qualifier("securityJdbcTemplate")
    private JdbcTemplate securityJdbcTemplate;

    private static final String QUERY_GET_ACCESSIBLE_ENTITY_MAPPINGS_BY_A_USER = "select user_id,entity_class,entity_id from " +
            "UserEntityAccessMapping ueam where ueam.user_id = ?";

    private static final String QUERY_GET_ACCESSIBLE_ENTITY_ID_MAPPINGS_BY_A_USER = "select entity_id from " +
            "UserEntityAccessMapping ueam where ueam.user_id = ?";

    private static final String QUERY_ADD_ENTITY_ACCESS_MAPPINGS = "INSERT INTO UserEntityAccessMapping" +
            "(`user_id`,`entity_class`,`entity_id`)VALUES(?,?,?)";

    private static RowMapper<UserEntityAccessMapping> userEntityAccessMappingMapper = new RowMapper<UserEntityAccessMapping>() {
        @Override
        public UserEntityAccessMapping mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserEntityAccessMapping userMapping = new UserEntityAccessMapping();
            userMapping.setUserId(rs.getLong("user_id"));
            userMapping.setEntityClass(rs.getString("entity_class"));
            userMapping.setEntityId(rs.getString("entity_id"));
            return userMapping;
        }
    };

    private static RowMapper<String> userEntityIdAccessMappingMapper = new RowMapper<String>() {
        @Override
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString("entity_id");
        }
    };

    public List<UserEntityAccessMapping> getUserEntityAccessMappings(User user) {
        List<UserEntityAccessMapping> userEntityAccessMappings = securityJdbcTemplate
                .query(QUERY_GET_ACCESSIBLE_ENTITY_MAPPINGS_BY_A_USER, userEntityAccessMappingMapper, user.getId());
        return userEntityAccessMappings;
    }

    public List<String> getUserEntityAccessIdMappings(User user) {
        List<String> userEntityAccessMappings = securityJdbcTemplate
                .query(QUERY_GET_ACCESSIBLE_ENTITY_ID_MAPPINGS_BY_A_USER, userEntityIdAccessMappingMapper, user.getId());
        return userEntityAccessMappings;
    }

    public int addUserEntityAccessMappings(List<UserEntityAccessMapping> userEntityAccessMappings) {

        List<Object[]> args = new ArrayList<>();

        for (UserEntityAccessMapping userEntityAccessMapping : userEntityAccessMappings) {
            Object[] rowParams = new Object[3];
            rowParams[0] = userEntityAccessMapping.getUserId();
            rowParams[1] = userEntityAccessMapping.getEntityClass();
            rowParams[2] = userEntityAccessMapping.getEntityId();

            args.add(rowParams);
        }

        int[] ret = securityJdbcTemplate.batchUpdate(QUERY_ADD_ENTITY_ACCESS_MAPPINGS, args);
        int totalInsertedRecords = 0;
        for (int i : ret) {
            totalInsertedRecords += i;
        }
        return totalInsertedRecords;
    }

}

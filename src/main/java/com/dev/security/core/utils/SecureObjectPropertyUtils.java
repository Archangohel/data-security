package com.dev.security.core.utils;

import com.dev.security.core.annotations.SecureEntity;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * Utility for fetching security related info from the target object.
 * @author Archan Gohel
 */
public class SecureObjectPropertyUtils {
    private static Logger logger = LoggerFactory.getLogger(SecureObjectPropertyUtils.class);

    /**
     * Fetches the identity property value. The passed object should be
     * a {@link com.dev.security.core.annotations.SecureEntity}
     *
     * @param object
     * @return
     */
    public static Object getObjectIdentity(Object object)  {
        SecureEntity secureEntityAnnotation = object.getClass().getAnnotation(SecureEntity.class);
        if (secureEntityAnnotation != null) {
            logger.debug("SecureEntity annotation found on {}", object.getClass());
            String identityProperty = secureEntityAnnotation.identity();
            Object identityPropertyValue = null;
            try {
                identityPropertyValue = PropertyUtils.getProperty(object, identityProperty);
                logger.debug("Identity property [{}->{}] found for object [{}]", identityProperty,
                        identityPropertyValue, object);
                return identityPropertyValue;
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.error("Unable to get the identity value for object {}. " +
                        "Got exception while getting the value [{}]", object, e.getMessage(), e);
               //throw e;
                return null;
            }
        } else {
            logger.error("Cant find secure entity annotation! Please pass a valid object");
            throw new IllegalArgumentException("Cant find secure entity annotation! Please pass a valid object");
        }
    }
}

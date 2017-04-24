package com.dev.security.core.aspects;

import com.dev.security.core.annotations.SecureEntity;
import com.dev.security.core.service.EntitySecurityService;
import com.dev.security.core.utils.SecureObjectPropertyUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Aspect that get executed around the secure method. It intercept the response and return only the accessible objects.
 *
 * @auther Archan Gohel
 */

@Aspect
@Component
public class SecureAccessAspect {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EntitySecurityService entitySecurityService;

    @Around("@target(com.dev.security.core.annotations.SecureAccess) || @annotation(com.dev.security.core.annotations.SecureAccess)")
    public Object aroundSecureAccess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        for (Object arg : proceedingJoinPoint.getArgs()) {
            logger.debug("Args {}", arg);
        }
        final long startTime1 = System.currentTimeMillis();
        Object ret = proceedingJoinPoint.proceed();
        final long endTime1 = System.currentTimeMillis();
        logger.debug("Before executing method for {}. Time taken to execute the method is {} ms",
                proceedingJoinPoint.getSignature(), endTime1 - startTime1);

        final long startTime2 = System.currentTimeMillis();
        Object processedObj = processReturnObject(ret);
        final long endTime2 = System.currentTimeMillis();
        logger.debug("After executing the method for {}.  Time taken to execute the security is {} ms",
                proceedingJoinPoint.getSignature(), endTime2 - startTime2);

        return ret;
    }

    private Object processReturnObject(Object object) {
        if (object instanceof Collection) {
            Iterator iterator = ((Collection) object).iterator();
            while (iterator.hasNext()) {
                Object retValue = processReturnObject(iterator.next());
                if (retValue == null) {
                    iterator.remove();
                }
            }
            return object;
        } else if (object instanceof Map) {
            Map<Object, Object> map = (Map) object;
            Iterator<Map.Entry<Object, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> entry = iterator.next();
                Object retValueForKey = processReturnObject(entry.getKey());
                if (retValueForKey != null) {
                    //check value
                    Object retValueForValue = processReturnObject(entry.getValue());
                    if (retValueForValue == null) {
                        iterator.remove();
                    }
                } else {
                    iterator.remove();
                }
            }
            return object;
        } else {
            //check if object is an secure entity.
            //return returnObjectIfAccessibleForThePrincipal(object);

            SecureEntity secureEntityAnnotation = object.getClass().getAnnotation(SecureEntity.class);
            if (secureEntityAnnotation != null) {
                logger.debug("SecureEntity annotation found on {}", object.getClass());
                String identityProperty = secureEntityAnnotation.identity();
                Object identityPropertyValue = null;
                try {
                    identityPropertyValue = PropertyUtils.getProperty(object, identityProperty);
                    logger.debug("Identity property [{}->{}] found for object [{}]", identityProperty,
                            identityPropertyValue, object);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    logger.error("Unable to get the identity value for object {}. " +
                            "Got exception while getting the value [{}]", object, e.getMessage(), e);
                    //return the object
                    return object;
                }
                if (identityProperty == null) {
                    logger.error("Identity value not found for object {}, hence unable to process the security.", object);
                } else {
                    //identity value is non null, check for the access to the current principle
                    if (entitySecurityService.isAccessibleForCurrentPrincipal(object, identityPropertyValue)) {
                        return object;
                    } else {
                        return null;
                    }
                }
            }
            return object;
        }
    }

    private Object returnObjectIfAccessibleForThePrincipal(Object object) {
        Object identityPropertyValue = null;

        identityPropertyValue = SecureObjectPropertyUtils.getObjectIdentity(object);

        if (identityPropertyValue == null) {
            logger.error("Identity value not found for object {}, hence unable to process the security.", object);
            return object;
        } else {
            //identity value is non null, check for the access to the current principle
            if (entitySecurityService.isAccessibleForCurrentPrincipal(object, identityPropertyValue)) {
                return object;
            } else {
                return null;
            }
        }
    }

    //under dev
    private Object processCollection(Collection<Object> collection) {
        Iterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            if (object instanceof Collection) {
                return processCollection((Collection) object);
            } else if (object instanceof Map) {
                return processMap((Map) object);
            }
            //Object retValue = returnObjectIfAccessibleForThePrincipal(object);
            Object retValue = null;
            //identity value is non null, check for the access to the current principle
            if (entitySecurityService.isAccessibleForCurrentPrincipal(object)) {
                retValue = object;
            } else {
                retValue = null;
            }

            if (retValue == null) {
                iterator.remove();
            }
        }
        return collection;
    }

    //under dev
    private Object processMap(Map<Object, Object> map) {
        Iterator<Map.Entry<Object, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> entry = iterator.next();
            if (entry.getKey() instanceof Collection) {
                return processCollection((Collection) entry.getKey());
            } else if (entry.getKey() instanceof Map) {
                return processMap((Map) entry.getKey());
            }
            Object retValueForKey = returnObjectIfAccessibleForThePrincipal(entry.getKey());
            if (retValueForKey != null) {
                //check value
                if (entry.getValue() instanceof Collection) {
                    return processCollection((Collection) entry.getValue());
                } else if (entry.getValue() instanceof Map) {
                    return processMap((Map) entry.getValue());
                }
                Object retValueForValue = processReturnObject(entry.getValue());
                if (retValueForValue == null) {
                    iterator.remove();
                }
            } else {
                iterator.remove();
            }
        }
        return map;
    }

    //under dev
    private Object processReturnObjectV1(Object object) {
        if (object instanceof Collection) {
            return processCollection((Collection) object);
        } else if (object instanceof Map) {
            return processMap((Map) object);
        } else {
            return returnObjectIfAccessibleForThePrincipal(object);
        }
    }
}

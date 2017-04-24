package com.dev.security.core.annotations;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Identity of the target entity.
 *
 * @auther Archan Gohel
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface SecureEntity {
    String identity();
}

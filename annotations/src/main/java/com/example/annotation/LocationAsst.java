package com.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Kenny on 2017/7/7 14:31.
 * Desc:
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface LocationAsst {
    MapType value() default MapType.TYPE_BDMap;
}

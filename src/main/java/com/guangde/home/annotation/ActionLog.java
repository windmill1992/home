package com.guangde.home.annotation;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2018/2/7.
 */


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ActionLog {
    public String content() default "";
}


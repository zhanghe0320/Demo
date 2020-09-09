package com.demo.invoke;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description: 绑定View的注解
 */
@Retention(RetentionPolicy.RUNTIME)//CLASS 编译时注解  RUNTIME运行时注解 SOURCE 源码注解
@Target(ElementType.FIELD)//注解作用范围:FIELD 属性  METHOD方法  TYPE 放在类上
public @interface ViewById {
    int value();
}
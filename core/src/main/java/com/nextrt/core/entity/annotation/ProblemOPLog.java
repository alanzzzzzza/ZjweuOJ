package com.nextrt.core.entity.annotation;

import java.lang.annotation.*;

/**
 * @Program: nextoj
 * @Description: 问题操作记录注解
 * @Author: 轩辕子墨
 * @Create: 2019-12-26 17:56
 **/

//元注解，定义注解被保留策略，一般有三种策略
//1、RetentionPolicy.SOURCE 注解只保留在源文件中，在编译成class文件的时候被遗弃
//2、RetentionPolicy.CLASS 注解被保留在class中，但是在jvm加载的时候北欧抛弃，这个是默认的声明周期
//3、RetentionPolicy.RUNTIME 注解在jvm加载的时候仍被保留
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})//定义了注解声明在哪些元素之前
@Documented
public @interface ProblemOPLog {
    String Desc() default "" ;//描述
}

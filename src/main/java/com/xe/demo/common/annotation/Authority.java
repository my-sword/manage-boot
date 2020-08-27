package com.xe.demo.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解,各项页面菜单
 */
@Target(ElementType.METHOD)//可作用于参数
@Retention(RetentionPolicy.RUNTIME)//元注解：修饰注解，参数表示运行周期
public @interface Authority {
	String opCode();

	String opName();

	int opSeq() default 1;
}

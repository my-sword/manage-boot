package com.xe.demo.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controller层日志拦截注解
 */
//自定义注解作用：说明和调用(AOP调用)。也可以替代配置文件完成对某些功能的描述，减少程序配置
	//调用：@Pointcut("@annotation(com.xe.demo.common.annotation.ServiceLog)")
@Target({ElementType.PARAMETER, ElementType.METHOD})//可作用于参数和方法
@Retention(RetentionPolicy.RUNTIME)//元注解，注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
@Documented//表明这个注解是由 javadoc记录
public @interface ControllerLog {

	String value() default "";
	int type() default 0;
	
}

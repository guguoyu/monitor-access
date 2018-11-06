package com.gtja.monitor.annotation;

/**
 *
 * @author guguoyu
 * @version 1.0
 * @since 2018/10/15
 * @since 1.8
 */
import java.lang.annotation.*;



@Target(ElementType.METHOD)//此注解的作用目标，括号里METHOD的意思说明此注解只能加在方法上面
@Retention(RetentionPolicy.RUNTIME)//注解的保留位置，括号里RUNTIME的意思说明注解可以存在于运行时，可以用于反射
@Documented//说明该注解将包含在javadoc中,也就是说生成javadoc时会生成此注解
public @interface MonitorRequest {
}

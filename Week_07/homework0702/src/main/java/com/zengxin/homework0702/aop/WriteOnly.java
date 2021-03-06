package com.zengxin.homework0702.aop;

import java.lang.annotation.*;

/**
 * 用于标注只写
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface WriteOnly {
}

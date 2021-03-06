package com.zengxin.homework0702.aop;

import java.lang.annotation.*;

/**
 * 用于标注只读
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ReadOnly {

}

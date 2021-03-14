package com.zengxin.myxa.annotation;

import com.zengxin.myxa.enums.Propagation;

import java.lang.annotation.*;

/**
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface GlobalTransactional {

    int timeoutMills() default 60000;

    String name() default "";

    Class<? extends Throwable>[] rollbackFor() default {};

    String[] rollbackForClassName() default {};

    Class<? extends Throwable>[] noRollbackFor() default {};

    String[] noRollbackForClassName() default {};

    Propagation propagation() default Propagation.REQUIRED;

    int lockRetryInternal() default 0;

    int lockRetryTimes() default -1;
}